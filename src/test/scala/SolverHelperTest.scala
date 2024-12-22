import Main.SolverHelper.{insertAtFirstBlank}
import Main.SudokuIO.{getGrid, getString}
import org.scalatest.funsuite.AnyFunSuite

class SolverHelperTest extends AnyFunSuite {

  test("insert at first _") {
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val insertElem = 1
    val expectedWithInserted = s"1 2 3 4\n3 $insertElem _ 2\n2 1 4 3\n4 3 2 1"

    val grid = getGrid(sudokuString)
    val withInserted = getString(insertAtFirstBlank(grid, insertElem))

    assert(withInserted == expectedWithInserted)
  }

  test("empty") {
    val str = "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"
    val exp = "4 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"

    val act = getString(insertAtFirstBlank(getGrid(str), 4))

    assert(exp == act)
  }

  test("inserted number too big") {
    val str = "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"
    val exp = "80 _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"

    val act = getString(insertAtFirstBlank(getGrid(str), 80))
    // TODO maybe expect an error here
    assert(exp != act)
  }

}
