package Main

import java.io.{FileNotFoundException, PrintWriter}
import scala.io.Source
import scala.util.Using
import Main.CellValidation.listContainsOnlyValidStrings


object SudokuIO {

  def getFileContent(filePath: String): String = {
    val fileContent = Using(Source.fromFile(filePath)) { source =>
      source.getLines().mkString("\n")
    }.getOrElse(throw new FileNotFoundException(s"Failed to read file at $filePath"))
    if (fileContent.isEmpty || fileContent == "") {
      throw new RuntimeException(s"File $filePath is empty")
    }
    fileContent
  }

  def asStringGrid(fileContent: String): List[List[String]] = {
    val rows = fileContent.split('\n')
    val assumedDim = rows.length

    val grid: List[List[String]] = rows.map { row =>
      val list = row.split(" ").toList.filter(cell => cell.nonEmpty)
      if list.length != assumedDim then throw new RuntimeException(s"Width and length of the grid are not equal: ${list.toString()}")
      if !listContainsOnlyValidStrings(list, assumedDim) then throw new RuntimeException(s"Invalid Character Found in row ${list.toString()}")
      list
    }.toList

    grid
  }

  def asIntGrid(grid: List[List[String]]): List[List[Option[Int]]] = {
    grid.map(_.map(
      str => try {
        Some(str.toInt)
      } catch {
        case e: NumberFormatException => None
        case _: Exception => throw new Exception("Unexpected Error")
      }
    )
    )
  }

  def getGrid(fileContent: String): List[List[Option[Int]]] = asIntGrid(asStringGrid(fileContent))

  def getString(grid: List[List[Option[Int]]]): String = {
    grid.map{ row =>
      row.map {
      case None => "_"
      case Some(value) => s"$value"
      }.mkString(" ")
    }.mkString("\n")
  }

  def saveSudoku(filePath: String, sudoku: List[List[Option[Int]]]): Unit = {
    Using(new PrintWriter(filePath)) { writer =>
      writer.write(getString(sudoku))
    }
  }
}
