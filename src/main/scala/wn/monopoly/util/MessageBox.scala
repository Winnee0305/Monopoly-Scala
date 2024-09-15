package wn.monopoly.util

object MessageBox { //to be used to display instruction
  def startGame(totRound: Int): String = {
    "Game started!\nTry to accumulate the highest assets or bankrupt your opponent in %d rounds!".format(totRound)
  }

  private def dice(dice1: Int, dice2: Int): String = {
    "You rolled %d + %d = %d".format(dice1, dice2, dice1 + dice2)
  }

  def draw(str: String): String = {
    "You drawn card:\n%s".format(str)
  }

  def rollDoubleOutJail(dice1: Int, dice2: Int): String = {
    "%s\nSuccessfully rolled a double!, you can get out of jail!".format(dice(dice1, dice2))
  }

  def failedDouble(dice1: Int, dice2: Int): String = {
    "%s\nFailed to get out of jail!".format(dice(dice1, dice2))
  }

  def outJail(): String = {
    "You have stayed in jail for 3 rounds, now you can get out of jail!"
  }
  def landedOn(str: String): String = {
    "You landed on %s.".format(str)
  }

  def buy(name: String, price: Int): String = {
    "%s\nYou can choose to buy the property with price $%d".format(landedOn(name), price)
  }

  def rent(name: String, owner: String, price: Int): String = {
    "%s \nThis property is owned by %s, please pay $%d for rent".format(landedOn(name), owner, price)
  }

  def chance(): String = {
    "You landed on chance! \nClick the button to draw a chance card."
  }

  def chest(): String = {
    "You landed on community chest! \nClick the button to draw a chance card."
  }

  def tax(name: String, amount: Int): String = {
    "You need to pay $%d for %s".format(amount, name)
  }

  def preJail(): String = {
    "Go to jail!"
  }

  def passGo(): String = {
    "You passed through the GO, earned $200!"
  }

  def insufficientBalance(): String = {
    "You dont have enough balance!"
  }

  def ownProperty(name: String): String = {
    "%s is owned by you :), no need to pay and just pass through.".format(name)
  }

  def buyProperty(name: String): String = {
    "You owned %s!".format(name)
  }

  def rentFree(name: String, day: Int): String = {
    "No need to pay rent!\nThe owner of %s is in jail Day %d.".format(name, day)
  }

  def inJail(num: Int): String = {
    "You are in jail turn day %d, try to roll a double to get out of jail!".format(num)
  }

  def endGame(): String = {
    "Game ended!"
  }

  def won(winner: String): String = {
    "Congratulations! %s won the game!".format(winner)
  }

  def bankrupt(name: String): String = {
    "%s has bankrupt!".format(name)
  }

  def highestAssets(winner: String, assets: Int): String = {
    "%s has the highest assets with $%d!".format(winner, assets)
  }

  def writeRecord(): String = {
    "This game has been recorded!"
  }

  def exit(): String = {
    "Exit to home before game ended will lost the game record! \nAre you sure you want to leave?"
  }

  def confirm(): String = {
    "Press 'Back to Home' to leave\n Press 'Continue' to continue the game"
  }

  def currencyFormatter(amount: Int): String = {
    val amountStr = amount.toString.reverse
    val formatted = amountStr.grouped(3).mkString(",").reverse
    "$%s".format(formatted)
  }
}