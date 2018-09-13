package com.routes

import cats.data.EitherT
import cats.effect.IO
import com.domain.ServiceError.{InvalidGrant, InvalidRequest}
import com.domain.{Credential, ServiceError, UserToken}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, EntityEncoder, HttpService, Response, Status}

object AuthRoute {

  private implicit val entityDecoder: EntityDecoder[IO, Credential]  = jsonOf[IO, Credential]
  private implicit val entityEncoder: EntityEncoder[IO, ServiceError] = jsonEncoderOf[IO, ServiceError]

  private def toResponse(res: IO[Either[ServiceError, UserToken]]): IO[Response[IO]] = res.flatMap {
    case Right(token) => Ok(token.token)
    case Left(e) =>
      e match {
        case InvalidRequest => BadRequest(e)
        case InvalidGrant   => IO.pure[Response[IO]](Response[IO](status = Status.Unauthorized))
        case _              => InternalServerError()
      }
  }

  def apply(requestToken: Credential => IO[Either[ServiceError, UserToken]]): HttpService[IO] = HttpService[IO] {
    case req @ POST -> Root / "auth" =>
      val res = for {
        parsedBody <- EitherT(req.attemptAs[Credential].value).leftMap(_ => InvalidRequest)
        token      <- EitherT(requestToken(parsedBody))
      } yield token

      toResponse(res.value)
  }
}
