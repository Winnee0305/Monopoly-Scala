package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, Tooltip}
import scalafxml.core.macros.sfxml
import scalafx.scene.image.ImageView
import scalafx.scene.shape.Rectangle
import wn.monopoly.model.{Chance, Dice, Player, Property, Railroad, Utility}
import wn.monopoly.util.{AnimationCreator, GameData, MultimediaHandler, SceneHandler}
import wn.monopoly.MainApp
import scalafx.Includes._
import scalafx.animation.SequentialTransition
import scalafx.application.Platform
import scalafx.geometry.Point2D
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.stage.{Modality, Stage, StageStyle}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

@sfxml
class GamePageController(private val musicSwitch: Button,
                         private val soundSwitch: Button,
                         private val rulesButton: Button,
                         private val exitButton: Button,
                         private val musicSwitchIcon: ImageView,
                         private val soundSwitchIcon: ImageView,
                         private val chanceButton: Button,
                         private val chestButton: Button,
                         private val player1Img: ImageView,
                         private val player1Name: Label,
                         private val player1Position: Label,
                         private val player1Balance: Label,
                         private val player1Asset: Label,
                         private val player2Img: ImageView,
                         private val player2Name: Label,
                         private val player2Position: Label,
                         private val player2Balance: Label,
                         private val player2Asset: Label,
                         private val rollDiceButton: Button,
                         private val buyPropertyButton: Button,
                         private val payButton: Button,
                         private val endTurnButton: Button,
                         private val player1Token: ImageView,
                         private val player2Token: ImageView,
                         private val diceUI1: ImageView,
                         private val diceUI2: ImageView,
                         private val player1Indicator: Rectangle,
                         private val player2Indicator: Rectangle,
                         private val roundNum: Label,
                         private val totRound: Label,
                         private val pos0: Label, //labels on each lot (to show tooltip)
                         private val pos1: Label,
                         private val pos2: Label,
                         private val pos3: Label,
                         private val pos4: Label,
                         private val pos5: Label,
                         private val pos6: Label,
                         private val pos7: Label,
                         private val pos8: Label,
                         private val pos9: Label,
                         private val pos10: Label,
                         private val pos11: Label,
                         private val pos12: Label,
                         private val pos13: Label,
                         private val pos14: Label,
                         private val pos15: Label,
                         private val pos16: Label,
                         private val pos17: Label,
                         private val pos18: Label,
                         private val pos19: Label,
                         private val pos20: Label,
                         private val pos21: Label,
                         private val pos22: Label,
                         private val pos23: Label,
                         private val pos24: Label,
                         private val pos25: Label,
                         private val pos26: Label,
                         private val pos27: Label,
                         private val pos28: Label,
                         private val pos29: Label,
                         private val pos30: Label,
                         private val pos31: Label,
                         private val pos32: Label,
                         private val pos33: Label,
                         private val pos34: Label,
                         private val pos35: Label,
                         private val pos36: Label,
                         private val pos37: Label,
                         private val pos38: Label,
                         private val pos39: Label,
                         private val paymentLabel: Label,
                         private val paymentAmount: Label) {

  //to create tooltip on the lot such as property, railroad and utility
  private val positionLabels: Array[Label] = Array(pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20, pos21, pos22, pos23, pos24, pos25, pos26, pos27, pos28, pos29, pos30, pos31, pos32, pos33, pos34, pos35, pos36, pos37, pos38, pos39)
  private val rollDiceButtonPulsing = AnimationCreator.createPulsingEffect(rollDiceButton, 100)

  initialize()

  private def initialize(): Unit = { //link the element to the model class
    GameData.game.dice1 = new Dice(diceUI1)
    GameData.game.dice2 = new Dice(diceUI2)
    GameData.game.player1.token = player1Token
    GameData.game.player2.token = player2Token
    player1Token.image = GameData.game.player1.img
    player2Token.image = GameData.game.player2.img
    player1Img.image = GameData.game.player1.img
    player2Img.image = GameData.game.player2.img
    GameData.game.player1.indicator = player1Indicator
    GameData.game.player2.indicator = player2Indicator
    GameData.game.player1.initialize()
    GameData.game.player2.initialize()
    totRound.text = " / %d".format(GameData.game.totRound) //display round number

    GameData.game.initializeGame("classicMap")
    updatePlayersInformationUI()
    updateButtonStatus(true)
    activateIndicator(GameData.game.player1) //activate player 1 indicator (show they have control)

    positionLabels.zipWithIndex.foreach { case (label, index) => //create tool tip on each pos label according to their index in the boardElement
      createTooltip(index)
      label.getStyleClass.add("pointer-cursor")
    }
    MultimediaHandler.setHoverSound(buyPropertyButton) //set buttons sound effects
    MultimediaHandler.setHoverSound(payButton)
    MultimediaHandler.setHoverSound(endTurnButton)
    MultimediaHandler.setHoverSound(rollDiceButton)
    MultimediaHandler.setHoverSound(musicSwitch)
    MultimediaHandler.setHoverSound(soundSwitch)
    MultimediaHandler.setHoverSound(rulesButton)
    MultimediaHandler.setHoverSound(exitButton)
    MultimediaHandler.setHoverSound(chanceButton)
    MultimediaHandler.setHoverSound(chestButton)
    MultimediaHandler.playBgMusic() //play bg music

    startGame()
  }

  private def startGame(): Unit = {
    GameData.game.startGame()
    Platform.runLater(() => showPopUpMessage())
  }

  def handleMusicSwitch(event: ActionEvent): Unit = {
    if (MultimediaHandler.playMusicStatus) { //currently is playing
      MultimediaHandler.setVolume(false)
      musicSwitchIcon.image = MultimediaHandler.createImage("musicOff.png")
    } else {
      MultimediaHandler.setVolume(true)
      musicSwitchIcon.image = MultimediaHandler.createImage("music.png")
    }
  }

  def handleSoundSwitch(event: ActionEvent): Unit = {
    if (MultimediaHandler.muted) { //currently is muted
      MultimediaHandler.setMute(false)
      soundSwitchIcon.image = MultimediaHandler.createImage("speaker.png")
    } else {
      MultimediaHandler.setMute(true)
      soundSwitchIcon.image = MultimediaHandler.createImage("speakerOff.png")
    }
  }

  def handleRules(event: ActionEvent): Unit = { //show the rules pop up window
    val loader = new FXMLLoader(getClass.getResource("../view/RulesView.fxml"), NoDependencyResolver)
    val root: VBox = loader.load[javafx.scene.layout.VBox]()
    val controller = loader.getController[RulesViewController#Controller]
    val dialogStage = new Stage()
    dialogStage.initModality(Modality.APPLICATION_MODAL)
    dialogStage.initOwner(MainApp.stage)
    dialogStage.initStyle(StageStyle.UNDECORATED) //remove the stage style
    dialogStage.initStyle(StageStyle.TRANSPARENT)
    val scene = new Scene(root)
    scene.setFill(Color.TRANSPARENT) //make transparent
    dialogStage.setScene(scene)
    controller.setDialogStage(dialogStage)
    dialogStage.showAndWait() //wait for the dialog
  }

  def handleExit(event: ActionEvent): Unit = { //method of exit button is clicked
    GameData.game.exit() //call the exit logic in the game class
    Platform.runLater(() => {
      MainApp.setExitDialog = true //a boolean to bass to the customize dialog controller so that the confirmation button is enable
      showPopUpMessage()
      if (MainApp.customDialogController.isExit){ //is the player confirm to exit
        GameData.resetGameData() //reset the game data
        MainApp.showRootPage() //back to root page
      }
    })
  }

  def handleRollDice(event: ActionEvent): Unit = { //method of roll dice button is clicked
    MultimediaHandler.playButtonClickSound()
    MultimediaHandler.playRollDiceSound()
    GameData.game.rollDice() //call the roll dice logic in the game class
    disableButton(rollDiceButton) //after dice rolled, disable the button
    val dicesAnimation = AnimationCreator.createDiceShufflingAnimation(GameData.game.dice1, GameData.game.dice2)
    dicesAnimation.play() //play the dice shuffling animation
    dicesAnimation.onFinished = _ => { //after shuffling animation finished, show the image of the dice value (randomly pick by system)
      GameData.game.dice1.imageView.image = GameData.game.dice1.diceImages(GameData.game.dice1.value - 1)
      GameData.game.dice2.imageView.image = GameData.game.dice2.diceImages(GameData.game.dice2.value - 1)
      handleTokenMove() //after dice rolled, move the player's token
    }
  }

  private def handleTokenMove(): Unit = { //method of move the player's token
    if (!GameData.game.hasMoved){ //if the current turn hasn't move
      val moveToken = createMoveTokenUI() //create the token ui (calling the method create token move transition)
      MultimediaHandler.playMoveTokenSound()
      moveToken.play()
      moveToken.onFinished = _ => { //after transition ended
        MultimediaHandler.playMoveTokenSound()
        Platform.runLater(() => { //ensure the animation is finished, that only continue execution
          if (!GameData.game.currentPlayer.inJail){ //if player not in jail
            handleTokenMoveFinished()
          }
        })
      }
    } else{
      Platform.runLater(() => handleTokenMoveFinished())
    }
  }


  private def handleTokenMoveFinished(): Unit = { //logic after the token moved
    if (GameData.game.currentPlayer.passedGo) { //if the player had passed the go
      GameData.game.passGo() //call the logic in Game model
      showPopUpMessage()
    }
    GameData.game.updateStatus() //update the status from Game model (based on lot landed)
    updatePayment()
    updatePlayersInformationUI()
    updateButtonStatus(false)
    Platform.runLater(() => {
      showPopUpMessage() //to show the current game state
      if (!GameData.game.hasMoved) { //(to handle situation when player lands on chance/chest, the card drawn will ask to go to other lot)
        handleTokenMove() //call again the move token logic
        GameData.game.hasMoved = true //set the boolean
      }
    })
  }

  def handleBuyProperty(event: ActionEvent): Unit = { //method of buy property
    MultimediaHandler.playButtonClickSound()
    GameData.game.buyProperty() //call the buy property logic in Game model
    if (GameData.game.hasBrought) { //check the boolean (successfully bought)
      MultimediaHandler.playPaySuccessSound()
      createTooltip(GameData.game.currentPlayer.position) //update the tooltip(owner)
    } else { //failed to buy the property
      MultimediaHandler.playPayFailSound()
    }
    showPopUpMessage()
    updateButtonStatus(false)
    updatePlayersInformationUI()
  }

  def handlePay(event: ActionEvent): Unit = { //method of pay
    MultimediaHandler.playButtonClickSound()
    GameData.game.payPayment() //call the pay payment logic in Game model
    if (GameData.game.insufficientBalance){ //if insufficient balance to pay
      MultimediaHandler.playPayFailSound()
      Platform.runLater(()=>{
        showPopUpMessage()
        GameData.game.setBankrupt() //cannot pay payment -> bankrupt
        endGame() //cannot continue game, game ended
      })
    } else{
      MultimediaHandler.playPaySuccessSound()
    }
    updatePayment()
    updateButtonStatus(false)
    updatePlayersInformationUI()
  }

  def handleEndTurn(event: ActionEvent): Unit = { //method of end turn button clicked
    MultimediaHandler.playButtonClickSound()
    deactivateIndicator(GameData.game.currentPlayer)
    GameData.game.endTurn()
    if (GameData.game.isEndGame()){ //check if the game ended
      endGame()
    } else{
      activateIndicator(GameData.game.currentPlayer) //switch the control to another player
      updateButtonStatus(true)
      if (GameData.game.instruction != null){
        Platform.runLater(() => showPopUpMessage())
      }
    }
  }

  def handleChest(event: ActionEvent): Unit = { //method of draw a card from chest
    MultimediaHandler.playDrawCardSound()
    GameData.game.drawCard(GameData.game.chestDeck) //draw a card
    updateAfterDrawCard()
  }

  def handleChance(event: ActionEvent): Unit = { //method of draw a card from chance
    MultimediaHandler.playDrawCardSound()
    GameData.game.drawCard(GameData.game.chanceDeck) //draw a card
    updateAfterDrawCard()
  }

  private def updatePayment(): Unit = { //update the payment display
    if (GameData.game.payment != null){
      paymentLabel.text = GameData.game.payment.desc
      paymentAmount.text = "$%s".format(GameData.game.payment.amount.toString)
    } else{
      paymentLabel.text = "-"
      paymentAmount.text = "-"
    }
  }

  private def updateAfterDrawCard(): Unit = { //method after a card is drawn
    showPopUpMessage() //to show the card drawn
    updateButtonStatus(false)
    updatePlayersInformationUI()
    updatePayment()
    if(!GameData.game.hasMoved){ //to move the player's token
      val moveToken = createMoveTokenUI()
      MultimediaHandler.playMoveTokenSound()
      Platform.runLater(() => {
        moveToken.play()
        moveToken.onFinished = _ => {
          MultimediaHandler.playMoveTokenSound()
          handleTokenMoveFinished()
        }
      })
    }
  }

  private def activateIndicator(player: Player): Unit = { //animate the flash effect on the player information box (indicate this player has control)
    roundNum.text = GameData.game.numRound.toString
    player.indicatorAnimation.play
    player.indicator.visible = true
  }

  private def deactivateIndicator(player: Player): Unit = { //stop the flash effect
    player.indicatorAnimation.stop
    player.indicator.visible = false
  }

  private def updatePlayersInformationUI(): Unit = {
    player1Name.text = GameData.game.player1.name
    player1Position.text = "%s".format(GameData.game.player1.position.toString)
    player1Balance.text = "$%s".format(GameData.game.player1.balance.toString)
    player1Asset.text = "$%s".format(GameData.game.player1.assets.toString)

    player2Name.text = GameData.game.player2.name
    player2Position.text = "%s".format(GameData.game.player2.position.toString)
    player2Balance.text = "$%s".format(GameData.game.player2.balance.toString)
    player2Asset.text = "$%s".format(GameData.game.player2.assets.toString)
  }

  private def createMoveTokenUI(): SequentialTransition = {
    val player = GameData.game.currentPlayer
    val sequentialTransition = new SequentialTransition()
    val startCoordinate = GameData.game.board.boardCoordinates(player.previousPosition)._1
    val endCoordinate = GameData.game.board.boardCoordinates(player.position)._1
    var totalDuration = 800

    if (!player.inJail) { //should move around the board clockwise to reach the destination
      val paths: List[Point2D] = GameData.game.board.getPath(player.previousPosition, player.position)
      val numPath = paths.length
      if (numPath > 12){ //if the distance is longer, the total animation duration also longer
        totalDuration = 1600
      }
      val transitionDuration = totalDuration / numPath
      for (i <- 0 until numPath - 1) {
        sequentialTransition.children.add(AnimationCreator.createTokenTransition(player.token, paths(i), paths(i + 1), transitionDuration))
      }
    } else { //directly go to the jail
      sequentialTransition.children.add(AnimationCreator.createTokenTransition(player.token, startCoordinate, endCoordinate, totalDuration))
    }
    sequentialTransition
  }

  private def updateButtonStatus(status: Boolean): Unit = {
    if (status) { //to be used when a new turn started
      disableButton(chanceButton)
      disableButton(chestButton)
      disableButton(payButton)
      disableButton(buyPropertyButton)
      disableButton(endTurnButton)
      enableButton(rollDiceButton)
    } else { //check the status accordingly to determine which button which be enabled
      if (GameData.game.hasRolled) {
        disableButton(rollDiceButton)
      } else {
        enableButton(rollDiceButton)
      }
      if (GameData.game.payment != null) { //there is payment to be paid
        enableButton(payButton)
        disableButton(rollDiceButton)
      } else {
        disableButton(payButton)
      }
      if (GameData.game.canBuyLot()) {
        enableButton(buyPropertyButton)
        disableButton(payButton)
      } else {
        disableButton(buyPropertyButton)
      }
      if (GameData.game.isDrawable() && GameData.game.cardDrawn == null){   //if landed on drawable lot and not yet draw card
        if (GameData.game.getCurrentLot().isInstanceOf[Chance]){ //check which (chance / chest) card to draw
          enableButton(chanceButton)
        } else{
          enableButton(chestButton)
        }
      } else{
        disableButton(chanceButton)
        disableButton(chestButton)
      }
      if (GameData.game.payment == null && (!GameData.game.isDrawable() || GameData.game.cardDrawn != null)) { //able to end turn only if no payment, card drawn if landed on drawable lot
        enableButton(endTurnButton)
      } else {
        disableButton(endTurnButton)
      }
    }
  }

  private def enableButton(button: Button): Unit = {
    if (button == rollDiceButton){
      rollDiceButtonPulsing.play()
    }
    button.disable = false
  }

  private def disableButton(button: Button): Unit = {
    if (button == rollDiceButton){
      rollDiceButtonPulsing.stop()
    }
    button.disable = true
  }


  private def showPopUpMessage(): Unit = { //showing customize pop up dialog if the instruction is not null
    if (GameData.game.instruction != null) {
      MainApp.showCustomizePopUpMessage(GameData.game.instruction.desc, GameData.game.instruction.insType)
    }
  }

  private def createTooltip(position: Int): Unit = { //to create tooltip and respective pos label
    val lot = GameData.game.board.boardElements(position)
    val label: Label = positionLabels(position)
    lot match {
      case _: Property =>
        val tooltip = new Tooltip()
        val (card, loader) = SceneHandler.getAnchorPaneAndLoader("../view/PropertyCard.fxml")
        val controller = loader.getController[PropertyCardController#Controller]
        controller.setInfo(position)
        tooltip.setGraphic(card)
        label.setTooltip(tooltip)
      case _: Utility =>
        val tooltip = new Tooltip()
        val (card, loader) = SceneHandler.getAnchorPaneAndLoader("../view/UtilityCard.fxml")
        val controller = loader.getController[UtilityCardController#Controller]
        controller.setInfo(position)
        tooltip.setGraphic(card)
        label.setTooltip(tooltip)
      case _: Railroad =>
        val tooltip = new Tooltip()
        val (card, loader) = SceneHandler.getAnchorPaneAndLoader("../view/RailroadCard.fxml")
        val controller = loader.getController[RailroadCardController#Controller]
        controller.setInfo(position)
        tooltip.setGraphic(card)
        label.setTooltip(tooltip)
      case _: Railroad =>
        val tooltip = new Tooltip()
        val (card, loader) = SceneHandler.getAnchorPaneAndLoader("../view/PropertyCard.fxml")
        val controller = loader.getController[PropertyCardController#Controller]
        controller.setInfo(position)
        tooltip.setGraphic(card)
        label.setTooltip(tooltip)
      case _ => None
    }
  }

  private def endGame(): Unit = { //end game logic
    Platform.runLater(() => {
      showPopUpMessage() 
      GameData.saveGame()
      GameData.resetGameData()
      MainApp.showRootPage()
    })
  }
}