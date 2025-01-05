package sudoku

import sudoku.CellValidation.listContainsOnlyValidStrings
import sudoku.LinterStyler.{getUniformLength, toStringWithLen}
import sudoku.SudokuTypes.{SudokuLogicalGrid, SudokuStringGrid}

import java.io.{FileNotFoundException, PrintWriter}
import scala.io.Source
import scala.util.Using

object SudokuIO {

  def getFileContent(filePath: String): String = {
    val fileContentOpt = Using(Source.fromFile(filePath)) { source =>
      source.getLines().mkString("\n")
    }
    if (!fileContentOpt.isSuccess)
      throw new FileNotFoundException(s"Failed to read file at $filePath")
    if (fileContentOpt.get.isEmpty)
      throw new FileNotFoundException(s"File $filePath is empty")
    fileContentOpt.get
  }

  def asSudokuStringGrid(fileContent: String): SudokuStringGrid = {
    val rows = fileContent.split('\n')
    val assumedDim = rows.length
    val sqrt = math.sqrt(assumedDim).intValue()
    require(
      sqrt * sqrt == assumedDim,
      s"Expected shape n^2 * n^2 but found $assumedDim * $assumedDim"
    )

    val grid: SudokuStringGrid = rows.map { row =>
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

  def asIntGrid(grid: SudokuStringGrid): SudokuLogicalGrid = {
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

  def getLogicalGrid(fileContent: String): SudokuLogicalGrid = asIntGrid(
    asSudokuStringGrid(fileContent)
  )

  def getCellString(cellContent: Option[Int]): String = {
    cellContent match {
      case Some(value) => s"$value"
      case _           => "_"
    }
  }

  def getString(
      grid: SudokuLogicalGrid
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

  def saveSudoku(filePath: String, sudoku: SudokuLogicalGrid): Unit = {
    Using(new PrintWriter(filePath)) { writer =>
      writer.write(getString(sudoku))
    }
  }

  def loadSudoku(filePath: String): SudokuLogicalGrid = {
    try {
      val str = getFileContent(filePath)
      getLogicalGrid(str)
    } catch {
      case e: Exception => throw e
      case _            => List()
    }
  }
}
