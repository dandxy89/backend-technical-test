package com.domain

import io.circe.{ Encoder, Json }
import io.circe.syntax._

trait ServiceError {
  def error: String
  def description: String
}

object ServiceError {
  case object UserNotFound extends ServiceError {
    override def error: String       = "Invalid User"
    override def description: String = "User is not known to this service"
  }

  case object InvalidGrant extends ServiceError {
    override def error: String       = "Invalid_grant"
    override def description: String = "Authentication failed from the provided credentials"
  }

  case object InvalidRequest extends ServiceError {
    override def error: String       = "Bad_request"
    override def description: String = "Authentication failed from the provided credentials"
  }

  implicit val encoder: Encoder[ServiceError] = Encoder.instance { v =>
    Json.obj("error" -> v.error.asJson, "detail" -> v.description.asJson)
  }
}
