package com

import java.util.concurrent.Executors

import cats.effect.IO
import fs2.{ Stream, StreamApp }
import org.http4s.HttpService

import scala.concurrent.ExecutionContext
import scala.util.Properties

object ServiceApp extends StreamApp[IO] {

  val port: Int                     = Properties.envOrNone("HTTP_PORT") map (_.toInt) getOrElse 8080
  val host: String                  = Properties.envOrNone("HTTP_HOST") getOrElse "0.0.0.0"
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  def prepareService: IO[HttpService[IO]] = IO(BackendService())

  override def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] =
    for {
      service <- Stream.eval(prepareService)
      code    <- BackendService.serve(service, host, port)
    } yield code
}
