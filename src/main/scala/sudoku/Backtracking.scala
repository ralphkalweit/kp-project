package sudoku

import sudoku.SolverHelper.{
  eliminateRecursive,
  eliminationMatrixIsSolved,
  getEliminationMatrix,
  getSudokuFromEliminationMatrix,
  insertAtFirstBlank
}
import sudoku.SudokuTypes.{SudokuEliminationMatrix, SudokuLogicalGrid}
import sudoku.SudokuValidation.{hasLogicalErrors, isCompleteSudoku}
import sudoku.SudokuContextual.sudokuExtensions
import scala.annotation.tailrec

object Backtracking {

  private def expand(
      grid: SudokuLogicalGrid
  ): List[SudokuLogicalGrid] = {
    (1 to grid.length).flatMap { candidate =>
      val newGrid = insertAtFirstBlank(grid, candidate)
      if (newGrid.isCorrect) Some(newGrid)
      else None
    }.toList
  }

  @tailrec
  def backtrackingWithoutElimination(
      pendingCandidates: List[SudokuLogicalGrid]
  )(using useDFS: Boolean): Option[SudokuLogicalGrid] = {
    pendingCandidates match {
      case currentGrid :: rest =>
        if (isCompleteSudoku(currentGrid)) {
          Some(currentGrid)
        } else {
          val nextStates = expand(currentGrid)
          val newFrontier =
            if (useDFS) nextStates ::: rest else rest ::: nextStates
          backtrackingWithoutElimination(newFrontier)
        }
      case Nil => None
    }
  }

  private def hasContradiction(
      possibilities: SudokuEliminationMatrix
  ): Boolean =
    possibilities.exists(_.exists(_.isEmpty))

  @tailrec
  def backtrackingWithElimination(
      pendingCandidates: List[SudokuLogicalGrid],
  )(using useDFS: Boolean): Option[SudokuLogicalGrid] = {
    pendingCandidates match {
      case Nil => None
      case currentGrid :: rest =>
        val possibilities = currentGrid.toEliminationMatrix
        val eliminated = eliminateRecursive(possibilities)

        if (hasContradiction(eliminated)) {
          backtrackingWithElimination(rest)
        } else {
          if (eliminationMatrixIsSolved(eliminated)) {
            Some(getSudokuFromEliminationMatrix(eliminated))
          } else {
            val partiallySolved = getSudokuFromEliminationMatrix(eliminated)
            val nextStates = expand(partiallySolved)

            val newFrontier =
              if (useDFS) nextStates ::: rest else rest ::: nextStates

            backtrackingWithElimination(newFrontier)
          }
        }
    }
  }

}
