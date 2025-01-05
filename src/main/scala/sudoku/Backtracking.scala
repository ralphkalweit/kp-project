package sudoku

import sudoku.SolverHelper.insertAtFirstBlank
import sudoku.SudokuTypes.SudokuLogicalGrid
import sudoku.SudokuValidation.{hasLogicalErrors, isCompleteSudoku}

import scala.annotation.tailrec

object Backtracking {

  private def expand(
      grid: SudokuLogicalGrid
  ): List[SudokuLogicalGrid] = {
    (1 to grid.length).flatMap { candidate =>
      val newGrid = insertAtFirstBlank(grid, candidate)
      if (!hasLogicalErrors(newGrid)) Some(newGrid)
      else None
    }.toList
  }

  @tailrec
  def backtracking(
      frontier: List[SudokuLogicalGrid]
  ): Option[SudokuLogicalGrid] = {
    frontier match {
      case currentGrid :: rest =>
        if (isCompleteSudoku(currentGrid)) {
          Some(currentGrid)
        } else {
          val nextStates = expand(currentGrid)
          // TODO add option to choose between DFS and BFS
          backtracking(nextStates ::: rest) //DFS
        }
      case Nil => None
    }

  }

}
