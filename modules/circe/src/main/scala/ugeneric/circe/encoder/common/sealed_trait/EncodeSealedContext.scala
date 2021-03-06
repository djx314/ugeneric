package ugeneric.circe.encoder

import zsg.{Context3, Plus3}
import io.circe.Json

class EncodeSealedContext[H] extends Context3[EncodeSealedTraitSelector[H]#JsonEncoder] {
  private val con = EncodeSealedTraitSelector[H]
  override def append[X1, X2, X3, Y1, Y2, Y3, Z1, Z2, Z3](
    x: EncodeSealedTraitSelector[H]#JsonEncoder[X1, X2, X3],
    y: EncodeSealedTraitSelector[H]#JsonEncoder[Y1, Y2, Y3]
  )(plus: Plus3[X1, X2, X3, Y1, Y2, Y3, Z1, Z2, Z3]): EncodeSealedTraitSelector[H]#JsonEncoder[Z1, Z2, Z3] = new con.JsonEncoder[Z1, Z2, Z3] {
    override def subClassToJsonOpt(model: H, classTags: Z2, name: Z3): Option[(String, Json)] = {
      val ax = plus.takeHead3(name)
      val yx = plus.takeTail3(name)
      val cx = plus.takeHead2(classTags)
      val cy = plus.takeTail2(classTags)
      val a  = x.subClassToJsonOpt(model, cx, ax)
      a.orElse(y.subClassToJsonOpt(model, cy, yx))
    }
  }
}

object EncodeSealedContext {
  private val value                    = new EncodeSealedContext[Any]
  def apply[T]: EncodeSealedContext[T] = value.asInstanceOf[EncodeSealedContext[T]]
}
