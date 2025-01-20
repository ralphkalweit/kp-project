package sudoku

object SudokuTypes {
  type SudokuStringGrid = Vector[Vector[String]]
  //
  type SudokuCell = Option[Int]
  type SudokuLogicalVector = Vector[SudokuCell]
  type SudokuLogicalGrid = Vector[SudokuLogicalVector]
  //
  type SudokuEliminationCell = Set[Int]
  type SudokuEliminationList = Vector[SudokuEliminationCell]
  type SudokuEliminationMatrix = Vector[SudokuEliminationList]
}
