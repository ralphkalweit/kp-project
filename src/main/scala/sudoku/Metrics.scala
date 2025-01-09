package sudoku

import sudoku.SudokuTypes.{SudokuLogicalGrid, SudokuLogicalList}
import sudoku.SudokuValidation.{
  getSudokuBlocks,
  getSudokuColumns,
  getSudokuRows
}

object Metrics {

  def measureGivens(grid: SudokuLogicalGrid): Int = {
    countGivensList(grid.flatten)
  }

  private val countGivensList: SudokuLogicalList => Int = _.count(_.nonEmpty)

  // TODO provide backtracking metrics here (e.g. needed steps)

  // TODO Constraint Propagation with {naked / hidden} single technique

}
