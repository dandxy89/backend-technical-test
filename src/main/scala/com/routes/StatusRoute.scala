package com.routes

import cats.effect.IO
import org.http4s.HttpService
import org.http4s.dsl.io._

object StatusRoute {

  def apply(): HttpService[IO] = HttpService[IO] {
    case _ @GET -> Root / "status" => Ok("""{"status": "OK"}""")
  }
}
