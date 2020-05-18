package org.scalax.ugeneric.circe.encoder.common.sealed_trait

import zsg.macros.ByNameImplicit
import zsg.macros.single.SealedTag
import io.circe.{Encoder, Json}

class EncodeSealedTraitSelector[H] {
  trait JsonEncoder[M, T, II] {
    def subClassToJsonOpt(model: H, classTags: T, labelled: II): Option[(String, Json)]
  }
}

object EncodeSealedTraitSelector {

  private val value                          = new EncodeSealedTraitSelector[Any]
  def apply[T]: EncodeSealedTraitSelector[T] = value.asInstanceOf[EncodeSealedTraitSelector[T]]

  implicit final def asunaCirceSealedEncoder[T, R](
    implicit t: ByNameImplicit[Encoder[R]]
  ): EncodeSealedTraitSelector[T]#JsonEncoder[SealedTag[R], Class[R], String] = {
    val con = EncodeSealedTraitSelector[T]
    new con.JsonEncoder[SealedTag[R], Class[R], String] {
      override def subClassToJsonOpt(model: T, classTags: Class[R], labelled: String): Option[(String, Json)] = {
        if (classTags.isInstance(model))
          Some((labelled, t.value(classTags.cast(model))))
        else
          Option.empty
      }
    }
  }

}
