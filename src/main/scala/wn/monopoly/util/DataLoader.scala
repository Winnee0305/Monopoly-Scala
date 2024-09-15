package wn.monopoly.util

import java.io.FileReader
import com.opencsv.CSVReader
import scalafx.geometry.Point2D
import collection.JavaConverters._

object DataLoader {
  private def loadCSV(fileName: String): List[Array[String]] = { //load all data in the csv file into a string array
    val resource = getClass.getResource("/data/%s.csv".format(fileName))
    var data: List[Array[String]] = List()
    try {
      val reader = new CSVReader(new FileReader(resource.getPath))
      data = reader.readAll().asScala.toList
      reader.close()
    }
    catch {
      case e: Exception => print("Error: " + e.getMessage)
    }
    data
  }

  def loadMap(map: String): List[(Point2D, String, String)] = {
    val fullData = loadCSV(map) //load data from the file received
    val headers = fullData.head.map(_.trim) //trim the header
    val data = fullData.tail //keep only the data (excluded the header)
    val headerMap = headers.zipWithIndex.toMap //map the header with index

    data.map { row =>
      val x = row(headerMap("x")).toDouble //get the index if the header is "x", assign to variable x
      val y = row(headerMap("y")).toDouble
      val name = row(headerMap("name"))
      val eleType = row(headerMap("type"))
      (new Point2D(x, y), name, eleType) //create tuple of Point2D, String, String
    }
  }

  def loadAttributes(eleType: String, name: String): Map[String, String] = { //load attributes based on desired element type and element name
    val fullData = loadCSV(eleType) //load all the data of the this element type
    val headers = fullData.head.map(_.trim) //trim the header
    val rows = fullData.tail //keep only the data (excluded header)

    rows.find(row => row(headers.indexOf("name")) == name) match { //find the rows that the data in the column "name" is equals to name
      case Some(row) => headers.zip(row).toMap
      case None => Map()
    }
  }
}