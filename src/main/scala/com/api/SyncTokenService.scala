package com.api

import com.domain.{ Credential, User, UserToken }

/**
 * 1. Service Trait / Interface
 * Provide both implementations of requestToken in terms of authenticate and issueToken. By doing
 * that, whoever implements the service will only need to implement authenticate and issueToken.
 */
trait SyncTokenService {
  protected def authenticate(credentials: Credential): User
  protected def issueToken(user: User): UserToken

  def requestToken(credentials: Credential): UserToken = issueToken(authenticate(credentials))
}
