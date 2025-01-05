package sudoku

import sudoku.SolverHelper.{
  eliminateRecursive,
  getEliminationMatrix,
  getSudokuFromEliminationMatrix
}

object Strategies {

  def trySolveWithElimination(
      sudoku: List[List[Option[Int]]]
  ): List[List[Option[Int]]] = {
    val possibilities = getEliminationMatrix(sudoku)
    val done = eliminateRecursive(
      possibilities
    )
    getSudokuFromEliminationMatrix(done)
  }

}
