package sudoku

import sudoku.Backtracking.{
  backtrackingWithElimination,
  backtrackingWithoutElimination
}
import sudoku.SudokuContextual.sudokuExtensions
import sudoku.SolverHelper.{
  eliminateRecursive,
  getSudokuFromEliminationMatrix
}
import sudoku.SudokuTypes.SudokuLogicalGrid

object Strategies {

  type StrategyFunctionType = SudokuLogicalGrid => SudokuLogicalGrid

  def trySolveWithElimination: StrategyFunctionType = (
    sudoku: SudokuLogicalGrid
  ) => {
    val possibilities = sudoku.toEliminationMatrix
    val finalResult = eliminateRecursive(
      possibilities
    )
    getSudokuFromEliminationMatrix(finalResult)
  }

  private def backtrackingStrategy(
      sudoku: SudokuLogicalGrid,
      useElimination: Boolean,
  )(using useDFS: Boolean): SudokuLogicalGrid = {
    val finalResult =
      if (useElimination) backtrackingWithElimination(List(sudoku))
      else backtrackingWithoutElimination(List(sudoku))

    finalResult.getOrElse(sudoku)
  }

  def trySolveUsingBacktrackingWithoutElimination(using usDFS: Boolean): StrategyFunctionType =
    (sudoku: SudokuLogicalGrid) => backtrackingStrategy(sudoku, false)(using usDFS)

  def trySolveUsingBacktrackingWithElimination(using usDFS: Boolean): StrategyFunctionType =
    (sudoku: SudokuLogicalGrid) => backtrackingStrategy(sudoku, true)(using usDFS)


}
