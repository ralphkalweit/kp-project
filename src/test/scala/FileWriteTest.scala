import sudoku.SudokuIO.{getFileContent, getGrid, getString, saveSudoku}
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.{Files, Paths}
import scala.util.Using
import java.io.PrintWriter

class FileWriteTest extends AnyFunSuite {

  test("get String from grid") {
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    // TODO initial string can contain spaces more that will be removed when using getString
    // logical sudoku stays the same but spaces could be helpful for readability of n x ns with n>9
    assert(sudokuString == getString(getGrid(sudokuString)))

    val sudokuString2 =
      "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 2" // contains an error
    assert(sudokuString2 == getString(getGrid(sudokuString2)))
  }

  test("Save file to valid path (and read it from there)") {
    val path = "test.sudoku"
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val sudoku = getGrid(sudokuString)

    saveSudoku(path, sudoku)
    assert(sudokuString == getFileContent(path))

    val filePath2 = Paths.get(path)
    if (Files.exists(filePath2)) {
      Files.delete(filePath2)
    }
  }
}
