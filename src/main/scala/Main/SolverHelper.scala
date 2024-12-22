package Main

object SolverHelper {

  def insertAtFirstBlank(
      sudoku: List[List[Option[Int]]],
      element: Int
  ): List[List[Option[Int]]] = {
    if element > sudoku.length then return sudoku

    val rowIndex = sudoku.indexWhere(_.contains(None))

    if rowIndex < 0 then sudoku
    else {
      val row = sudoku(rowIndex)
      val firstNoneIdx = row.indexOf(None)
      sudoku.updated(rowIndex, row.updated(firstNoneIdx, Some(element)))
    }

  }
}
