package org.scalax.ugeneric.circe.decoder.common.model

import asuna.macros.single.DefaultValue
import asuna.{Application4, PropertyTag}
import io.circe._
import org.scalax.ugeneric.circe.NameTranslator

trait PluginDecodeContent[N, Model, Name, DefaultValue] extends Any {
  def getDecoder(name: Name, defaultValue: DefaultValue, p: Option[NameTranslator], useDefaultValue: Boolean): Decoder[Model]
}

object PluginDecodeContent {

  implicit def asunaPlaceHolderDecoder[T](implicit dd: => Decoder[T]): Application4[PluginDecodeContent, PropertyTag[T], T, String, DefaultValue[T]] = {
    _ => (name, defaultValue, p, useDefault) =>
      val nameI = p.map(_.tran(name)).getOrElse(name)

      { j =>
        val fieldValue = j.downField(nameI)
        val value      = fieldValue.as(dd)

        if (value.isRight) {
          value
        } else {
          //if this field is not exists or this field is null
          if (fieldValue.focus.map(_.isNull).getOrElse(true) && useDefault) {
            defaultValue.value.map(Right.apply).getOrElse(value)
          } else {
            value
          }
        }
      }
  }

}
