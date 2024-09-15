package wn.monopoly.view

import scalafx.scene.control.Label
import scalafxml.core.macros.sfxml
import wn.monopoly.model.Property
import wn.monopoly.util.GameData

@sfxml
class PropertyCardController(
                              private val propertyName: Label,
                              private val price: Label,
                              private val rent: Label,
                              private val rentSet: Label,
                              private val h1: Label,
                              private val h2: Label,
                              private val h3: Label,
                              private val h4: Label,
                              private val hotel: Label,
                              private val mortgage: Label,
                              private val housePrice: Label,
                              private val hotelPrice: Label,
                              private val owner: Label
                            ) {

  def setInfo(index: Int): Unit = { //set the attributes of the property accordingly
    val property: Property = GameData.game.board.boardElements(index).asInstanceOf[Property] //retrieve the element
    val style: String = "g%d".format(property.group)
    propertyName.styleClass += style
    propertyName.text = property.name.toUpperCase
    price.text = addCurrency(property.price)
    rent.text = addCurrency(property.rent)
    rentSet.text = addCurrency(property.rent_set)
    h1.text = addCurrency(property.rent_1h)
    h2.text = addCurrency(property.rent_2h)
    h3.text = addCurrency(property.rent_3h)
    h4.text = addCurrency(property.rent_4h)
    hotel.text = addCurrency(property.rent_hotel)
    mortgage.text = addCurrency(property.mortgage)
    housePrice.text = addCurrency(property.price_per_house)
    hotelPrice.text = addCurrency(property.price_per_house)
    if (property.owner != null) owner.text = property.owner.name else owner.text = "None"
  }

  def addCurrency(num: Int): String = {
    "$%s".format(num.toString)
  }
}
