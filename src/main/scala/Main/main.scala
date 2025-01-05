package Main

import sudoku.Strategies.trySolveWithElimination
import sudoku.SudokuIO.{loadSudoku, saveSudoku}
import sudoku.SudokuValidation.isCompleteSudoku

@main def demo(): Unit =
  println("Starting demonstration!")
  demonstration()

def demonstration(): Unit = {

  val fileName = "9x9.sudoku"
  val fileNameEdited = fileName.replace(".", "_edited.")

  val sudoku: List[List[Option[Int]]] = try {
    val s = loadSudoku(fileName)
    if (s.isEmpty) throw new RuntimeException()
    s
  } catch {
    case e: Exception =>
      println(s"Error: ${e.getMessage}")
      return
  }

  println(s"Sudoku loaded: $sudoku")

  if (isCompleteSudoku(sudoku)) {
    println("The Sudoku is already solved.")
    saveSudoku(fileNameEdited, sudoku)
  } else {
    val hopefullySolved = trySolveWithElimination(sudoku)
    // TODO allow user to choose different strategies here
    if (isCompleteSudoku(hopefullySolved)) {
      println("Sudoku solved!")
      saveSudoku("4x4_solved.sudoku", hopefullySolved)
    } else {
      println("Could not find a complete solution with this strategy. Please try a different one next time.")
      // todo print sudoku or ask to save it
    }

  }


}