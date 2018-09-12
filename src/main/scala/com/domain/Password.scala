package com.domain

final case class Password(value: String) extends AnyVal {
  override def toString: String = "*************"
}
