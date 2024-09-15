package wn.monopoly.util

import scalikejdbc._
import wn.monopoly.model.GameRec

trait Database{
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true";
  Class.forName(derbyDriverClassname)

  //ad-hoc session provider on the URL
  ConnectionPool.singleton(dbURL, "me", "mine")
  implicit val session = AutoSession
}

object Database extends Database {
  def setupDB() = {
    if (!hasDBInitialize)
      GameRec.initializeTable()

    def hasDBInitialize: Boolean = {
      DB getTable "GameRec" match {
        case Some(x) => true
        case None => false
      }
    }
  }

  def dropAllTables(): Unit = {
    DB.autoCommit { implicit session =>
      sql"DROP TABLE gameRec".execute.apply()
    }
  }
}