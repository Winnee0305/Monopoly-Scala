package wn.monopoly.model

import wn.monopoly.util.{GameData, MessageBox}

class Game() {
  var board: Board = new Board() //create a Board instance for a game

  //to be set in setup page
  var player1: Player = _
  var player2: Player = _
  var totRound: Int = _

  // to store the game status
  var numRound: Int = 1 //the game start from round 1
  var dice1: Dice = _
  var dice2: Dice = _
  var currentPlayer: Player = _
  var cardDrawn: Card = _ //store the card drawn
  var payment: Payment = _ //store the payment information
  var instruction: Instruction = _ //an instruction instance (important when storing the information to be display after each game event)
  var winner: Player = _
  var hasRolled: Boolean = false
  var hasMoved: Boolean = false
  var insufficientBalance = false
  var hasBrought: Boolean = false
  var bankrupt: Player = null

  // create chanceDeck and chestDeck for a game
  var chanceDeck: DrawDeck = _
  var chestDeck: DrawDeck = _

  def initializeGame(mapName: String): Unit = {
    board.initializeMap(mapName) //initialize the board (load the specified board information from csv file, store all information in the board instance)
    currentPlayer = player1 // set current player (by default start from player 1)

    // initialize the decks
    initializeChanceDeck()
    initializeChestDeck()
  }

  private def initializeChanceDeck(): Unit = {
    val chanceCards: List[Card] = List( //create a list of cards, by setting the description, image and actions
      new Card("Advance To Go", "income", player => {
        hasMoved = false
        currentPlayer.passedGo = true
        player.updatePosition("base")
      }),
      new Card("Go To Jail. Go directly to jail, do not pass Go, do not collect $200", "jail", player => {
        hasMoved = false
        player.goToJail()
      }),
      new Card("Your building loan matures. Collect $150", "income", player => {
        player.addBalance(150)
      }),
      new Card("Bank pays you dividend of $50", "income", player => {
        player.addBalance(50)
      }),
      new Card("Speeding fine $15", "pay", player => {
        payment = Payment(15, "Speeding Fine", null)
      }),
      new Card("Advance to %s ".format(board.boardElements(39).name), "run", player => {
        hasMoved = false
        player.updatePosition(board.boardElements(39).name)
      }),
      new Card("Advance to %s ".format(board.boardElements(25).name), "run", player => {
        hasMoved = false
        player.updatePosition(board.boardElements(25).name)
      }),
      new Card("Advance to %s ".format(board.boardElements(12).name), "run", player => {
        hasMoved = false
        player.updatePosition(board.boardElements(12).name)
      }),
      new Card("Advance to %s ".format(board.boardElements(6).name), "run", player => {
        hasMoved = false
        player.updatePosition(board.boardElements(6).name)
      }),
      new Card("Advance to %s ".format(board.boardElements(29).name), "run", player => {
        hasMoved = false
        player.updatePosition(board.boardElements(29).name)
      }),
    )
    chanceDeck = DrawDeck(chanceCards) //create a draw deck with this list of chance cards
  }

  private def initializeChestDeck(): Unit = {
    val chestCards: List[Card] = List( //create a list of cards, by setting the description, image and actions
      new Card("Advance To Go", "income", player => {
        hasMoved = false
        player.updatePositionDirectly(0)
        player.addBalance(200)
      }),
      new Card("Doctor's fee. Pay $50", "pay", player => {
        payment = Payment(50, "Doctor's fee", null)
      }),
      new Card("Go To Jail. Go directly to jail, do not pass Go, do not collect $200", "jail", player => {
        hasMoved = false
        player.goToJail()
      }),
      new Card("Income tax refund. Collect $20", "income", player => {
        player.addBalance(20)
      }),
      new Card("Pay hospital fess of $100", "pay", player => {
        payment = Payment(100, "Hospital fess", null)
      }),
      new Card("Holiday fund matures. Receive $100", "income", player => {
        player.addBalance(100)
      }),
      new Card ("Bank error in your favor. Collect $200", "income", player =>{
        player.addBalance(200)
      }),
      new Card("From sale of stock you get $50", "income", player => {
        player.addBalance(50)
      }),
      new Card ("Life insurance matures. Collect $100", "income", player => {
        player.addBalance(100)
      }),
      new Card ("Pay school fees of $100", "pay", player => {
        payment = Payment(100, "School fees", null)
      }),
      new Card ("Receive $25 consultancy fee", "income", player => {
        player.addBalance(25)
      }),
      new Card ("You have won second prize in a beauty contest. Collect $10", "income", player => {
        player.addBalance(10)
      })
    )
    chestDeck = DrawDeck(chestCards) //create a draw deck with this list of chest cards
  }

