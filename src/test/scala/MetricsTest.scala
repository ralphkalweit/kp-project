import sudoku.Metrics.measureGivens
import sudoku.SudokuIO.getLogicalGrid
import org.scalatest.funsuite.AnyFunSuite

class MetricsTest extends AnyFunSuite {

  test("givens") {
    val sudoku = "_ _ _ _\n 1 2 3 4\n4 3 2 1\n_ _ _ _"
    val actualGivens = measureGivens(getLogicalGrid(sudoku))
    assert(8 == actualGivens)

    val empty4 = Iterator.fill(4)("_ _ _ _").mkString("\n")
    assert(0 == measureGivens(getLogicalGrid(empty4)))

    val full4 = Iterator.fill(4)("1 1 4 4").mkString("\n")
    assert(16 == measureGivens(getLogicalGrid(full4))) // TODO expect error instead?
  }

}
