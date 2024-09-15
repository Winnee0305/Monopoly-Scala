package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.layout.VBox
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import wn.monopoly.util.{MultimediaHandler, SceneHandler}


@sfxml
class RulesViewController(private val container: ScrollPane,
                          private val backButton: Button){

  private var dialogStage: Stage = _

  initialize()

  def initialize(): Unit = { //replace the scroll pane content from a fxml file
    val rules: VBox = SceneHandler.getVboxByFilePath("../view/Rules.fxml")
    container.content = rules
    container.vbarPolicy = ScrollPane.ScrollBarPolicy.Never //modify the sty;e
    container.hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    MultimediaHandler.setHoverSound(backButton)
  }

  def setDialogStage(dialogStage: Stage): Unit = {
    this.dialogStage = dialogStage
  }

  def handleBack(event: ActionEvent): Unit = {
    MultimediaHandler.playButtonClickSound()
    dialogStage.close()
  }
}