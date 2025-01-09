import sudoku.SudokuIO.getLogicalGrid
import org.scalatest.funsuite.AnyFunSuite
import sudoku.SudokuTypes.SudokuLogicalGrid
import sudoku.SudokuValidation.isCompleteVector

import sudoku.SudokuContextual.sudokuExtensions

class SudokuValidationTest extends AnyFunSuite {

  test("get all regions from sudoku n^2 x n^2 for n=2") {
    // TODO this is a good test to compare with the go version
    val sudoku = getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1")
    val expectedCols = sudoku.transpose
    val expectedBlocks: SudokuLogicalGrid = Vector(
      Vector(Some(1), Some(2), Some(3), Some(4)),
      Vector(Some(3), Some(4), Some(1), Some(2)),
      Vector(Some(2), Some(1), Some(4), Some(3)),
      Vector(Some(4), Some(3), Some(2), Some(1))
    )

    assert(sudoku == sudoku.getRows)
    assert(expectedCols == sudoku.getColumns)
    assert(expectedBlocks == sudoku.getBlocks)
  }

  test("is complete List") {
    assert(isCompleteVector(Vector(Some(1), Some(2))))
    assert(!isCompleteVector(Vector(Some(1), None)))
    assert(!isCompleteVector(Vector()))
  }

  test("sudoku.isValidAndCompleteComplete") {
    val pairs = Map(
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1" -> true,
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n2 2 2 2" -> false,
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 _ 2 1" -> false,
    )

    pairs.toList.foreach((str, correct) =>
      val grid = getLogicalGrid(str)
      assert(grid.isValidAndCompleteComplete == correct)
    )

    assert(!Vector().isValidAndCompleteComplete)
  }

  test("sudoku.isCorrect") {
    val pairs = Map(
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1" -> true,
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 3 2" -> false,
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 _ 2 1" -> true,
      "1 2 3 4\n 3 4 1 2 \n 2 _ 4 3\n4 _ 2 1" -> true,
      "_ _ _ 1\n_ _ _ _\n_ _ _ 1\n_ _ _ _" -> false,
      "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _" -> true,
      "1 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8" -> true,
      "1 2 3 4\n3 1 2 _\n2 3 4 1\n _ _ _ _" -> false,
    )

    pairs.toList.foreach((str, correct) =>
      val grid = getLogicalGrid(str)
      assert(grid.isCorrect == correct)
    )
    assert(Vector().isCorrect)
  }

}
