package sudoku

object SudokuTypes {
  type SudokuStringGrid = List[List[String]]
  //
  type SudokuLogicalList = List[Option[Int]]
  type SudokuLogicalGrid = List[SudokuLogicalList]
  //
  type SudokuEliminationCell = Set[Int]
  type SudokuEliminationList = List[SudokuEliminationCell]
  type SudokuEliminationMatrix = List[SudokuEliminationList]
}
