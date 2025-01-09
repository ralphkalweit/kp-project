import sudoku.SudokuIO.{getFileContent, getLogicalGrid, getString, saveSudoku}
import org.scalatest.funsuite.AnyFunSuite
import sudoku.SudokuContextual.sudokuExtensions

import java.nio.file.{Files, Paths}
import java.io.IOException

class FileWriteTest extends AnyFunSuite {

  test("get String from grid") {
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    assert(sudokuString == getLogicalGrid(sudokuString).asString)

    val sudokuString2 =
      "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 2" // contains a logical error
    assert(sudokuString2 == getLogicalGrid(sudokuString2).asString)
  }

  test("Save file to valid path (and read it from there)") {
    val path = "test.sudoku"
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val sudoku = getLogicalGrid(sudokuString)

    try {
      saveSudoku(path, sudoku)
    } catch {
      case _: Throwable => assert(false)
    }
    assert(sudokuString == getFileContent(path))

    val filePath2 = Paths.get(path)
    if (Files.exists(filePath2)) {
      Files.delete(filePath2)
    }
  }

  test("save to invalid path") {
    val path = "./does-not-exist/test.sudoku"
    val sudokuString = "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
    val sudoku = getLogicalGrid(sudokuString)

    try {
      saveSudoku(path, sudoku) // expecting this throws an error
      assert(false)
    } catch {
      case e: IOException => assert(true)
      case _              => assert(false)
    }

    try {
      val filePath2 = Paths.get(path)
      if (Files.exists(filePath2)) {
        Files.delete(filePath2) // should not exist
      }
      assert(false, "Expected the file could not be saved")
    } catch {
      case e: Throwable => assert(true)
    }
  }
}
