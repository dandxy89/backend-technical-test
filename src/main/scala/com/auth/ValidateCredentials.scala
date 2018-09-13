package com.auth

import com.domain.ServiceError.InvalidGrant
import com.domain.{Credential, ServiceError, User}

object ValidateCredentials {

  def checkUsersCredential: Credential => Either[ServiceError, User] =
    cred => Either.cond(cred.username.caps == cred.password.value, User(cred.username), InvalidGrant)
}
