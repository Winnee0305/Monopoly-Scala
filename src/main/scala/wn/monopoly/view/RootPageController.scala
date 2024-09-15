package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
import scalafxml.core.macros.sfxml
import scalafx.scene.image.ImageView
import scalafx.scene.layout.VBox
import wn.monopoly.util.{AnimationCreator, MultimediaHandler, SceneHandler}

@sfxml
class RootPageController(private val backButton: Button,
                         private val content: VBox,
                         private val pageName: Label,
                         private val title: ImageView,
                         private val logo: ImageView,
                         private val musicSwitch: Button,
                         private val soundSwitch: Button,
                         private val musicSwitchIcon: ImageView,
                         private val soundSwitchIcon: ImageView){

  initialize

  private def initialize(): Unit = {
    setPageName("home")
    val titlePulsing = AnimationCreator.createPulsingEffect(title, 1000)
    titlePulsing.play()
    val bouncingEffect = AnimationCreator.createBouncingEffect(logo)
    bouncingEffect.play()

    setPageName("home")
    changeContentByFilePath("../view/RootView.fxml") //by default, change the content to RootView
    disableButton() //disable the back button
    MultimediaHandler.setHoverSound(backButton)
    MultimediaHandler.setHoverSound(musicSwitch)
    MultimediaHandler.setHoverSound(soundSwitch)
    MultimediaHandler.playBgMusic()
  }

  def handleBack(event: ActionEvent): Unit = { //method for back button is clicked
    MultimediaHandler.playButtonClickSound()
    setPageName("home")
    changeContentByFilePath("../view/RootView.fxml") //change the back to RootView
    disableButton()
  }

  def handleMusicSwitch(event: ActionEvent): Unit = {
    if (MultimediaHandler.playMusicStatus) {
      MultimediaHandler.setVolume(false)
      musicSwitchIcon.image = MultimediaHandler.createImage("musicOff.png")
    } else {
      MultimediaHandler.setVolume(true)
      musicSwitchIcon.image = MultimediaHandler.createImage("music.png")
    }
  }

  def handleSoundSwitch(event: ActionEvent): Unit = {
    if (MultimediaHandler.muted) {
      MultimediaHandler.setMute(false)
      soundSwitchIcon.image = MultimediaHandler.createImage("speaker.png")
    } else {
      MultimediaHandler.setMute(true)
      soundSwitchIcon.image = MultimediaHandler.createImage("speakerOff.png")
    }
  }

  //to show respective page by adding children to the content vbox from another fxml
  def showNewGame(): Unit = {
    setPageName("setup game")
    changeContentByFilePath("../view/SetupView.fxml")
    enableButton()
  }

  def showLeaderboard(): Unit = {
    setPageName("leaderboard")
    changeContentByFilePath("../view/LeaderboardView.fxml")
    enableButton()
  }

  def showRules(): Unit = {
    setPageName("rules")
    manageRulesAndCredits("rules")
    enableButton()
  }

  def showCredits(): Unit = {
    setPageName("credits")
    manageRulesAndCredits("credits")
    enableButton()
  }

  private def changeContentByFilePath(scene: String): Unit = { //change the content to the specified file
    val vbox = SceneHandler.getVboxByFilePath(scene)
    changeContent(vbox)
  }

  private def changeContent(vbox: VBox): Unit = {
    content.getChildren.clear() //clear the content first
    content.getChildren.add(vbox) //add the vbox loaded from fxml file
  }

  private def manageRulesAndCredits(name: String): Unit = { //because one controller (ScrollViewController need to handle 2 fxml file to display)
    val loader = SceneHandler.getLoader("../view/ScrollView.fxml")
    loader.load()
    val controller = loader.getController[ScrollViewController#Controller]
    name match { //display the specified fxml by calling the method in the controller class
      case "rules" => controller.displayRules()
      case "credits" => controller.displayCredits()
    }
    val vbox = controller.root //get the vbox
    changeContent(vbox) //change the content to this vbox
  }

  private def setPageName(name: String): Unit = {
    pageName.text = name.mkString("  ").toUpperCase
  }

  private def disableButton(): Unit = {
    backButton.visible = false
    backButton.disable = true
  }

  private def enableButton(): Unit = {
    backButton.visible = true
    backButton.disable = false
  }
}

