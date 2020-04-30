package org.scalax.ugeneric.slick.mutiply

import asuna.{Application4, Context4, PropertyTag1}
import slick.ast.{ElementSymbol, Node, Select}
import slick.lifted.{Shape, ShapeLevel}

trait RepNode[RepType, DataType, EncodeRef, Packed] extends Any {
  def buildParams(extract: Any => DataType): Packed
  def encodeRef(rep: RepType, path: Node, index: Int): (EncodeRef, Int)
  def node(rep: RepType, l: List[Node]): List[Node]
  def fieldPlus(data: DataType, l: List[Any]): List[Any]
  def fieldTail(l: List[Any]): (DataType, List[Any])
}

object RepNode {

  implicit def slickRepNodeApplication[F <: ShapeLevel, Rep, Data, Out](
    implicit dd: Shape[F, Rep, Data, Out]
  ): Application4[RepNode, PropertyTag1[Rep, Data], Rep, Data, Any, Out] = new Application4[RepNode, PropertyTag1[Rep, Data], Rep, Data, Any, Out] {
    override def application(context: Context4[RepNode]): RepNode[Rep, Data, Any, Out] = new RepNode[Rep, Data, Any, Out] {
      override def node(rep: Rep, l: List[Node]): List[Node]               = dd.toNode(rep) :: l
      override def fieldPlus(data: Data, l: List[Any]): List[Any]          = data :: l
      override def fieldTail(l: List[Any]): (Data, List[Any])              = (l.head.asInstanceOf[Data], l.tail)
      override def encodeRef(rep: Rep, path: Node, index: Int): (Any, Int) = (dd.encodeRef(rep, Select(path, ElementSymbol(index))), index + 1)
      override def buildParams(extract: Any => Data): Out                  = dd.buildParams(extract)
    }
  }

}