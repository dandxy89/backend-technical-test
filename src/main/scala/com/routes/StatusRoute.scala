package com.routes

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import org.http4s.HttpService
import org.http4s.dsl.io._

object StatusRoute extends LazyLogging {

  def apply(): HttpService[IO] = HttpService[IO] {
    case _ @GET -> Root / "status" =>
      logger.debug("Request for the Status Endpoint received")
      Ok("""{"status": "OK"}""")
  }
}
