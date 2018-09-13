package com.domain

import io.circe.{ Encoder, Json }
import io.circe.syntax._

trait DomainError {
  def error: String
  def description: String
}

object DomainError {

  implicit val encoder: Encoder[DomainError] = Encoder.instance { v =>
    Json.obj("error" -> v.error.asJson, "detail" -> v.description.asJson)
  }

  case object UserNotFound extends DomainError {
    override def error: String       = "Invalid User"
    override def description: String = "User is not known to this service"
  }

  case object InvalidGrant extends DomainError {
    override def error: String       = "Invalid_grant"
    override def description: String = "Authentication failed from the provided credentials"
  }

  case object InvalidRequest extends DomainError {
    override def error: String       = "Bad_request"
    override def description: String = "Authentication failed from the provided credentials"
  }
}
