package Main

import java.io.{FileNotFoundException, PrintWriter}
import scala.io.Source
import scala.util.Using
import Main.CellValidation.listContainsOnlyValidStrings
import Main.LinterStyler.{getUniformLength, toStringWithLen}

object SudokuIO {

  def getFileContent(filePath: String): String = {
    val fileContentOpt = Using(Source.fromFile(filePath)) { source =>
      source.getLines().mkString("\n")
    }
    require(fileContentOpt.isSuccess, s"Failed to read file at $filePath")
    require(fileContentOpt.get.nonEmpty, s"File $filePath is empty")
    fileContentOpt.get
  }

  def asStringGrid(fileContent: String): List[List[String]] = {
    val rows = fileContent.split('\n')
    val assumedDim = rows.length
    val sqrt = math.sqrt(assumedDim).intValue()
    require(
      sqrt * sqrt == assumedDim,
      s"Expected shape n^2 * n^2 but found $assumedDim * $assumedDim"
    )

    val grid: List[List[String]] = rows.map { row =>
      val list = row.split(" ").toList.filter(cell => cell.nonEmpty)
      require(
        list.length == assumedDim,
        s"Width and length of the grid are not equal: ${list.toString()}"
      )
      require(
        listContainsOnlyValidStrings(list, assumedDim),
        s"Invalid Character Found in row ${list.toString()}"
      )
      list
    }.toList

    grid
  }

  def asIntGrid(grid: List[List[String]]): List[List[Option[Int]]] = {
    grid.map(
      _.map(str =>
        try {
          Some(str.toInt)
        } catch {
          case e: NumberFormatException => None
          case _: Exception             => throw new Exception("Unexpected Error")
        }
      )
    )
  }

  def getGrid(fileContent: String): List[List[Option[Int]]] = asIntGrid(
    asStringGrid(fileContent)
  )

  def getCellString(cellContent: Option[Int]): String = {
    cellContent match {
      case Some(value) => s"$value"
      case _           => "_"
    }
  }

  def getString(
      grid: List[List[Option[Int]]]
  ): String = {
    val uniformLength = getUniformLength(grid.length)

    grid
      .map {
        _.map { toStringWithLen(_, uniformLength) }
          .mkString(" ")
      }
      .mkString("\n")
  }

  def areEqual[T](
      sudoku1: List[List[T]],
      sudoku2: List[List[T]]
  ): Boolean = {
    sudoku1.zip(sudoku2).forall { (row1, row2) =>
      row1.zip(row2).forall(_ == _)
    }
  }

  def saveSudoku(filePath: String, sudoku: List[List[Option[Int]]]): Unit = {
    Using(new PrintWriter(filePath)) { writer =>
      writer.write(getString(sudoku))
    }
  }

  def loadSudoku(filePath: String): List[List[Option[Int]]] = {
    try {
      val str = getFileContent(filePath)
      return getGrid(str)
    } catch {
      case e: Exception => println(e.toString)
      case _            => ()
    }
    List()
  }
}
