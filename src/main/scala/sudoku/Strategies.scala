package sudoku

import sudoku.SolverHelper.{eliminateRecursive, getEliminationMatrix, getSudokuFromEliminationMatrix}
import sudoku.SudokuTypes.SudokuLogicalGrid

object Strategies {

  def trySolveWithElimination(
      sudoku: SudokuLogicalGrid
  ): SudokuLogicalGrid = {
    val possibilities = getEliminationMatrix(sudoku)
    val done = eliminateRecursive(
      possibilities
    )
    getSudokuFromEliminationMatrix(done)
  }

}
