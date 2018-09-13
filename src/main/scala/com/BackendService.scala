package com

import cats.effect.{ IO, Timer }
import cats.implicits._
import com.api.IOAsyncTokenService
import com.auth.TokenGenerator.generateToken
import com.auth.ValidateCredentials.checkUsersCredential
import com.routes.{ AuthRoute, StatusRoute }
import fs2.{ Stream, StreamApp }
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

/**
 * 3. REST API
 * Task: Define a simple REST API to offer the functionality of the SimpleAsyncTokenService implemented in the previous block.
 */
object BackendService {

  def apply()(implicit t: Timer[IO]): HttpService[IO] = {

    val tokenHandler = new IOAsyncTokenService(checkUsersCredential, generateToken)

    StatusRoute() <+>
      AuthRoute(tokenHandler.requestToken)
  }

  def serve(service: HttpService[IO], host: String, port: Int)(implicit ec: ExecutionContext): Stream[IO, StreamApp.ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(port, host)
      .mountService(service, "/")
      .withNio2(false)
      .withWebSockets(false)
      .withExecutionContext(ec)
      .enableHttp2(false)
      .serve
}
