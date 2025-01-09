import sudoku.SolverHelper.{eliminateListFromAllSingles, eliminateOneStep, eliminateRecursive, eliminationMatrixIsSolved, getEliminationMatrix, getSudokuFromEliminationMatrix, insertAtFirstBlank, isPartialSolution}
import sudoku.SudokuIO.{areEqual, getLogicalGrid, getString, saveSudoku}
import sudoku.SudokuValidation.{getSudokuBlocks, getSudokuFromSudokuBlocks, hasLogicalErrors, isCompleteVector, isCompleteSudoku}
import org.scalatest.funsuite.AnyFunSuite
import sudoku.Strategies.trySolveWithElimination

import scala.::

class SolverHelperTest extends AnyFunSuite {

  test("insert at first _") {
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val insertElem = 1
    val expectedWithInserted = s"1 2 3 4\n3 $insertElem _ 2\n2 1 4 3\n4 3 2 1"

    val grid = getLogicalGrid(sudokuString)
    val withInserted = getString(insertAtFirstBlank(grid, insertElem))

    assert(withInserted == expectedWithInserted)
  }

  test("empty") {
    val str = "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"
    val exp = "4 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"

    val act = getString(insertAtFirstBlank(getLogicalGrid(str), 4))

    assert(exp == act)
  }

  test("inserted number too big") {
    val str = "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"
    val exp = "80 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"

    val act = getString(insertAtFirstBlank(getLogicalGrid(str), 80))
    // TODO maybe expect an error here
    assert(exp != act)
  }

  test("transform to and from elimination Matrix") {

    Vector(
      "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _",
      "4 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _",
      "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    ).foreach { str =>
      val grid = getLogicalGrid(str)
      val eliminationMatrix = getEliminationMatrix(grid)
      val actual = getSudokuFromEliminationMatrix(eliminationMatrix)
      assert(areEqual(grid, actual))
    }
  }

  test("inverse block retrieval") {
    Vector(
      "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _",
      "4 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _",
      "4 3 2 1\n_ _ _ _\n_ _ _ _\n_ _ _ _",
      "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1",
      "5 _ _ _ _ 8 _ _ 3 _ _ _ _ _ _ 7\n_ 7 _ 1 _ _ _ 9 _ _ _ _ _ _ _ _\n_ _ _ _ 6 _ _ _ 5 _ _ _ _ 12 _ _\n_ 2 _ _ _ _ 6 _ _ _ _ _ _ _ _ _\n_ _ _ 3 _ 7 _ _ _ _ 9 _ _ _ _ _\n_ _ _ _ _ _ _ 4 _ _ _ _ _ 1 _ _\n12 _ _ _ _ _ _ _ 7 _ _ _ _ _ _ 6\n_ _ _ _ _ 5 _ _ _ _ _ _ 9 _ _ _\n_ _ _ _ _ _ _ _ _ _ _ 14 _ _ 6 _\n9 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n_ 12 _ _ _ 6 _ _ _ _ _ _ _ _ _ _\n_ _ _ _ 5 _ _ _ 9 _ _ _ _ _ _ _\n_ _ 9 _ _ _ _ _ _ _ _ _ 8 _ _ _\n_ _ _ _ _ _ _ 7 _ _ 14 _ _ _ _ _\n_ _ _ _ _ 9 _ _ _ 13 _ _ _ _ 8 _\n6 _ _ _ _ _ _ _ _ _ _ _ _ _ _ 5",
      "1 2 3 4 5 6 7 8 9\n4 5 6 7 8 9 1 2 3\n7 8 9 1 2 3 4 5 6\n2 3 4 5 6 7 8 9 1\n5 6 7 8 9 1 2 3 4\n8 9 1 2 3 4 5 6 7\n3 4 5 6 7 8 9 1 2\n6 7 8 9 1 2 3 4 5\n9 1 2 3 4 5 6 7 8"
    ).foreach { str =>
      val grid = getLogicalGrid(str)
      val blocks = getSudokuBlocks(grid)
      val actual = getSudokuFromSudokuBlocks(blocks)

      assert(areEqual(grid, actual))
    }
  }

