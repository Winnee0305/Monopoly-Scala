package wn.monopoly.model

import scalafx.geometry.Point2D
import wn.monopoly.util.DataLoader

class Board() {
  var boardCoordinates: List[(Point2D, String, String)] = _  //declare a list to store the coordinates of each element (coordinate, lot name, lot type)
  var boardElements: List[BoardElement] = _ //BoardElements data type of list allows to store elements in different data type (inheritance from BoardElements)
  private var boardCorner: List[Point2D] = List()

  def initializeMap(mapName: String): Unit = { //receive map name to load
    this.boardCoordinates = DataLoader.loadMap(mapName) //load the map information from csv file
    this.boardElements = boardCoordinates.map{ //convert each map item from String to their respective data type
      case (coordinate, name, eleType) =>
        eleType match{ //based on the String eleType, identify which data type to create
          case "base" => Base(coordinate, name)
          case "property" => createProperty(coordinate, name)
          case "chest" => Chest(coordinate, name)
          case "tax" => createTax(coordinate, name)
          case "railroad" => createRailroad(coordinate, name)
          case "chance" => Chance(coordinate, name)
          case "jail" => Jail(coordinate, name)
          case "prejail" => PreJail(coordinate, name)
          case "utility" => createUtility(coordinate, name)
          case "freeparking" => FreeParking(coordinate, name)
        }
    }
    val spacesBetweenCorners = boardCoordinates.length / 4 //Identify the corner lot
    for (i <- 0 until 4) {
      this.boardCorner = boardCorner :+ boardCoordinates(i * spacesBetweenCorners)._1 //add to the corner list
    }
  }

  private def createProperty(coordinate: Point2D, name: String): Property ={
    val propertyMap = DataLoader.loadAttributes("property", name) //get the information of a property by passing the name
    Property( //create an Property instance
      coordinate,
      name,
      propertyMap("group").toInt,
      propertyMap("price").toInt,
      propertyMap("price_per_house").toInt,
      propertyMap("rent").toInt,
      propertyMap("rent_set").toInt,
      propertyMap("rent_1h").toInt,
      propertyMap("rent_2h").toInt,
      propertyMap("rent_3h").toInt,
      propertyMap("rent_4h").toInt,
      propertyMap("rent_hotel").toInt,
      propertyMap("mortgage").toInt)
  }

  private def createRailroad(coordinate: Point2D, name: String): Railroad ={
    val railroadMap = DataLoader.loadAttributes("railroad", name) //get the information of a railroad by passing the name
    Railroad( //create a railroad instance
      coordinate,
      name,
      railroadMap("price").toInt,
      railroadMap("rent_1").toInt,
      railroadMap("rent_2").toInt,
      railroadMap("rent_3").toInt,
      railroadMap("rent_4").toInt,
      railroadMap("mortgage").toInt
    )
  }

  private def createTax(coordinate: Point2D, name: String): Tax = {
    val taxMap = DataLoader.loadAttributes("tax", name) //get the information of tax by passing the tax name
    Tax( //create a tax instance
      coordinate,
      name,
      taxMap("price").toInt
    )
  }

  private def createUtility(coordinate: Point2D, name: String): Utility = {
    val utilityMap = DataLoader.loadAttributes("utility", name) //get the information of utility by passing the utility name
    Utility( // create an utility instance
      coordinate,
      name,
      utilityMap("price").toInt,
      utilityMap("rent_1").toInt,
      utilityMap("rent_2").toInt,
      utilityMap("mortgage").toInt
    )
  }

  def calcRent(lot: Ownable): Int ={ //to calculate the rent of the ownable lot
    lot match{ //down casting from superclass (Ownable) to subclass
      case property: Property => property.calcRent(filterPropertyByGroup(property.group))
      case utility: Utility => utility.calcRent(filterLotByType(classOf[Utility]))
      case railroad: Railroad =>railroad.calcRent(filterLotByType(classOf[Railroad]))
    }
  }

  private def filterPropertyByGroup(groupNum: Int): List[Property] = { //to filter the Properties to get a List of Property with same specific group number
    filterLotByType(classOf[Property]).collect{
      case p: Property if p.group == groupNum => p
    }
  }

  private def filterLotByType[T <: BoardElement](desiredType: Class[T]): List[T] = { //to filter the Lot to get a List of the desired data type of BoardElement
    boardElements.collect{
      case element if desiredType.isInstance(element) => element.asInstanceOf[T]
    }
  }

  def getLotPositionByName(lotName: String): Int = {
    boardElements.indexWhere(_.name == lotName)
  }

  def getPath(startPosition: Int, endPosition: Int): List[Point2D] = { //to identify the path to bypass (to be used when moving token)
    val coordinates = boardCoordinates.map(_._1) //get a list of all coordinates
    if (startPosition <= endPosition) {
      coordinates.slice(startPosition, endPosition + 1) //slice the unnecessary coordinate (only keep the coordinate between start and end)
    } else { //if start > end, bypassed base
      coordinates.slice(startPosition, coordinates.length) ++ coordinates.slice(0, endPosition + 1)
    }
  }
}
