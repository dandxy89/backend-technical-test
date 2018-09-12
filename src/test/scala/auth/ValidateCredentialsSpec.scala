package auth

import com.auth.ValidateCredentials.checkUsersCredential
import com.domain.DomainError.InvalidGrant
import com.domain.{ Credential, Password, User, UserName }
import org.scalatest.{ FlatSpec, Matchers }

class ValidateCredentialsSpec extends FlatSpec with Matchers {

  behavior of "The validating user name and password"
  // If the password matches the username in uppercase, the validation is a success, otherwise is a failure

  it should "succeed with valid credentials" in {
    val validCred = Credential(UserName("test"), Password("TEST"))

    // The userId of the returned user will be the provided username.
    checkUsersCredential(validCred) shouldBe Right(User(validCred.username))
  }

  it should "fail with incorrect credentials" in {
    val invalidCred = Credential(UserName("test1"), Password("TEST"))

    // Invalid credentials
    checkUsersCredential(invalidCred) shouldBe Left(InvalidGrant)
  }
}
