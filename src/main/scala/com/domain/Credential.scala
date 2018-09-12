package com.domain

import io.circe.Decoder

final case class Credential(username: UserName, password: Password)

object Credential {

  implicit val decoder: Decoder[Credential] = Decoder.instance { c =>
    for {
      userId   <- c.downField("username").as[String]
      password <- c.downField("password").as[String]
    } yield Credential(UserName(userId), Password(password))
  }
}
