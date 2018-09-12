package com

import cats.effect.IO
import com.routes.StatusRoute
import org.http4s.HttpService
import fs2.{ Stream, StreamApp }
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object BackendService {

  def apply(): HttpService[IO] = StatusRoute()

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
