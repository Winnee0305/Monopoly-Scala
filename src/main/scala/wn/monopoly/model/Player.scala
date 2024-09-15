package wn.monopoly.model

import scalafx.animation.FadeTransition
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.shape.Rectangle
import wn.monopoly.util.{AnimationCreator, GameData}


class Player( var name: String,
              var position: Int = 0,
              var balance: Int = 1000,
              var assets: Int = 0,
              var properties: Array[Ownable] = Array[Ownable]()) {

  var token: ImageView = _
  var indicator: Rectangle = _
  var indicatorAnimation: FadeTransition = _
  var img: Image = _

  var previousPosition: Int = _
  var inJail: Boolean = false
  var jailTurns: Int = 0
  var passedGo: Boolean = false

  def initialize(): Unit = {
    if (indicator != null) {
      indicatorAnimation = AnimationCreator.createFadeTransition(indicator) //create animation for the indicator ectangle
    }
    updateAsset()
  }

  def validateName(): Boolean = {
    if (name.isEmpty) { //check if it is empty
      throw new Exception("Name cannot be empty")
    } else if (name.length >= 16) { //check if the length is more than or equal to 16 characters
      throw new Exception("The length of name should within 16 characters")
    }else{
      true
    }
  }

  def updatePosition(diceRolled: Int): Unit = { //update position based on spaces to move
    previousPosition = position //replace the previous position
    position += diceRolled
    if (position > 39) { //meaning that the player had gone around the board
      position -= 40
      passedGo = true
    }
  }

  def updatePosition(lotName: String): Unit = { //update position based on the destination name
    val position = GameData.game.board.getLotPositionByName(lotName)
    updatePosition(calcDistance(position))
  }

  def updatePositionDirectly(lotPosition: Int): Unit = { //update position directly (straight away to the index of the destination lot number)
    previousPosition = position
    position = lotPosition
    if (previousPosition > position) passedGo = true
  }


  private def calcDistance(dest: Int): Int = { //calculate the spaces between destination and current location
    if (dest >= position) dest - position
    else 40 - position + dest
  }


  def addBalance(amount: Int): Unit = {
    this.balance += amount
    updateAsset()
  }

  def deductBalance(amount: Int): Unit = {
    this.balance -= amount
    updateAsset()
  }

  def sufficientBalance(price: Int): Boolean = {
    if(this.balance < price){
      false
    }else{
      true
    }
  }

  def addProperty(property: Ownable): Unit = {
    properties = properties :+ property
    updateAsset()
  }

  def goToJail(): Unit = {
    updatePositionDirectly(10)
    this.inJail = true
  }

  def getOutOfJail(): Unit = {
    this.inJail = false
    this.jailTurns = 0
  }

  private def updateAsset(): Unit = {
    val propertiesValue = properties.map(_.price).sum //asset includes the value of all the properties
    this.assets = balance + propertiesValue
  }
}
