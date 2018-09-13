package api

import cats.effect.{IO, Timer}
import com.api.IOAsyncTokenService
import com.domain.ServiceError.InvalidGrant
import com.domain._
import org.joda.time.DateTime
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext

class AsyncTokenServiceSpec extends FlatSpec with Matchers {

  behavior of "IO Sync Token Service"

  implicit val t: Timer[IO] = IO.timer(ExecutionContext.global)

  val validCredentials: Credential => Either[ServiceError, User]   = _ => Right(User(UserName("Dan")))
  val invalidCredentials: Credential => Either[ServiceError, User] = _ => Left(InvalidGrant)

  val validToken: (User, DateTime) => Either[ServiceError, UserToken]   = (_, _) => Right(UserToken("XYZ"))
  val invalidToken: (User, DateTime) => Either[ServiceError, UserToken] = (_, _) => Left(InvalidGrant)

  it should "Successfully generate a token" in {
    val ioHandler = new IOAsyncTokenService(validCredentials, validToken)

    ioHandler.requestToken(Credential(UserName("XYZ"), Password("123"))).unsafeRunSync() shouldBe Right(UserToken("XYZ"))
  }

  it should "fail to generate when the credentials are invalid" in {
    val ioHandler = new IOAsyncTokenService(invalidCredentials, validToken)

    ioHandler.requestToken(Credential(UserName("XYZ"), Password("123"))).unsafeRunSync() shouldBe Left(InvalidGrant)
  }

  it should "fail to generate when the username is invalid" in {
    val ioHandler = new IOAsyncTokenService(validCredentials, invalidToken)

    ioHandler.requestToken(Credential(UserName("XYZ"), Password("123"))).unsafeRunSync() shouldBe Left(InvalidGrant)
  }

}
