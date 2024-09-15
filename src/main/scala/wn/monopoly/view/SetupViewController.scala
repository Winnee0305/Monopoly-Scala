package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.scene.control.{Button, RadioButton, TextField, ToggleGroup}
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import wn.monopoly.model.PreGame
import wn.monopoly.MainApp
import scalafx.Includes._
import scalafx.application.Platform
import wn.monopoly.util.MultimediaHandler

@sfxml
class SetupViewController(private val player1Name: TextField,
                          private val player1Img: ImageView,
                          private val player1TokenOpt1: RadioButton,
                          private val player1TokenOpt2: RadioButton,
                          private val player1TokenOpt3: RadioButton,
                          private val player1TokenOpt4: RadioButton,
                          private val player1TokenOpt5: RadioButton,
                          private val player2Name: TextField,
                          private val player2Img: ImageView,
                          private val player2TokenOpt1: RadioButton,
                          private val player2TokenOpt2: RadioButton,
                          private val player2TokenOpt3: RadioButton,
                          private val player2TokenOpt4: RadioButton,
                          private val player2TokenOpt5: RadioButton,
                          private val round30: RadioButton,
                          private val round60: RadioButton,
                          private val round90: RadioButton,
                          private val round120: RadioButton,
                          private val startButton: Button){

  private val player1TokenGroup = new ToggleGroup() //create toggle groups
  private val player2TokenGroup = new ToggleGroup()
  private val roundGroup = new ToggleGroup()

  private val preGame = new PreGame() //instantiate a PreGame to store the pre-game information
  private val tokenOpts: List[Image] = List(
    MultimediaHandler.createImage("token1.PNG"),
    MultimediaHandler.createImage("token2.PNG"),
    MultimediaHandler.createImage("token3.PNG"),
    MultimediaHandler.createImage("token4.PNG"),
    MultimediaHandler.createImage("token5.PNG"))
  private var player1Tokens: List[RadioButton] = _
  private var player2Tokens: List[RadioButton] = _
  private var roundOpts: List[RadioButton] = _

  initialize()

  private def initialize(): Unit = {

    player1Tokens = List(player1TokenOpt1, player1TokenOpt2, player1TokenOpt3, player1TokenOpt4, player1TokenOpt5) //assign the radio buttons of 1 player to a list
    player2Tokens = List(player2TokenOpt1, player2TokenOpt2, player2TokenOpt3, player2TokenOpt4, player2TokenOpt5)
    roundOpts = List(round30, round60, round90, round120) //assign the radio buttons of round options

    setTokenRadioButton(player1Tokens, player1TokenGroup)
    setTokenRadioButton(player2Tokens, player2TokenGroup)
    MultimediaHandler.setHoverSound(startButton)

    roundOpts.foreach { opt => {
      opt.toggleGroup = roundGroup
      opt.styleClass.clear()
      opt.styleClass.add("round-label")}
    }
  }

  private def setTokenRadioButton(buttons: List[RadioButton], toggleGroup: ToggleGroup): Unit = { //set the radio buttons to a same toggle group (so that only one radio button will be selected)
    buttons.zip(tokenOpts).foreach { case (radioButton, image) =>
      radioButton.toggleGroup = toggleGroup
      radioButton.getGraphic.asInstanceOf[javafx.scene.image.ImageView].image = image //add the graphic to the radio button (token representation)
    }
  }

  def handlePlayer1Token(): Unit = { //method of the radio button is clicked
    MultimediaHandler.playButtonClickSound()
    preGame.player1.img = getImageFromRadioButton(player1TokenGroup) //update the image (to the selected token representation in radio button)
    updateImg(player1Img, preGame.player1.img)
  }

  def handlePlayer2Token(): Unit = {
    MultimediaHandler.playButtonClickSound()
    preGame.player2.img = getImageFromRadioButton(player2TokenGroup)
    updateImg(player2Img, preGame.player2.img)
  }

  def handleRound(): Unit = { //method of selecting round options
    MultimediaHandler.playButtonClickSound()
    roundOpts.foreach {opt =>
      opt.styleClass.remove("selected-round")
    }
    val selectedRadioButton = roundGroup.selectedToggle.get().asInstanceOf[javafx.scene.control.RadioButton]
    selectedRadioButton.styleClass.add("selected-round") //style the selected option
  }

  def handleStartGame(event: ActionEvent): Unit = { //method of start game button is clicked
    MultimediaHandler.playButtonClickSound()
    preGame.player1.name = player1Name.text.value //get the input text and set
    preGame.player2.name = player2Name.text.value
    try{
      if (preGame.validateName() && preGame.validateToken()){ //call validation logic from preGame
        if (roundGroup.getSelectedToggle != null ){
          val numRound: Int = roundGroup.getSelectedToggle match{ //match number of round integer based on button selected
            case `round30` => 30
            case `round60` => 60
            case `round90` => 90
            case `round120` => 120
          }
          MainApp.showGamePage(preGame.player1, preGame.player2, numRound) //passed the settings to main app to show game page
        } else{
          Platform.runLater(() => MainApp.showCustomizePopUpMessage("Please select number of rounds!", "alert"))
        }
      }
    }catch{
      case e: Exception => Platform.runLater(() => MainApp.showCustomizePopUpMessage(e.getMessage, "alert"))
    }
  }

  private def updateImg(imgView: ImageView, img: Image): Unit = { //update image view with the image given
    imgView.image = img
  }

  private def getImageFromRadioButton(toggleGroup: ToggleGroup): Image = { //retrieve image from the radio button
    val radioButton = toggleGroup.selectedToggle.value.asInstanceOf[javafx.scene.control.RadioButton]
    val imageView = radioButton.getGraphic.asInstanceOf[javafx.scene.image.ImageView]
    imageView.image()
  }
}

