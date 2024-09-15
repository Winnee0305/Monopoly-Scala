package wn.monopoly.model

trait Ownable{
  var owner: Player
  var price: Int

  def setOwner(player: Player): Unit = {
    owner = player
  }
}