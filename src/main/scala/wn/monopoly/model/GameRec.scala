package wn.monopoly.model

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}
import wn.monopoly.util.Database
import java.time.{LocalDateTime}
import scala.util.Try

class GameRec(val playerNameS: String,
              val assetI: Int,
              val numRoundI: Int,
              val dateD: LocalDateTime){
  val playerName = new StringProperty(playerNameS)
  val asset = IntegerProperty(assetI)
  val numRound = IntegerProperty(numRoundI)
  val date = ObjectProperty[LocalDateTime](dateD)

  def save() : Try[Int] = { //save the record to database
    Try(DB autoCommit { implicit session =>
      sql"""
        insert into gameRec (playerName, asset, numRound, date) values
          (${playerName.value}, ${asset.value}, ${numRound.value}, ${date.value.toString})
      """.update.apply()
    })
  }
}

object GameRec extends Database{
  def apply (
              playerName : String,
              asset : Int,
              numRound: Int,
              date: LocalDateTime
            ) : GameRec = {
    new GameRec(playerName, asset, numRound, date)
  }

  def initializeTable() = { //create table
    DB autoCommit { implicit session =>
      sql"""
			create table gameRec (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
			  playerName varchar(64),
			  asset int,
        numRound int,
			  date varchar(64)
			)
			""".execute.apply()
    }
  }



  def getAllRecords : List[GameRec] = { //retrieve all game record from the database
    DB readOnly { implicit session =>
      sql"select * from gameRec".map(rs =>
        GameRec(
          rs.string("playerName"),
          rs.int("asset"),
          rs.int("numRound"),
          LocalDateTime.parse(rs.string("date")))).list.apply()
    }
  }
}
