package wn.monopoly.model

import scalafx.geometry.Point2D

trait BoardElement{
  def name: String
  def coordinate: Point2D
}