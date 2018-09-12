package com.auth

import com.domain.DomainError.InvalidGrant
import com.domain.{Credential, DomainError, User}

object ValidateCredentials {

  def checkUsersCredential: Credential => Either[DomainError, User] =
    cred => Either.cond(cred.username.caps == cred.password.value, User(cred.username), InvalidGrant)
}
