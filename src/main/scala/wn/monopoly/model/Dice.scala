package wn.monopoly.model

import scalafx.scene.image.{Image, ImageView}
import wn.monopoly.util.MultimediaHandler

class Dice(val imageView: ImageView) {
  var value: Int = 0
  val diceImages: Array[Image] = Array(
    MultimediaHandler.createImage("dice1.png"),
    MultimediaHandler.createImage("dice2.png"),
    MultimediaHandler.createImage("dice3.png"),
    MultimediaHandler.createImage("dice4.png"),
    MultimediaHandler.createImage("dice5.png"),
    MultimediaHandler.createImage("dice6.png"),
  )

  def rollDice(): Unit = {
    val result = (Math.random() * diceImages.length).toInt //utilise random to pick a dice value
    value = result + 1
  }
}
