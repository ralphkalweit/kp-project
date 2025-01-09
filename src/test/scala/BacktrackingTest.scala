import sudoku.Backtracking.{
  backtrackingWithElimination,
  backtrackingWithoutElimination
}
import sudoku.SudokuIO.{getLogicalGrid, getString}
import org.scalatest.funsuite.AnyFunSuite
import sudoku.SolverHelper.isPartialSolution

class BacktrackingTest extends AnyFunSuite {

  private val strategies =
    Vector(backtrackingWithElimination, backtrackingWithoutElimination)

  private val dfsOptions = Set(true, false)

  test("Solvable 4x4") {
    strategies.foreach { strategy =>
      val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
      val expectedSolution = "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"
      val grid = getLogicalGrid(sudokuString)

      val solution = strategy(List(grid), true)
      val solutionBFS = strategy(List(grid), true)

      assert(getString(solution.getOrElse(Vector())) == expectedSolution)
      assert(getString(solutionBFS.getOrElse(Vector())) == expectedSolution)
    }
  }

  test("Empty") {
    val f = List(getLogicalGrid("_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"))
    strategies.foreach(strategy =>
      dfsOptions.foreach(useDFS => assert(strategy(f, useDFS).nonEmpty))
    )
  }

  test("Solvable 9x9") {
    strategies.foreach { strategy =>
      dfsOptions.foreach { useDFS =>
        val sudokuString =
          "_ 6 _ 3 _ 8 1 _ _\n5 8 1 _ _ 9 6 _ _\n_ _ 3 5 _ _ _ _ _\n_ 9 _ 8 _ 2 _ _ _\n _ _ _ 7 4 5 9 3 6\n3 5 _ _ _ 6 8 _ 4\n1 3 5 _ _ _ _ _ _\n7 _ 8 6 1 3 4 _ _\n9 4 _ _ _ 7 3 _ _"

        val grid = getLogicalGrid(sudokuString)

        val solution = strategy(List(grid), useDFS)
        assert(solution.nonEmpty)

        assert(isPartialSolution(grid, solution.get))
      }
    }
  }

  test("not solvable") {
    strategies.foreach { strategy =>
      dfsOptions.foreach { useDFS =>
        Vector(
          getLogicalGrid("1 _ _ _\n2 _ 3 4\n_ _ _ _\n_ _ _ _"),
          getLogicalGrid("1 _ _ _\n_ 2 3 4\n_ _ _ _\n_ _ _ _"),
          getLogicalGrid("1 _ _ _\n_ _ _ 4\n_ _ _ 3\n_ _ _ 2")
        ).foreach { grid =>
          assert(strategy(List(grid), useDFS).isEmpty)
        }
      }
    }
  }

}
