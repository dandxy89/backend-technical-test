package com.domain

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class UserToken(token: String)

object UserToken {
  implicit val decoder: Encoder[UserToken] = deriveEncoder
}
