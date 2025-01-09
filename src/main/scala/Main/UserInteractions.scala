package Main

import sudoku.SudokuIO.{loadSudoku, saveSudoku}
import sudoku.SudokuTypes.SudokuLogicalGrid

import java.io.FileNotFoundException
import scala.annotation.tailrec
import scala.io.StdIn

object UserInteractions {

  private def getUserInput(prompt: String): String = {
    val horizontalLine = "-" * (prompt.split("\n").last.length)
    println(prompt)
    println(horizontalLine)
    StdIn.readLine()
  }

  @tailrec
  def userInteractionSaveSudoku(
      initialFilePath: String,
      sudoku: SudokuLogicalGrid
  ): Unit = {
    val restartOption = "new"
    val resultFilePath = getUserInput(
      s"Please type in a path to save the sudoku.\nYou can press enter to overwrite the existing file,\ntype in `${restartOption}` to load a new file or enter `:q` to quit:"
    )
    resultFilePath match {
      case ":q"              =>
      case "exit"            =>
      case s"$restartOption" => demonstration()
      case ""                => saveSudoku(initialFilePath, sudoku)
      case any =>
        try {
          saveSudoku(any, sudoku)
        } catch {
          case e: Throwable => {
            println("Could not save using this filename. Please try again")
            userInteractionSaveSudoku(initialFilePath, sudoku)
          }
        }
    }
  }

  @tailrec
  def userInteractionLoadSudoku(
  ): (SudokuLogicalGrid, String) = {
    val chosenFilePath = getUserInput(
      s"Please type in the file name of the sudoku file to open it:"
    )
    chosenFilePath match {
      case path =>
        try {
          (loadSudoku(chosenFilePath), chosenFilePath)
        } catch {
          case _: FileNotFoundException => {
            println(
              "The file could not be found. Please try again with a different filename."
            )
            userInteractionLoadSudoku()
          }
          case e: Throwable => {
            println(e)
            println("An unexpected Error occurred. Please try again.")
            userInteractionLoadSudoku()
          }
        }
    }
  }

  @tailrec
  def userInteractionChooseFromOptions[T](
      prompt: String,
      options: Map[String, T],
      default: (String, Option[T]) = ("emptyDefaultAction", None)
  ): (String, Option[T]) = {
    val exitCommands = Set(":q", "exit")
    val specialCommands = options.keySet

    val input = getUserInput(prompt)

    input match {
      case exit if exitCommands.contains(exit)  => ("", None)
      case cmd if specialCommands.contains(cmd) => (cmd, Some(options(cmd)))
      case "!" => {
        println("You've just found an easter egg. Congrats!")
        ("", None)
      }
      case defaultAction if default._2.isDefined => default
      case invalid => {
        println(s"Invalid input: $invalid. Please try again.")
        userInteractionChooseFromOptions(prompt, options)
      }
    }
  }

}
