package routes

import cats.effect.{ IO, Timer }
import com.api.IOAsyncTokenService
import com.auth.TokenGenerator.generateToken
import com.auth.ValidateCredentials.checkUsersCredential
import com.routes.AuthRoute
import org.http4s.headers._
import org.http4s.{ Headers, HttpService, MediaType, Method, Request, Status, Uri }
import org.scalatest.{ FlatSpec, Matchers }

import scala.concurrent.ExecutionContext

class AuthRouteSpec extends FlatSpec with Matchers {

  behavior of "Auth route"

  implicit val t: Timer[IO]  = IO.timer(ExecutionContext.global)
  val route: HttpService[IO] = AuthRoute(new IOAsyncTokenService(checkUsersCredential, generateToken).requestToken)
  val headers: Headers       = Headers(`Content-Type`(MediaType.`application/json`))
  val routeUri: Uri          = Uri.unsafeFromString("/auth")

  it should "return 200 (Ok) when a valid Token has been generated" in {
    val validBody = """{"username": "dan", "password": "DAN" }"""
    val req       = Request[IO](method = Method.POST, uri = routeUri, headers = headers).withBody(validBody).unsafeRunSync()
    val resp      = route.run(req).value.unsafeRunSync()

    resp match {
      case Some(r) => r.status shouldBe Status.Ok
      case _       => fail()
    }
  }

  it should "return 400 (Bad Request) when a valid Token has been generated" in {
    val malformedBody = """{"username": "fail ME" }"""
    val req           = Request[IO](method = Method.POST, uri = routeUri, headers = headers).withBody(malformedBody).unsafeRunSync()
    val resp          = route.run(req).value.unsafeRunSync()

    resp match {
      case Some(r) =>
        r.status shouldBe Status.BadRequest
        r.as[String]
          .unsafeRunSync() shouldBe """{"error":"Bad_request","detail":"Authentication failed from the provided credentials"}"""
      case _ => fail()
    }
  }

  it should "return 401 (Unauthorised) when a valid Token has been generated" in {
    val invalidBody = """{"username": "fail ME", "password": "X" }"""
    val req         = Request[IO](method = Method.POST, uri = routeUri, headers = headers).withBody(invalidBody).unsafeRunSync()
    val resp        = route.run(req).value.unsafeRunSync()

    resp match {
      case Some(r) => r.status shouldBe Status.Unauthorized
      case _       => fail()
    }
  }
}
