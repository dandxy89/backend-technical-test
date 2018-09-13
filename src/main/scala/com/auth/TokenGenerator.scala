package com.auth

import com.domain.ServiceError.InvalidGrant
import com.domain.{ ServiceError, User, UserToken }
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object TokenGenerator {

  private val datetimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

  def generateToken: (User, DateTime) => Either[ServiceError, UserToken] =
    (user, dt) =>
      Either.cond(!user.userId.value.startsWith("A"),
                  UserToken(s"${user.userId.value}_${dt.toString(datetimeFormat)}"),
                  InvalidGrant)
}
