package sudoku

import sudoku.SolverHelper.{isPartialSolution, getEliminationMatrix}
import sudoku.SudokuTypes.{SudokuEliminationMatrix, SudokuLogicalGrid}
import sudoku.SudokuValidation.{getSudokuBlocks, getSudokuColumns, getSudokuRows, hasLogicalErrors, isCompleteSudoku}
import sudoku.SudokuIO.getString

object SudokuContextual {

  given sudokuExtensions: AnyRef with
    extension (sudoku: SudokuLogicalGrid)
      def isCorrect: Boolean = !hasLogicalErrors(sudoku)
      def isValidAndCompleteComplete: Boolean = isCompleteSudoku(sudoku)
      def isPartialSolutionOf(fullSudoku: SudokuLogicalGrid): Boolean = isPartialSolution(sudoku, fullSudoku)
      //
      def getRows: SudokuLogicalGrid = getSudokuRows(sudoku)
      def getColumns: SudokuLogicalGrid = getSudokuColumns(sudoku)
      def getBlocks: SudokuLogicalGrid = getSudokuBlocks(sudoku)
      //
      def asString: String = getString(sudoku)
      def toEliminationMatrix: SudokuEliminationMatrix = getEliminationMatrix(sudoku)

}
