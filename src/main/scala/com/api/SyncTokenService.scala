package com.api

import com.domain.{ Credential, User, UserToken }

trait SyncTokenService {
  protected def authenticate(credentials: Credential): User
  protected def issueToken(user: User): UserToken

  def requestToken(credentials: Credential): UserToken = issueToken(authenticate(credentials))
}
