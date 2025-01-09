import org.scalatest.funsuite.AnyFunSuite
import sudoku.LinterStyler.toStringWithLen
import sudoku. SudokuContextual.sudokuExtensions
import sudoku.SudokuIO.{
  asIntGrid,
  getCellString,
  getLogicalGrid,
  loadSudoku,
  saveSudoku
}

import java.io.PrintWriter
import java.nio.file.{Files, Paths}
import scala.util.Using

class LinterStylerTest extends AnyFunSuite {

  test("adjustNumDigits") {

    val cells = Vector(Some(1), Some(10), None)
    val kinds = Vector(3, 5, 1)

    cells.foreach(cell =>
      kinds.foreach(uniLen =>
        if (getCellString(cell).length > uniLen) {
          assertThrows[RuntimeException](toStringWithLen(cell, uniLen))
        } else {
          val str = toStringWithLen(cell, uniLen)
          assert(str.length == uniLen)
        }
      )
    )

    assert(toStringWithLen(None, 3) == "  _")
    assert(toStringWithLen(None, 2) == " _")
    assert(toStringWithLen(Some(1), 1) == "1")
  }

  test("4x4 empty") {
    val str = "_    _    _   1\n_  _  _  _\n_ _ _ 3\n_ _ _ _"
    val grid = getLogicalGrid(str)
    val newString = grid.asString

    assert(newString != str)
    assert(newString.split(" ").length == 13)
  }

  test("convert, lint, convert 16x16") {

    val filePath = "16x16_TEST_LINT.sudoku"

    val sudoku16: String =
      "5 _ _ _ _ 8 _ _ 3 _ _ _ _ _ _ 7\n_ 7 _ 1 _ _ _ 9 _ _ _ _ _ _ _ _\n_ _ _ _ 6 _ _ _ 5 _ _ _ _ 12 _ _\n_ 2 _ _ _ _ 6 _ _ _ _ _ _ _ _ _\n_ _ _ 3 _ 7 _ _ _ _ 9 _ _ _ _ _\n_ _ _ _ _ _ _ 4 _ _ _ _ _ 1 _ _\n12 _ _ _ _ _ _ _ 7 _ _ _ _ _ _ 6\n_ _ _ _ _ 5 _ _ _ _ _ _ 9 _ _ _\n_ _ _ _ _ _ _ _ _ _ _ 14 _ _ 6 _\n9 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n_ 12 _ _ _ 6 _ _ _ _ _ _ _ _ _ _\n_ _ _ _ 5 _ _ _ 9 _ _ _ _ _ _ _\n_ _ 9 _ _ _ _ _ _ _ _ _ 8 _ _ _\n_ _ _ _ _ _ _ 7 _ _ 14 _ _ _ _ _\n_ _ _ _ _ 9 _ _ _ 13 _ _ _ _ 8 _\n6 _ _ _ _ _ _ _ _ _ _ _ _ _ _ 5"
    Using(new PrintWriter(filePath)) {
      _.write(sudoku16)
    }

    val grid = loadSudoku(filePath)
    val newString = grid.asString

    assert(newString.split(" ").length > sudoku16.split(" ").length)
    assert(
      newString.replace(" ", "").length == sudoku16.replace(" ", "").length
    )

    val filePath2 = Paths.get(filePath)
    if (Files.exists(filePath2)) {
      Files.delete(filePath2)
    }
  }

  test("empty 100 x 100") {

    val size = 100
    val expectedSpaces = " " * (size.toString.length - 1)

    val gridString =
        getLogicalGrid(List.fill(size)(" " + "_ " * size).mkString("\n")).asString
      

    gridString.split('\n').foreach { row =>
      assert(row.startsWith(s"${expectedSpaces}_"))
      assert(row.last == '_')
      assert(row.split(expectedSpaces).length == size + 1)
      assert(row.replace(" ", "") == "_" * size)
    }

  }
}
