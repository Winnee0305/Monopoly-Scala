package wn.monopoly.model

class Card(val desc: String,
           val typeName: String,
           val action: Player => Unit) { //to be used when creating chance cards and community chests

  def apply(player: Player): Unit = action(player)
}