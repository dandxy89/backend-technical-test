package routes

import cats.effect.IO
import com.routes.StatusRoute
import org.http4s.{ Request, Uri }
import org.scalatest.{ FlatSpec, Matchers }
import org.http4s.dsl.io._

class StatusRouteSpec extends FlatSpec with Matchers {

  it should "return 200 (OK)" in {
    val req  = Request[IO](uri = Uri.unsafeFromString("/status"))
    val resp = StatusRoute().run(req).value.unsafeRunSync()

    resp match {
      case Some(r) => r.status shouldBe Ok
      case _       => fail()
    }
  }
}
