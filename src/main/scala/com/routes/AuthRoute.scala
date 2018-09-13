package com.routes

import cats.data.EitherT
import cats.effect.IO
import com.domain.DomainError.InvalidRequest
import com.domain.{Credential, DomainError, UserToken}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, EntityEncoder, HttpService}

object AuthRoute {

  implicit val entityDecoder: EntityDecoder[IO, Credential]  = jsonOf[IO, Credential]
  implicit val entityEncoder: EntityEncoder[IO, DomainError] = jsonEncoderOf[IO, DomainError]

  def apply(requestToken: Credential => IO[Either[DomainError, UserToken]]): HttpService[IO] = HttpService[IO] {
    case req @ POST -> Root / "auth" => {
      val res = for {
        parsedBody <- EitherT(req.attemptAs[Credential].value).leftMap(_ => InvalidRequest)
        token      <- EitherT(requestToken(parsedBody))
      } yield token

      res.value.flatMap {
        case Right(token) => Ok(token.token)
        case Left(e)      => BadRequest(e)
      }
    }
  }
}
