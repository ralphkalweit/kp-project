import org.scalatest.funsuite.AnyFunSuite
import sudoku.SudokuIO.getLogicalGrid
import sudoku.SudokuValidation.{getSudokuBlocks, hasLogicalErrors, isCompleteSudoku}

class TrueNxNTest extends AnyFunSuite{

  test("NxN for non-squared N") {

    val checkHasErrors = Map(
      "1 2 3\n3 1 2\n2 3 1" -> false,
      "5 4 3 2 1\n5 _ _ _ _\n _ _ _ _ _\n_ _ _ _ _\n_ _ _ _ _" -> true
    )

    val checkIsComplete = Map(
      "1 2 3\n3 1 2\n2 3 1" -> true,
      "5 4 3 2 1\n5 _ _ _ _\n _ _ _ _ _\n_ _ _ _ _\n_ _ _ _ _" -> false,
      "5 4 3 2 1\n_ _ _ _ _\n _ _ _ _ _\n_ _ _ _ _\n_ _ _ _ _" -> false,
      "_ 2 3\n3 1 2\n2          3 1" -> false,
    )

    val checkGetSudokuBlocks = Map(
      "1 2\n2 1" -> Vector(Vector(Some(1), Some(2)), Vector(Some(2), Some(1))),
      "1 2 3 4\n 3 4 1 2 \n 2 1 4 3\n4 3 2 1" ->
        Vector(
          Vector(Some(1), Some(2), Some(3), Some(4)),
          Vector(Some(3), Some(4), Some(1), Some(2)),
          Vector(Some(2), Some(1), Some(4), Some(3)),
          Vector(Some(4), Some(3), Some(2), Some(1))
        )
    )

    val checks = Map(checkHasErrors -> hasLogicalErrors,
      checkIsComplete -> isCompleteSudoku,
      checkGetSudokuBlocks -> getSudokuBlocks
    )

    checks.toList.foreach((data, testFn) =>
      data.toList.foreach((str, correct) =>
        val grid = getLogicalGrid(str)
        assert(testFn(grid) == correct)
      )
    )








  }


}
