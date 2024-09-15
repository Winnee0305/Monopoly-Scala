package wn.monopoly.model

import scalafx.geometry.Point2D

case class Utility(_coordinate: Point2D,
                   _name: String,
                   _price: Int,
                   rent_1u: Int,
                   rent_2u: Int,
                   mortgage: Int)extends BoardElement with Ownable {

  override def name: String = _name
  override def coordinate: Point2D = _coordinate
  override var owner: Player = _
  override var price: Int = _price


  def calcRent(utilities: List[Utility]): Int = {
    if (ownedBothUtilities(utilities)) 10 else 4
  }

  private def ownedBothUtilities(utilities: List[Utility]): Boolean = { //check if the owner owned both utilities
    utilities.forall(_.owner == this.owner)
  }
}