  def startGame(): Unit = {
    instruction = Instruction(MessageBox.startGame(totRound), "start")
  }

  def rollDice(): Unit = {
    dice1.rollDice()
    dice2.rollDice()
    hasRolled = true
    if(!currentPlayer.inJail) { //update position only if player not in jail
      currentPlayer.updatePosition(dice1.value + dice2.value)
    } else{
      hasMoved = true //so that the move token animation will not play
    }
  }

  private def resetStatus(): Unit = { //reset all the status when end turn
    cardDrawn = null
    payment = null
    hasRolled = false
    hasMoved = false
    instruction = null
    insufficientBalance = false
    hasBrought = false
  }

  def updateStatus(): Unit = { //update after player rolled dice and moved(to manage the game event based on where the player landed)
    if (currentPlayer.inJail && currentPlayer.jailTurns >= 1) { //if player is in the jail
      manageJail()
    } else { //if player is not in the jail
      hasMoved = true
      manageRegular()
    }
  }

  private def manageJail()= { //manage game event when the player is in jail
    if (!hasRolled) { //use when the player is in jail and the beginning of a new turn
      if (currentPlayer.jailTurns < 4) { // player still need to stay in jail
        instruction = Instruction(MessageBox.inJail(currentPlayer.jailTurns), "jail")
      } else { //player stayed at jail for 3 turns, can go out
        outJail()
        instruction = Instruction(MessageBox.outJail(), "outJail")
      }
    } else {
      if (dice1.value == dice2.value) { //if the dice rolled is double
        currentPlayer.getOutOfJail() //can go out jail
        hasMoved = false //can move the token
        currentPlayer.updatePosition(dice1.value + dice2.value) //update the position
        instruction = Instruction(MessageBox.rollDoubleOutJail(dice1.value, dice2.value), "dice")
      } else { //failed to roll a double, need to stay in jail
        instruction = Instruction(MessageBox.failedDouble(dice1.value, dice2.value), "dice")
      }
    }
  }

  private def manageRegular(): Unit = { //manage game event when the player is not in jail
    getCurrentLot() match { //check the lot landed on
      case ownable: Ownable => //landed on an Ownable lot
        if (ownable.owner == null){ //no owner
          instruction = Instruction(MessageBox.buy(ownable.name, ownable.price), "property")
        } else{
          if (ownable.owner == currentPlayer){ //player is the owner
            instruction = Instruction(MessageBox.ownProperty(ownable.name), "owner")
          }else {
            if (ownable.owner.inJail){ //the owner is in jail
              instruction = Instruction(MessageBox.rentFree(ownable.name, ownable.owner.jailTurns), "free")
            } else { //need to pay rent
              landedOnRent()
              instruction = Instruction(MessageBox.rent(ownable.name, ownable.owner.name, payment.amount), "rent")
            }
          }
        }
      case _: Chance => //landed on Chance
        instruction = Instruction(MessageBox.chance(), "gamble")
      case _: Chest => //landed on Chest
        instruction = Instruction(MessageBox.chest(), "gamble")
      case tax: Tax => //landed on Tax
        landedOnTax()
        instruction = Instruction(MessageBox.tax(tax.name, payment.amount), "tax")
      case _: PreJail => //landed on go to jail
        goToJail()
        instruction = Instruction(MessageBox.preJail(), "jail")
      case _ => instruction = null
    }
  }

  def drawCard(drawDeck: DrawDeck): Unit = { //draw a card from draw deck passing in
    cardDrawn = drawDeck.draw()
    instruction = Instruction(cardDrawn.desc, cardDrawn.typeName)
    cardDrawn.action(currentPlayer) //execute the actions on the card
  }

  private def landedOnTax(): Unit = {
    val tax = getCurrentLot().asInstanceOf[Tax].calcTax(currentPlayer) //calculate the tax need to pay
    payment = Payment(tax, "Tax", null) //create a Payment instance
  }

