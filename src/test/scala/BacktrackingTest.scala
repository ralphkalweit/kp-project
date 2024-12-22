import Main.Backtracking.backtracking
import Main.SudokuIO.{getGrid, getString}
import org.scalatest.funsuite.AnyFunSuite

class BacktrackingTest extends AnyFunSuite {

  test("Solvable 4x4") {
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val expectedSolution = "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"
    val grid = getGrid(sudokuString)
    val solution = backtracking(List(grid))

    assert(getString(solution.getOrElse(List())) == expectedSolution)
  }

  test("Empty") {
    assert(backtracking(List(getGrid("_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"))).nonEmpty)
  }

  test("Solvable 9x9") {
    val sudokuString =
      "_ 6 _ 3 _ 8 1 _ _\n5 8 1 _ _ 9 6 _ _\n_ _ 3 5 _ _ _ _ _\n_ 9 _ 8 _ 2 _ _ _\n _ _ _ 7 4 5 9 3 6\n3 5 _ _ _ 6 8 _ 4\n1 3 5 _ _ _ _ _ _\n7 _ 8 6 1 3 4 _ _\n9 4 _ _ _ 7 3 _ _"

    val solution = backtracking(List(getGrid(sudokuString)))

    assert(solution.nonEmpty)
//    println(s"Solution:\n${getString(solution.getOrElse(List()))}")
  }

}
