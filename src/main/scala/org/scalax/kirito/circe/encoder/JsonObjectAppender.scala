package org.scalax.kirito.circe.encoder

import io.circe.Json

abstract class JsonObjectAppender[T] {
  def appendField(data: T, m: List[(String, Json)]): List[(String, Json)]
}
