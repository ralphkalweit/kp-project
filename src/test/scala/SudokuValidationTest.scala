import Main.SudokuIO.{getGrid, asStringGrid}
import org.scalatest.funsuite.AnyFunSuite
import Main.SudokuValidation.{
  getSudokuBlocks,
  getSudokuColumns,
  getSudokuRows,
  isCompleteList,
  isCompleteSudoku,
  hasErrors
}

class SudokuValidationTest extends AnyFunSuite {

  test("get all regions from sudoku n^2 x n^2 for n=2") {
    val sudoku = getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1")
    val expectedCols = sudoku.transpose
    val expectedBlocks: List[List[Option[Int]]] = List(
      List(Some(1), Some(2), Some(3), Some(4)),
      List(Some(3), Some(4), Some(1), Some(2)),
      List(Some(2), Some(1), Some(4), Some(3)),
      List(Some(4), Some(3), Some(2), Some(1))
    )

    val rows = getSudokuRows(sudoku)
    assert(sudoku == rows)
    assert(expectedCols == getSudokuColumns(sudoku))
    assert(expectedBlocks == getSudokuBlocks(sudoku))
  }

  test("is complete List") {
    assert(isCompleteList(List(Some(1), Some(2))))
    assert(!isCompleteList(List(Some(1), None)))
    assert(!isCompleteList(List()))
  }

  test("complete sudoku") {
    assert(isCompleteSudoku(getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1")))

    // last row is wrong:
    // TODO maybe expect an error here instead
    assert(!isCompleteSudoku(getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n2 2 2 2")))

    // last row has one missing:
    assert(!isCompleteSudoku(getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 _ 2 1")))

    assert(!isCompleteSudoku(List()))
  }

  test("Sudoku has errors") {
    assert(!hasErrors(getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1")))
    assert(hasErrors(getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 3 2")))
    assert(!hasErrors(getGrid("1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 _ 2 1")))
    assert(!hasErrors(getGrid("1 2 3 4\n 3 4 1 2 \n 2 _ 4 3\n4 _ 2 1")))
    assert(hasErrors(getGrid("_ _ _ 1\n_ _ _ _\n_ _ _ 1\n_ _ _ _")))
    assert(!hasErrors(getGrid("_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _")))

    assert(
      !hasErrors(
        getGrid(
          "1 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8"
        )
      )
    )
  }

}