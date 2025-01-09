import sudoku.SudokuIO.getLogicalGrid
import org.scalatest.funsuite.AnyFunSuite
import sudoku.SudokuTypes.SudokuLogicalGrid
import sudoku.SudokuValidation.{
  getSudokuBlocks,
  getSudokuColumns,
  getSudokuRows,
  hasLogicalErrors,
  isCompleteVector,
  isCompleteSudoku
}

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

    val rows = getSudokuRows(sudoku)
    assert(sudoku == rows)
    assert(expectedCols == getSudokuColumns(sudoku))
    assert(expectedBlocks == getSudokuBlocks(sudoku))
  }

  test("is complete List") {
    assert(isCompleteVector(Vector(Some(1), Some(2))))
    assert(!isCompleteVector(Vector(Some(1), None)))
    assert(!isCompleteVector(Vector()))
  }

  test("complete sudoku") {
    assert(
      isCompleteSudoku(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1"))
    )

    // last row is wrong:
    assert(
      !isCompleteSudoku(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n2 2 2 2"))
    )

    // last row has one missing:
    assert(
      !isCompleteSudoku(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 _ 2 1"))
    )

    assert(!isCompleteSudoku(Vector()))
  }

  test("Sudoku has errors") {
    assert(
      !hasLogicalErrors(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1"))
    )
    assert(
      hasLogicalErrors(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 3 2"))
    )
    assert(
      !hasLogicalErrors(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 _ 2 1"))
    )
    assert(
      !hasLogicalErrors(getLogicalGrid("1 2 3 4\n 3 4 1 2 \n 2 _ 4 3\n4 _ 2 1"))
    )
    assert(
      hasLogicalErrors(getLogicalGrid("_ _ _ 1\n_ _ _ _\n_ _ _ 1\n_ _ _ _"))
    )
    assert(
      !hasLogicalErrors(getLogicalGrid("_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"))
    )

    assert(
      !hasLogicalErrors(
        getLogicalGrid(
          "1 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8"
        )
      )
    )
  }

}
