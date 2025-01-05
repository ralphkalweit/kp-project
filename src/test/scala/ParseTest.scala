import sudoku.SudokuIO.{asIntGrid, getGrid, asStringGrid}
import sudoku.CellValidation.listContainsOnlyValidStrings
import org.scalatest.funsuite.AnyFunSuite

class ParseTest extends AnyFunSuite {

  test("wrong shape") {
    val sudoku = "_\n_ 2"
    try {
      asStringGrid(sudoku)
    } catch {
      case e: RuntimeException => assert(true)
      case _                   => assert(false)
    }
  }

  test("gridify 1x1") {
    assert(asStringGrid("_").nonEmpty)
  }

  test("gridify 3x3") {
    assertThrows[RuntimeException](asStringGrid("_ _ _\n_ _ _\n_ _ _"))
    // TODO support any type of sudoku, not just n^2 x n^2
    // assert(asStringGrid("_ _ _\n_ _ _\n_ _ _").length == 3)
  }

  test("more spaces, no problems?") {
    assert(
      asStringGrid(
        "_  _ _ _\n_ _ _ _ \n_ _ _       _\n_   _   _     _"
      ).length == 4
    )
    assert(asStringGrid("1 2 3 4\n3 _ _ 2 \n 2 1 4 3 \n4 3 2 1").length == 4)
  }

  test("unsupported Character") {
    try {
      asStringGrid("_ a\n_ (")
    } catch {
      case e: Exception => assert(true)
      case _            => assert(false)
    }
  }

  test("ContainsOnlyValidStrings ???") {
    val list = List("1", "2", "_", "4", "5")
    assert(listContainsOnlyValidStrings(list, 5))
    assert(listContainsOnlyValidStrings(list, 6))
    assert(!listContainsOnlyValidStrings(list, 4))

    val list2 = List("-1")
    assert(!listContainsOnlyValidStrings(list2, 5))

    val wrong = "_ a\n2 _"
    assertThrows[RuntimeException](asStringGrid(wrong))
  }

  test("to int grid") {
    val strGrid = asStringGrid("_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _")
    val intGrid = asIntGrid(strGrid)

    intGrid.foreach {
      _.foreach { * => assert(*.isEmpty) }
    }

    val _grid = asStringGrid("1 2 3 4\n3 2 1 4\n3 2 1 4\n3 2 1 4")
    val grid2 = asIntGrid(_grid)
    grid2.foreach { _.foreach { * => assert(*.nonEmpty) } }
  }

  test("int grid from many spaces") {
    val str = "1 2  3 4 \n3 _   _ 2 \n 2 1 4   3 \n4 3 2 1"
    val strGrid = asStringGrid(str)
    val optGrid = getGrid(str)

    assert(strGrid.length == optGrid.length)
    strGrid.foreach { strList =>
      optGrid.foreach { optList =>
        assert(optList.length == strList.length)
      }
    }
  }
}
