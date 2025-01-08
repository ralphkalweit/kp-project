package Main

import sudoku.Strategies.{StrategyFunctionType, trySolveUsingBacktrackingWithElimination, trySolveUsingBacktrackingWithoutElimination, trySolveWithElimination}
import sudoku.SudokuIO.{getString, userInteractionLoadSudoku, userInteractionSaveSudoku}
import sudoku.SudokuValidation.isCompleteSudoku

@main def main(): Unit = {
  println("Starting demonstration!")
  demonstration()
}

def demonstration(): Unit = {

  val (sudoku, chosenFilePath) = userInteractionLoadSudoku()

  println(s"Sudoku loaded:\n${getString(sudoku)}\n")

  if (isCompleteSudoku(sudoku)) {
    println("The Sudoku is already solved.")
    userInteractionSaveSudoku(chosenFilePath, sudoku)
  } else {

    // TODO allow user to choose different strategies here
    val strategies: Map[String, StrategyFunctionType] = Map(
      "Elimination" -> trySolveWithElimination,
      "Backtracking Without Elimination" -> trySolveUsingBacktrackingWithoutElimination,
      "Backtracking With Elimination" -> trySolveUsingBacktrackingWithElimination
    )
    // TODO maybe use contextual abstraction here to provide a solver?

//    val chosenStrategy = "Elimination"
    val chosenStrategy = "Backtracking With Elimination"
    println(s"Using $chosenStrategy to solve the sudoku...")
    val hopefullySolved = strategies(chosenStrategy)(sudoku)
    if (isCompleteSudoku(hopefullySolved)) {
      println("Found a solution for the sudoku!")
    } else {
      println(
        "Could not find a complete solution with this strategy. Please try a different strategy next time."
      )
    }
    println(getString(hopefullySolved))
    userInteractionSaveSudoku(chosenFilePath, hopefullySolved)

  }
}