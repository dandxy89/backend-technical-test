package com.auth

import com.domain.DomainError.InvalidGrant
import com.domain.{ DomainError, User, UserToken }
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object TokenGenerator {

  private val datetimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")

  def generateToken: (User, DateTime) => Either[DomainError, UserToken] =
    (user, dt) =>
      Either.cond(!user.userId.value.startsWith("A"),
                  UserToken(s"${user.userId.value}_${dt.toString(datetimeFormat)}"),
                  InvalidGrant)
}
