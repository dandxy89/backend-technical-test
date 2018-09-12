package domain

import com.domain.Password
import org.scalatest.{ FlatSpec, Matchers }

class PasswordSpec extends FlatSpec with Matchers {

  behavior of "Password case class security"

  it should "expose the password easily" in {
    Password("my_secret").toString shouldBe "*************"
  }
}
