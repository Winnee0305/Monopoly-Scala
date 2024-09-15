package wn.monopoly.view

import wn.monopoly.model.Railroad
import scalafx.scene.control.Label
import scalafxml.core.macros.sfxml
import wn.monopoly.util.GameData

@sfxml
class RailroadCardController(private val name: Label,
                             private val price: Label,
                             private val rent: Label,
                             private val rent2: Label,
                             private val rent3: Label,
                             private val rent4: Label,
                             private val mortgage: Label,
                             private val owner: Label) {

  def setInfo(index: Int): Unit = { //set the attributes of the railroad accordingly
    val railroad: Railroad = GameData.game.board.boardElements(index).asInstanceOf[Railroad] //retrieve the element
    name.text = railroad.name.toUpperCase
    price.text = addCurrency(railroad.price)
    rent.text = addCurrency(railroad.rent_1)
    rent2.text = addCurrency(railroad.rent_2)
    rent3.text = addCurrency(railroad.rent_3)
    rent4.text = addCurrency(railroad.rent_4)
    mortgage.text = addCurrency(railroad.mortgage)
    if (railroad.owner != null) owner.text = railroad.owner.name else owner.text = "None"
  }

  def addCurrency(num: Int): String = {
    "$%s".format(num.toString)
  }
}