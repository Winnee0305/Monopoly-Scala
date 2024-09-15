package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.application.Platform
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import wn.monopoly.MainApp
import wn.monopoly.util.MultimediaHandler

@sfxml
class RootViewController(private val newGameButton: Button,
                         private val leaderboardButton: Button,
                         private val rulesButton: Button,
                         private val creditsButton: Button,
                         private val exitButton: Button){

  initialize()

  private def initialize(): Unit = {
    MultimediaHandler.setHoverSound(newGameButton)
    MultimediaHandler.setHoverSound(leaderboardButton)
    MultimediaHandler.setHoverSound(rulesButton)
    MultimediaHandler.setHoverSound(creditsButton)
    MultimediaHandler.setHoverSound(exitButton)
  }

  //methods to handle each page accordingly if respective button is clicked
  def handleNewGame(event: ActionEvent): Unit = {
    MultimediaHandler.playButtonClickSound()
    MainApp.rootPageController.showNewGame()
  }

  def handleLeaderboard(event: ActionEvent): Unit = {
    MultimediaHandler.playButtonClickSound()
    MainApp.rootPageController.showLeaderboard()
  }

  def handleRules(event: ActionEvent): Unit = {
    MultimediaHandler.playButtonClickSound()
    MainApp.rootPageController.showRules()
  }

  def handleCredit(event: ActionEvent): Unit = {
    MultimediaHandler.playButtonClickSound()
    MainApp.rootPageController.showCredits()
  }

  def handleExit(event: ActionEvent): Unit = {
    MultimediaHandler.playButtonClickSound()
    Platform.exit()
  }
}

