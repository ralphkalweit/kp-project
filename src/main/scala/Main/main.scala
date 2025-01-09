package Main

import Main.UserInteractions.{userInteractionChooseFromOptions, userInteractionLoadSudoku, userInteractionSaveSudoku}
import sudoku.Strategies.{StrategyFunctionType, trySolveUsingBacktrackingWithElimination, trySolveUsingBacktrackingWithoutElimination, trySolveWithElimination}
import sudoku.SudokuValidation.{hasLogicalErrors, isCompleteSudoku}
import sudoku.SudokuContextual.sudokuExtensions

@main def main(): Unit = {
  println("Starting demonstration!")
  demonstration()
}

def demonstration(): Unit = {

  val (sudoku, chosenFilePath) = userInteractionLoadSudoku()
  println(s"Sudoku loaded:\n${sudoku.asString}\n")
  if (!sudoku.isCorrect) {
    println("Sudoku has logical errors. Please try a different one.")
    demonstration()
  }

  if (sudoku.isValidAndCompleteComplete) {
    println("The Sudoku is already solved.")
    userInteractionSaveSudoku(chosenFilePath, sudoku)
  } else {
    given useDFS: Boolean = true

    val strategies: Map[String, StrategyFunctionType] = Map(
      "Elimination" -> trySolveWithElimination,
      "Backtracking With Elimination" -> trySolveUsingBacktrackingWithElimination,
      "Backtracking Without Elimination" -> trySolveUsingBacktrackingWithoutElimination
    )

    val separator = "`\n- `"
    val prompt = s"Choose a Strategy. Options:\n- `${strategies.keySet.mkString(separator)}`"
    val chosenStrategy = userInteractionChooseFromOptions[StrategyFunctionType](prompt, strategies)
    if (chosenStrategy._2.isEmpty) {
      return
    }

    println(s"Using ${chosenStrategy._1} to solve the sudoku...")
    val strategyFunction = chosenStrategy._2.get

    val hopefullySolved = strategyFunction(sudoku)
    if (isCompleteSudoku(hopefullySolved)) {
      println("Found a solution for the sudoku!")
    } else {
      println(
        "Could not find a complete solution with this strategy. Please try a different strategy next time."
      )
    }
    println(hopefullySolved.asString)
    userInteractionSaveSudoku(chosenFilePath, hopefullySolved)

  }
}