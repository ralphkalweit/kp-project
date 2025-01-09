package sudoku

object SudokuTypes {
  type SudokuStringGrid = Vector[Vector[String]]
  //
  type SudokuCell = Option[Int]
  type SudokuLogicalList = Vector[SudokuCell]
  type SudokuLogicalGrid = Vector[SudokuLogicalList]
  //
  type SudokuEliminationCell = Set[Int]
  type SudokuEliminationList = Vector[SudokuEliminationCell]
  type SudokuEliminationMatrix = Vector[SudokuEliminationList]
}
