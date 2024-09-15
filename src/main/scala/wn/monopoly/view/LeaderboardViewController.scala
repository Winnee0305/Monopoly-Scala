package wn.monopoly.view

import javafx.event.ActionEvent
import scalafx.scene.control.{TableCell, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import wn.monopoly.MainApp
import wn.monopoly.model.GameRec
import wn.monopoly.util.{GameData, MultimediaHandler}
import scalafx.beans.property.StringProperty
import scalafx.scene.image.Image

import java.time.format.DateTimeFormatter

@sfxml
class LeaderboardViewController(private val leaderboard: TableView[GameRec],
                                private val noColumn: TableColumn[GameRec, String],
                                private val dateColumn: TableColumn[GameRec, String],
                                private val nameColumn: TableColumn[GameRec, String],
                                private val assetColumn: TableColumn[GameRec, String],
                                private val numRoundColumn: TableColumn[GameRec, String]){
  initialize()

  private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //date formatter to display the desired date format on leaderboard

  private def initialize(): Unit = {
    leaderboard.items = GameData.gameRecs //get the data of the table view from the gameRecs

    noColumn.cellValueFactory = { data => //set the data to the respective columns
      val rowIndex = leaderboard.getItems.indexOf(data.value) + 1
      StringProperty(rowIndex.toString)
    }

    nameColumn.cellValueFactory = {
      _.value.playerName
    }

    assetColumn.cellValueFactory = { data =>
      StringProperty(data.value.asset.value.toString)
    }

    numRoundColumn.cellValueFactory = { data =>
      StringProperty(data.value.numRound.value.toString)
    }

    dateColumn.cellValueFactory = { data =>
      StringProperty(data.value.date.value.format(dateFormatter))
    }
  }

  def handleBack(event: ActionEvent): Unit = {
    MainApp.showRootPage()
  }
}
