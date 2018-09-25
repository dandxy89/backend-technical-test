package com.domain

final case class UserName(value: String) extends AnyVal {
  def caps: String = value.toUpperCase
}
