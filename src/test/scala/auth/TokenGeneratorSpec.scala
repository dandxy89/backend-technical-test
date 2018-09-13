package auth

import com.auth.TokenGenerator.generateToken
import com.domain.ServiceError.InvalidGrant
import com.domain.{ User, UserName, UserToken }
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }
import org.joda.time.{ DateTime, DateTimeZone }
import org.scalatest.{ FlatSpec, Matchers }

class TokenGeneratorSpec extends FlatSpec with Matchers {

  behavior of "Token Generator"

  val testDateTime: DateTime            = DateTime.now(DateTimeZone.UTC)
  val datetimeFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
  val validUser                         = User(UserName("dan"))
  val invalidUser                       = User(UserName("Afailure"))

  it should "If the userId of the provided User starts with A, the call will fail." in {
    generateToken(invalidUser, testDateTime) shouldBe Left(InvalidGrant)
  }

  it should "The token attribute for the User Token will be the concatenation of the userId and the current date time in UTC" in {
    generateToken(validUser, testDateTime) shouldBe Right(UserToken(s"dan_${testDateTime.toString(datetimeFormat)}"))
  }
}
