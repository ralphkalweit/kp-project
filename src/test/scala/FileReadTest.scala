import sudoku.SudokuIO.getFileContent
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.{Files, Paths}
import scala.util.Using
import java.io.PrintWriter

class FileReadTest extends AnyFunSuite {
  test("File Not Found") {
    try {
      getFileContent(
        "this_filepath_will_never_exist_ever_vern_ever_ever_never_ever"
      )
      assert(false)
    } catch {
      case e: Exception => assert(true)
      case _            => assert(false)
    }
  }

  test("Existing File can be read") {
    val expected = "a8 o_9wk;qaj25 p8e]yas;hjka"

    val filePath = "test_existing_file.txt"
    Using(new PrintWriter(filePath)) { writer =>
      writer.write(expected)
    }

    val actualContent = getFileContent(filePath)
    assert(actualContent == expected)

    val filePath2 = Paths.get(filePath)
    if (Files.exists(filePath2)) {
      Files.delete(filePath2)
    }
  }

  test("Empty File") {
    val empty = ""
    val filePath = "test_existing_file.txt"
    Using(new PrintWriter(filePath)) { writer =>
      writer.write(empty)
    }

    try {
      getFileContent(filePath)
    } catch {
      case e: Exception => assert(true)
      case _            => assert(false)
    }

    val filePath2 = Paths.get(filePath)
    if (Files.exists(filePath2)) {
      Files.delete(filePath2)
    }
  }
}
