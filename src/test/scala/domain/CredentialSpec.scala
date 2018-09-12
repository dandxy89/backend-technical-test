package domain

import com.domain.{ Credential, Password, UserName }
import io.circe.Json
import org.scalatest.{ FlatSpec, Matchers }
import io.circe.parser.parse

class CredentialSpec extends FlatSpec with Matchers {

  val validJson: Json   = parse("""{"username": "dan", "password": "123"}""").getOrElse(fail())
  val invalidJson: Json = parse("""{"username": "dan", "pa1ssword": "123"}""").getOrElse(fail())

  it should "decode a correctly formed json payload" in {
    validJson.as[Credential].toOption shouldBe Some(Credential(UserName("dan"), Password("123")))
  }

  it should "not decode a malformed json payload" in {
    invalidJson.as[Credential].toOption shouldBe None
  }
}