  private def landedOnRent(): Unit = {
    val lot: Ownable = getCurrentLot().asInstanceOf[Ownable]
    var rent = board.calcRent(lot) //calculate the rent need to pay
    if (lot.isInstanceOf[Utility]){ //special rent calculation for Utility
      rent *= (dice1.value + dice2.value)
    }
    payment = Payment(rent, "Rent", lot.owner) //create a Payment instance
  }

  def buyProperty(): Unit = {
    val lot: Ownable = getCurrentLot().asInstanceOf[Ownable]
    val price = lot.price
    if (GameData.game.currentPlayer.sufficientBalance(price)) { //if the player has sufficient balance to buy the property
      hasBrought = true //set status
      pay(price, None)
      lot.setOwner(currentPlayer)
      currentPlayer.addProperty(lot)
      instruction = Instruction(MessageBox.buyProperty(getCurrentLot().name), "owner")
    } else{ //if teh player has insufficient balance
      instruction = Instruction(MessageBox.insufficientBalance(), "noMoney")
    }
  }

  def payPayment(): Unit = {
    pay(payment.amount, payment.beneficial)
  }

  def pay(price: Int, beneficial: Any): Unit = {
    if (currentPlayer.sufficientBalance(price)) { //if player has sufficient balance
      currentPlayer.deductBalance(price) //deduct the price
      beneficial match {
        case player: Player => player.addBalance(price) //add the price to beneficial party if got
        case _ => None
      }
      payment = null //clear the payment after paid
    } else{ //if play has insufficient balance
      instruction = Instruction(MessageBox.insufficientBalance(), "noMoney") //change the instruction
      insufficientBalance = true //set the boolean
    }
  }

  def endTurn(): Unit = {
    hasRolled = false
    switchPlayer()
  }

  private def switchPlayer(): Unit = {
    resetStatus()
    if (currentPlayer == player1){
      currentPlayer = player2
    } else{
      currentPlayer = player1
      numRound += 1 //increase round number
      if (isEndGame()){
        endGame()
      }
    }
    if (currentPlayer.inJail){ //increase the jail turn
      if (currentPlayer.jailTurns < 4 ){
        currentPlayer.jailTurns += 1
      }
      updateStatus()
    }
  }

  def canBuyLot(): Boolean = {
    getCurrentLot() match { //if it is an Ownable lot and no owner
      case ownable: Ownable if ownable.owner == null => true
      case _ => false
    }
  }

  def getCurrentLot(): BoardElement = { //get the lot instance based on the position of current player
    board.boardElements(currentPlayer.position)
  }

  private def goToJail(): Unit = {
    currentPlayer.goToJail()
    hasMoved = false
  }

  private def outJail(): Unit = {
    currentPlayer.getOutOfJail()
  }

  def isDrawable(): Boolean = { //check if landed on Chance or Chest
    getCurrentLot().isInstanceOf[Chance] || getCurrentLot().isInstanceOf[Chest]
  }


  private def determineWinner(): Unit = { //determine the winner based on the assets value
    winner = if (player1.assets > player2.assets) player1 else player2
  }

  def passGo(): Unit = { //play passed the go
    currentPlayer.addBalance(200)
    currentPlayer.passedGo = false
    instruction = Instruction(MessageBox.passGo, "income")
  }

  def setBankrupt() = {
    bankrupt = currentPlayer
    winner = if (bankrupt == player1) player2 else player1 //set the opponent player as winner
    endGame()
  }

  def isEndGame(): Boolean = { //end game if reach round limit or one of the player bankrupt
    reachRoundLim() || bankrupt != null
  }

  private def reachRoundLim(): Boolean = {
    if (numRound > totRound){
      determineWinner()
      true
    } else{
      false
    }
  }

  def getRoundRec(): Int = { //get the round record
    if (numRound > totRound) totRound else numRound
  }

  def exit(): Unit = {
    instruction = Instruction("%s\n%s".format(MessageBox.exit(), MessageBox.confirm()), "exitColour" )
  }

  def endGame() = {
    if (bankrupt== null){
      instruction = Instruction("%s\n%s\n%s\n%s".format(MessageBox.endGame(), MessageBox.highestAssets(winner.name, winner.assets), MessageBox.won(winner.name), MessageBox.writeRecord()), "trophy")
    } else {
      instruction = Instruction("%s\n%s\n%s".format(MessageBox.bankrupt(bankrupt.name), MessageBox.won(winner.name), MessageBox.writeRecord()), "trophy")
    }
  }
}