  test("eliminateFromAllSingles should handle various cases correctly") {
    val testCases = Vector(
      Vector(Set(1, 2, 3), Set(1), Set(2, 4), Set(5)) -> Vector(
        Set(2, 3),
        Set(1),
        Set(2, 4),
        Set(5)
      ),
      Vector(Set(1, 2), Set(3, 4), Set(5, 6)) -> Vector(
        Set(1, 2),
        Set(3, 4),
        Set(5, 6)
      ),
      Vector(Set(1), Set(2), Set(3)) -> Vector(Set(1), Set(2), Set(3)),
      Vector(Set(1, 2, 3), Set(1), Set(1, 4), Set(5)) -> Vector(
        Set(2, 3),
        Set(1),
        Set(4),
        Set(5)
      )
    )

    testCases.foreach { case (input, expectedOutput) =>
      assert(eliminateListFromAllSingles(input) == expectedOutput)
    }
  }

  test("eliminate one step") {
    val smallSudoku = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val expectedSolution = "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"
    val possibilities = getEliminationMatrix(getLogicalGrid(smallSudoku))
    val eliminatedOnce = eliminateOneStep(possibilities)
    assert(eliminationMatrixIsSolved(eliminatedOnce))
    //
    val result = getSudokuFromEliminationMatrix(eliminatedOnce)
    val expectedResult = getLogicalGrid(expectedSolution)
    assert(areEqual(result, expectedResult))
  }

  test("elimination tail recursive") {
    val sudokuMap = Map(
      "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1" -> "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"
    )
    sudokuMap.foreach { case (inputSudoku, expectedSolution) =>
      val poss = getEliminationMatrix(getLogicalGrid(inputSudoku))
      val eliminatedResult = eliminateRecursive(poss)
      assert(eliminationMatrixIsSolved(eliminatedResult))

      val result = getSudokuFromEliminationMatrix(eliminatedResult)
      val expectedResult = getLogicalGrid(expectedSolution)
      assert(
        areEqual(result, expectedResult)
      )
    }
  }

  test("partial Solution 4x4") {
    assert(isPartialSolution(Vector(), Vector()))
    val x4_empty = "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"
    val sudokuList = x4_empty :: List("_ 2 _ 4\n_ 4 _ 2\n2 1 4 3\n4 3 2 1", "1 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _")

    sudokuList.foreach(str =>
      val grid = getLogicalGrid(str)
      assert(isPartialSolution(grid, grid))
      assert(isPartialSolution(getLogicalGrid(x4_empty), grid))
    )
  }

  test("elimination won't help with everything") {
    val str = "_ 2 _ 4\n_ 4 _ 2\n2 1 4 3\n4 3 2 1"
    val grid = getLogicalGrid(str)

    val attempt = trySolveWithElimination(grid)
    assert(isPartialSolution(grid, grid))

    assert(attempt == grid)
    assert(isPartialSolution(attempt, grid))

  }

  test("eliminate 9x9") {
    val x9 =
      "_ _ _ 7 _ _ _ _ _\n7 2 _ _ _ 9 _ 5 1\n8 9 1 _ 2 6 _ 7 _\n9 _ 3 2 _ _ _ 6 8\n6 8 _ 1 _ _ 3 4 2\n2 5 4 _ _ _ _ _ _\n_ _ 9 3 1 _ 6 8 _\n_ _ _ 9 5 _ _ _ 4\n_ 3 _ 6 7 _ _ 1 _"
    //
    val grid = getLogicalGrid(x9)
    val eliminated = trySolveWithElimination(grid)
    saveSudoku("9x9.sudoku", eliminated)
    //
    assert(!hasLogicalErrors(eliminated))
    assert(isCompleteSudoku(eliminated))
    //
    assert(isPartialSolution(grid, eliminated))
  }

}
