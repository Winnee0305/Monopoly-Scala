package wn.monopoly.model

import scalafx.geometry.Point2D

case class Tax(_coordinate: Point2D,
               _name: String,
               price: Int) extends BoardElement{
  override def name: String = _name
  override def coordinate: Point2D = _coordinate

  def calcTax(player: Player): Int = {
    if (name.contains("income")){ //for income tax
      if (player.assets * 0.1 < 200){ //if 10% of the player's assets s les than 200
        (player.assets * 0.1).toInt
      } else{
        200
      }
    } else{
      price
    }
  }
}