package com.domain

trait DomainError {
  def error: String
  def description: String
}

object DomainError {
  case object UserNotFound extends DomainError {
    override def error: String = "Invalid User"
    override def description: String = "User is not known to this service"
  }

  case object InvalidGrant extends DomainError {
    override def error: String = "Invalid_grant"
    override def description: String = "Authentication failed from the provided credentials"
  }
}
