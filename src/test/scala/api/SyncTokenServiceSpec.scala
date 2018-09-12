package api

import com.api.SyncTokenService
import com.domain._
import org.scalatest.{ FlatSpec, Matchers }

class SyncTokenServiceSpec extends FlatSpec with Matchers {

  behavior of "Sync Token Service"

  val testSyncClass: SyncTokenService = new SyncTokenService {
    override protected def authenticate(credentials: Credential): User = User(credentials.username)
    override protected def issueToken(user: User): UserToken           = UserToken(user.userId.value)
  }

  it should "generate a token" in {
    testSyncClass.requestToken(Credential(UserName("DAN"), Password("123"))) shouldBe UserToken("DAN")
  }
}
