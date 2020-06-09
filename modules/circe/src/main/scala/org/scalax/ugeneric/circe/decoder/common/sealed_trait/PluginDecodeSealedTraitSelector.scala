package org.scalax.ugeneric.circe.decoder.common.sealed_trait

import zsg.macros.ByNameImplicit
import zsg.macros.single.SealedTag
import io.circe.Decoder
import org.scalax.ugeneric.circe.NameTranslator

class PluginDecodeSealedTraitSelector[P] {

  trait JsonDecoder[I, II] {
    def getValue(name: II, i: Option[NameTranslator]): Decoder[P]
  }

}

object PluginDecodeSealedTraitSelector {

  def apply[T]: PluginDecodeSealedTraitSelector[T]        = value.asInstanceOf[PluginDecodeSealedTraitSelector[T]]
  private val value: PluginDecodeSealedTraitSelector[Any] = new PluginDecodeSealedTraitSelector[Any]

  implicit def asunaCirceSealedDecoder[T, R <: T](
    implicit t: ByNameImplicit[Decoder[R]]
  ): PluginDecodeSealedTraitSelector[T]#JsonDecoder[SealedTag[R], String] = {
    val con = PluginDecodeSealedTraitSelector[T]
    new con.JsonDecoder[SealedTag[R], String] {
      override def getValue(name: String, i: Option[NameTranslator]): Decoder[T] = {
        val nameI = i.map(_.tran(name)).getOrElse(name)
        Decoder.instance {
          _.get(nameI)(t.value): Decoder.Result[R]
        }
      }
    }
  }

}