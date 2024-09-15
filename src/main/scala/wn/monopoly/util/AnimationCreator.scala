package wn.monopoly.util

import scalafx.animation.{FadeTransition, KeyFrame, ScaleTransition, Timeline, TranslateTransition}
import scalafx.geometry.Point2D
import scalafx.scene.Node
import scalafx.util.Duration
import wn.monopoly.model.Dice
import scala.util.Random

object AnimationCreator {

  def createPulsingEffect[T <: Node](node: T, duration: Double): ScaleTransition = {
    new ScaleTransition(Duration(duration), node) {
      rate = 0.4
      cycleCount = ScaleTransition.Indefinite
      fromX = 0.95
      toX = 0.9
      fromY = 0.95
      toY = 0.9
      autoReverse = true
    }
  }

  def createBouncingEffect[T <: Node](node: T): TranslateTransition = {
    new TranslateTransition(Duration(1200), node) {
      cycleCount = TranslateTransition.Indefinite
      fromY = 0
      toY = -15
      autoReverse = true
    }
  }

  def createFadeTransition[T <: Node](node: T): FadeTransition = {
    new FadeTransition(Duration(200), node) {
      fromValue = 0.4
      toValue = 1.0
      cycleCount = Timeline.Indefinite
      autoReverse = true
    }
  }

  def createTokenTransition[T <: Node](node: T, startXY: Point2D, endXY: Point2D, duration: Double): TranslateTransition = { //create transition from one coordinate to another coordinate
    new TranslateTransition(Duration(duration), node) {
      fromX = startXY.getX
      fromY = startXY.getY
      toX = endXY.getX
      toY = endXY.getY
    }
  }

  def createDiceShufflingAnimation(dice1: Dice, dice2: Dice): Timeline = {
    val random = new Random()
    new Timeline {
      cycleCount = 15
      autoReverse = false
      keyFrames = Seq(
        KeyFrame(Duration(30), onFinished = _ => { //create shuffling effect by randomly switch the dice image
          dice1.imageView.image = dice1.diceImages(random.nextInt(6))
          dice2.imageView.image = dice2.diceImages(random.nextInt(6))
        })
      )
    }
  }
}


