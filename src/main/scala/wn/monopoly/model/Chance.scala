package wn.monopoly.model

import scalafx.geometry.Point2D

case class Chance(_coordinate: Point2D,
                  _name: String) extends BoardElement{

  override def name: String = _name
  override def coordinate: Point2D = _coordinate
}