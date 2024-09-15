package wn.monopoly.model

import scalafx.geometry.Point2D

case class Property(_coordinate: Point2D,
                    _name: String,
                    group: Int,
                    _price: Int,
                    price_per_house: Int,
                    rent: Int,
                    rent_set: Int,
                    rent_1h: Int,
                    rent_2h: Int,
                    rent_3h: Int,
                    rent_4h: Int,
                    rent_hotel: Int,
                    mortgage: Int
                   ) extends BoardElement with Ownable {

  private var houseBuilt: Int = 0 //by default no house built
  private var hotelBuilt: Boolean = false //by default no hotel built

  override def name: String = _name
  override def coordinate: Point2D = _coordinate
  override var owner: Player = _
  override var price: Int = _price

  def calcRent(propertiesInSameGroup: List[Property]): Int = {
    if (hotelBuilt) rent_hotel
    else houseBuilt match{
      case 0 => if (ownerOwnedSet(propertiesInSameGroup)) rent_set else rent
      case 1 => rent_1h
      case 2 => rent_2h
      case 3 => rent_3h
      case 4 => rent_4h
    }
  }

  private def ownerOwnedSet(properties: List[Property]): Boolean = { //to check if the owner woned a colour group of properties
    properties.forall(_.owner == this.owner)
  }
}