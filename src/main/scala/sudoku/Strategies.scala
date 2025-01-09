package sudoku

import sudoku.Backtracking.{
  backtrackingWithElimination,
  backtrackingWithoutElimination
}
import sudoku.SolverHelper.{
  eliminateRecursive,
  getEliminationMatrix,
  getSudokuFromEliminationMatrix
}
import sudoku.SudokuTypes.SudokuLogicalGrid

object Strategies {

  type StrategyFunctionType = SudokuLogicalGrid => SudokuLogicalGrid

  def trySolveWithElimination: StrategyFunctionType = (
    sudoku: SudokuLogicalGrid
  ) => {
    val possibilities = getEliminationMatrix(sudoku)
    val finalResult = eliminateRecursive(
      possibilities
    )
    getSudokuFromEliminationMatrix(finalResult)
  }

  private def backtrackingStrategy(
      sudoku: SudokuLogicalGrid,
      useElimination: Boolean,
      useDFS: Boolean
  ): SudokuLogicalGrid = {
    val finalResult =
      if (useElimination) backtrackingWithElimination(List(sudoku), useDFS)
      else backtrackingWithoutElimination(List(sudoku), useDFS)

    finalResult.getOrElse(sudoku)
  }

  def trySolveUsingBacktrackingWithoutElimination: StrategyFunctionType =
    (sudoku: SudokuLogicalGrid) => {
      backtrackingStrategy(sudoku, false, true)
    }

  def trySolveUsingBacktrackingWithElimination: StrategyFunctionType =
    (sudoku: SudokuLogicalGrid) => {
      backtrackingStrategy(sudoku, true, true)
    }

}
