package com

import cats.effect.{IO, Timer}
import org.http4s.client.Client
import org.http4s.client.blaze.{BlazeClientConfig, Http1Client}
import org.http4s.dsl.io._
import org.http4s.headers._
import org.http4s.{Headers, MediaType, Method, Request, Uri}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext
import scala.util.Properties

class AuthRouteITest extends FlatSpec with Matchers {

  val protocol: String = "http"
  val port: Int        = Properties.envOrNone("HTTP_PORT") map (_.toInt) getOrElse 8080
  val host: String     = Properties.envOrNone("HTTP_HOST") getOrElse "0.0.0.0"

  behavior of "Auth route"

  implicit val t: Timer[IO] = IO.timer(ExecutionContext.global)
  val headers: Headers      = Headers(`Content-Type`(MediaType.`application/json`))
  val routeUri: Uri         = Uri.unsafeFromString(s"$protocol://$host:$port/auth")

  val testingClient: Client[IO] = Http1Client[IO](BlazeClientConfig.defaultConfig).unsafeRunSync()

  it should "return 200 (Ok) when a valid Token has been generated" in {
    val validBody = """{"username": "dan", "password": "DAN" }"""
    val req       = Request[IO](method = Method.POST, uri = routeUri, headers = headers).withBody(validBody)
    val resp      = testingClient.fetch(req)(IO.pure).unsafeRunSync()

    resp.status shouldBe Ok
    resp.as[String].unsafeRunSync() shouldBe a[String]
  }

  it should "return 400 (Bad Request) when a valid Token has been generated" in {
    val malformedBody = """{"username": "fail ME" }"""
    val req           = Request[IO](method = Method.POST, uri = routeUri, headers = headers).withBody(malformedBody).unsafeRunSync()
    val resp          = testingClient.fetch(req)(IO.pure).unsafeRunSync()

    resp.status shouldBe BadRequest
    resp
      .as[String]
      .unsafeRunSync() shouldBe """{"error":"Bad_request","detail":"Authentication failed from the provided credentials"}"""
  }

  it should "return 401 (Unauthorised) when a valid Token has been generated" in {
    val invalidBody = """{"username": "fail ME", "password": "X" }"""
    val req         = Request[IO](method = Method.POST, uri = routeUri, headers = headers).withBody(invalidBody).unsafeRunSync()
    val resp        = testingClient.fetch(req)(IO.pure).unsafeRunSync()

    resp.status shouldBe Unauthorized
  }
}
