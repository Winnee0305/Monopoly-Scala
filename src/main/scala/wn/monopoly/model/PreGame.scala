package wn.monopoly.model

class PreGame(){ //before the game started
  var player1: Player = new Player(null)
  var player2: Player = new Player(null)

  def validateName(): Boolean = {
    if (player1.name.isEmpty || player2.name.isEmpty) {
      throw new Exception("Players' name cannot be empty!")
    } else{
      if (player1.name == player2.name) {
        throw new Exception("Players' name cannot be the same!")
      } else {
        try {
          player1.validateName() && player2.validateName()
        }
        catch {
          case e: Exception => throw new Exception(e.getMessage)
        }
      }
    }
  }

  def validateToken(): Boolean  = {
    if (player1.img == null || player2.img == null){
      throw new Exception("Please select a token")
    } else if (player1.img == player2.img){
      throw new Exception("Players' token cannot be the same!")
    } else{
      true
    }
  }
}