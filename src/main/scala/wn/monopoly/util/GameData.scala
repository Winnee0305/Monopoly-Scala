package wn.monopoly.util

import scalafx.collections.ObservableBuffer
import wn.monopoly.model.{Game, GameRec}
import java.time.LocalDateTime

object GameData{
  var game: Game = new Game() //store the Game in an object so that can be accessed anytime
  val gameRecs: ObservableBuffer[GameRec] = ObservableBuffer[GameRec]() //store the game records

  updateGameRecs()

  private def updateGameRecs(): Unit = {
    gameRecs.clear() //clear current records
    gameRecs ++= GameRec.getAllRecords //retrieve again records from the database
    sortGameRec()
  }

  private def sortGameRec(): Unit = {
    val sortedRec = gameRecs.sortBy(-_.asset.value) //sort assets ascending
    gameRecs.clear()
    gameRecs ++= sortedRec
  }

  def saveGame(): Unit = {
    val rec = GameRec(game.winner.name, game.winner.assets, game.getRoundRec(), LocalDateTime.now()) //create a game record instance
    rec.save() //save the record to database
    updateGameRecs() //after save, update the list of record in the program
  }

  def resetGameData(): Unit = {
    game = new Game()
  }
}