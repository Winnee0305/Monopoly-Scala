package wn.monopoly.model

import scala.util.Random

case class DrawDeck(val cards: List[Card]){

  private val random = new Random()

  def draw(): Card = { //utilise random to draw a card
    val idx = random.nextInt(cards.size)
    cards(idx)
  }
}