package sudoku

object SudokuTypes {
  type SudokuStringGrid = Vector[Vector[String]]
  //
  type SudokuLogicalList = Vector[Option[Int]]
  type SudokuLogicalGrid = Vector[SudokuLogicalList]
  //
  type SudokuEliminationCell = Set[Int]
  type SudokuEliminationList = Vector[SudokuEliminationCell]
  type SudokuEliminationMatrix = Vector[SudokuEliminationList]
}
