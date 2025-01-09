import sudoku.SudokuIO.{asIntGrid, asSudokuStringGrid, getLogicalGrid}
import sudoku.CellValidation.vectorContainsOnlyValidStrings
import org.scalatest.funsuite.AnyFunSuite

class ParseTest extends AnyFunSuite {

  test("wrong shape") {
    val sudoku = "_\n_ 2"
    try {
      asSudokuStringGrid(sudoku)
    } catch {
      case e: RuntimeException => assert(true)
      case _                   => assert(false)
    }
  }

  test("gridify 1x1") {
    assert(asSudokuStringGrid("_").nonEmpty)
  }

  test("3x3") {
    assert(asSudokuStringGrid("_ _ _\n_ _ _\n_ _ _").length == 3)
  }

  test("more spaces, no problems?") {
    assert(
      asSudokuStringGrid(
        "_  _ _ _\n_ _ _ _ \n_ _ _       _\n_   _   _     _"
      ).length == 4
    )
    assert(
      asSudokuStringGrid("1 2 3 4\n3 _ _ 2 \n 2 1 4 3 \n4 3 2 1").length == 4
    )
  }

  test("unsupported Character") {
    try {
      asSudokuStringGrid("_ a\n_ (")
    } catch {
      case e: Exception => assert(true)
      case _            => assert(false)
    }
  }

  test("ContainsOnlyValidStrings ???") {
    val list = Vector("1", "2", "_", "4", "5")
    assert(vectorContainsOnlyValidStrings(list, 5))
    assert(vectorContainsOnlyValidStrings(list, 6))
    assert(!vectorContainsOnlyValidStrings(list, 4))

    val list2 = Vector("-1")
    assert(!vectorContainsOnlyValidStrings(list2, 5))

    val wrong = "_ a\n2 _"
    assertThrows[RuntimeException](asSudokuStringGrid(wrong))
  }

  test("to int grid") {
    val strGrid = asSudokuStringGrid("_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _")
    val intGrid = asIntGrid(strGrid)

    intGrid.foreach {
      _.foreach { * => assert(*.isEmpty) }
    }

    val _grid = asSudokuStringGrid("1 2 3 4\n3 2 1 4\n3 2 1 4\n3 2 1 4")
    val grid2 = asIntGrid(_grid)
    grid2.foreach { _.foreach { * => assert(*.nonEmpty) } }
  }

  test("int grid from many spaces") {
    val str = "1 2  3 4 \n3 _   _ 2 \n 2 1 4   3 \n4 3 2 1"
    val strGrid = asSudokuStringGrid(str)
    val optGrid = getLogicalGrid(str)

    assert(strGrid.length == optGrid.length)
    strGrid.foreach { strList =>
      optGrid.foreach { optList =>
        assert(optList.length == strList.length)
      }
    }
  }
}
