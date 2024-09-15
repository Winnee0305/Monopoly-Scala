package wn.monopoly.view

import scalafx.scene.control.Label
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import wn.monopoly.model.Utility
import wn.monopoly.util.{GameData, MultimediaHandler}

@sfxml
class UtilityCardController(private val utilityImage: ImageView,
                            private val name: Label,
                            private val price: Label,
                            private val owner: Label) {

  def setInfo(index: Int): Unit = { //set the attributes of the utility accordingly
    val utility: Utility = GameData.game.board.boardElements(index).asInstanceOf[Utility] //get the element
    val img = utility._name.toLowerCase match { //set the image
      case name if name.contains("electric") => "electric.gif"
      case name if name.contains("water") => "water.gif"
    }
    utilityImage.image = MultimediaHandler.createImage(img) //set the image
    name.text = utility._name.toUpperCase
    price.text = "$%d".format(utility.price)
    if (utility.owner != null) owner.text = utility.owner.name else owner.text = "None"
  }
}