package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import wn.monopoly.util.MultimediaHandler

@sfxml
class CustomDialogController(private val msgIcon: ImageView,
                             private val msgLabel: Label,
                             private val continueButton: Button,
                             private val backHomeButton: Button) { //a customize alert box
  private var dialogStage: Stage = _
  var isExit: Boolean = false //a boolean to determine is confirm to exit

  initialize()

  def initialize(): Unit = {
    MultimediaHandler.setHoverSound(continueButton) //set hover sound for the buttons
    MultimediaHandler.setHoverSound(backHomeButton)
    backHomeButton.disable = true //by default, the button is disabled
  }

  def setDialogStage(dialogStage: Stage): Unit = {
    this.dialogStage = dialogStage
  }

  def setContent(msg: String, icon: Image): Unit = {
    msgIcon.image = icon
    msgLabel.text = msg
  }

  def handleContinue(event: ActionEvent): Unit = { //if continue button is clicked
    MultimediaHandler.playButtonClickSound()
    closeStage()
  }

  def handleBackHome(event: ActionEvent): Unit = { //if the back home button is clicked
    MultimediaHandler.playButtonClickSound()
    isExit = true //update the boolean
    closeStage()
  }

  private def closeStage(): Unit = {
    if (dialogStage != null) {
      dialogStage.close()
    }
  }

  def exitDialog(): Unit = { //if it should be the exit dialog (to be called in the main app)
    backHomeButton.disable = false
  }
}