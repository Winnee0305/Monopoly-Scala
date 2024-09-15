package wn.monopoly

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.stage.{Modality, Stage, StageStyle}
import wn.monopoly.model.Player
import wn.monopoly.util.{Database, GameData, MultimediaHandler, SceneHandler}
import wn.monopoly.view.{CustomDialogController, RootPageController}
import scalafx.Includes._

object MainApp extends JFXApp {
  var rootPageController: RootPageController#Controller = _
  var customDialogController: CustomDialogController#Controller = _
  var setExitDialog: Boolean = false

  //to reset the database
//  Database.dropAllTables()
  Database.setupDB()

  stage = SceneHandler.initializeStage("Monopoly") //create a stage
  stage.icons.add(MultimediaHandler.createImage("logo.png")) //set icon
  showRootPage() //show the main page

  def showRootPage(): Unit = {
    val rootPageLoader = SceneHandler.getLoader("../view/RootPage.fxml")
    val scene = SceneHandler.loadSceneByLoader(rootPageLoader)
    this.rootPageController = rootPageLoader.getController[RootPageController#Controller] //set the controller
    stage.setScene(scene)
    stage.show()
  }

  def showGamePage(player1: Player, player2: Player, totRound: Int): Unit = {
    GameData.game.player1 = player1 //set the information to the Game model
    GameData.game.player2 = player2
    GameData.game.totRound = totRound
    val scene = SceneHandler.loadScene("../view/GamePage.fxml")
    stage.setScene(scene)
    stage.show()
  }

  def showCustomizePopUpMessage(message: String, imgName: String): Unit = {
    val loader = SceneHandler.getLoader("/wn/monopoly/view/CustomDialog.fxml") //get the fxml loader
    val root: VBox = loader.load[javafx.scene.layout.VBox]() //get the vbox
    customDialogController = loader.getController[CustomDialogController#Controller] //get the controller
    val dialogStage = new Stage()
    dialogStage.initModality(Modality.APPLICATION_MODAL)
    dialogStage.initOwner(MainApp.stage)
    dialogStage.initStyle(StageStyle.UNDECORATED)
    dialogStage.initStyle(StageStyle.TRANSPARENT)
    val scene = new Scene(root)
    scene.setFill(Color.TRANSPARENT)
    dialogStage.setScene(scene)
    customDialogController.setDialogStage(dialogStage)
    customDialogController.setContent(message, MultimediaHandler.createImage("%s.png".format(imgName)))
    if (setExitDialog){ //if need to set an exit dialog
      customDialogController.exitDialog() //call the method in controller class to set exit dialog (enable confirmation button)
    }
    dialogStage.showAndWait()
    setExitDialog = false //reset the status
  }
}
