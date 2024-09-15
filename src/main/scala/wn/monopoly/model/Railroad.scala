package wn.monopoly.model

import scalafx.geometry.Point2D

case class Railroad(_coordinate: Point2D,
                    _name: String,
                    _price: Int,
                    rent_1: Int,
                    rent_2: Int,
                    rent_3: Int,
                    rent_4: Int,
                    mortgage: Int)extends BoardElement with Ownable {

  override def name: String = _name
  override def coordinate: Point2D = _coordinate
  override var owner: Player = _
  override var price: Int = _price

  def calcRent(railroads: List[Railroad]): Int = {
    val numOwned = checkNumRailroadOwned(railroads)
    numOwned match{
      case 1 => rent_1
      case 2 => rent_2
      case 3 => rent_3
      case 4 => rent_4
    }
  }

  private def checkNumRailroadOwned(railroads: List[Railroad]): Int = { //to check the number of railroad owner owned
    railroads.count(_.owner == this.owner)
  }
}