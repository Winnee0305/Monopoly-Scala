package wn.monopoly.util

import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Parent, Scene}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.scene.layout.{AnchorPane, VBox}

object SceneHandler {

  def getLoader(filePath: String): FXMLLoader = { //create fxml loader using the fxml file path
    val resource = getClass.getResource(filePath)
    new FXMLLoader(resource, NoDependencyResolver)
  }

  private def getParentPane(loader: FXMLLoader): Parent = { //get the parent pane
   loader.load[javafx.scene.Parent]()
  }

  private def loadSceneByRootPane(rootPane: Parent): Scene = { //create scene using the root pane
    new Scene(rootPane)
  }

  def loadSceneByLoader(loader: FXMLLoader): Scene = { //create scene using loader
    val rootPane = getParentPane(loader)
    loadSceneByRootPane(rootPane)
  }

  def loadScene(filePath: String): Scene = { //create scene using fxml file path
    val loader = getLoader(filePath)
    loadSceneByRootPane(getParentPane(loader))
  }

  def initializeStage(name: String): PrimaryStage = new PrimaryStage(){ //create a stage
    title = name
  }

  def getAnchorPaneAndLoader(filePath: String): (AnchorPane, FXMLLoader) = {
    val loader = getLoader(filePath)
    val root = loader.load[javafx.scene.layout.AnchorPane]()
    (root, loader)
  }

  def getVboxByFilePath(filePath: String): VBox = {
    val loader = getLoader(filePath)
    loader.load[javafx.scene.layout.VBox]()
  }
}