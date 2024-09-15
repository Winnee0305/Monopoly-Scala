package wn.monopoly.view

import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.VBox
import scalafxml.core.macros.sfxml
import wn.monopoly.util.SceneHandler

@sfxml
class ScrollViewController(val root: VBox,
                           private val container: ScrollPane){

  def displayRules(): Unit ={ //display rules page content in the scroll pane
    displayPage("../view/Rules.fxml")
  }

  def displayCredits(): Unit = { //display credits page content in the scroll pane
    displayPage("../view/Credits.fxml")
  }

  private def displayPage(fxmlFilePath: String): Unit = { //replace the content to the scroll pane
    val contents: VBox = SceneHandler.getVboxByFilePath(fxmlFilePath)
    container.content = contents
    container.vbarPolicy = ScrollPane.ScrollBarPolicy.Never
    container.hbarPolicy = ScrollPane.ScrollBarPolicy.Never
  }
}