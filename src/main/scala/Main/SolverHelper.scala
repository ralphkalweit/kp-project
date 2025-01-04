package Main

import Main.SudokuIO.areEqual
import Main.SudokuValidation.{
  getSudokuBlocks,
  getSudokuColumns,
  getSudokuRows,
  hasLogicalErrors,
  isCompleteSudoku
}

import scala.annotation.tailrec

object SolverHelper {

  def insertAtFirstBlank(
      sudoku: List[List[Option[Int]]],
      element: Int
  ): List[List[Option[Int]]] = {
    if (element > sudoku.length) sudoku
    else {
      val rowIndex = sudoku.indexWhere(_.contains(None))

      if (rowIndex < 0) sudoku
      else {
        val row = sudoku(rowIndex)
        val firstNoneIdx = row.indexOf(None)
        sudoku.updated(rowIndex, row.updated(firstNoneIdx, Some(element)))
      }
    }
  }

  def getEliminationMatrix(
      sudoku: List[List[Option[Int]]]
  ): List[List[Set[Int]]] = {
    val fullSet = (1 to sudoku.length).toSet
    sudoku.map(_.map {
      case Some(number) => Set(number)
      case None         => fullSet
    })
  }

  def getSudokuFromEliminationMatrix(
      eliminationMatrix: List[List[Set[Int]]]
  ): List[List[Option[Int]]] = {
    eliminationMatrix.map(_.map {
      case set if set.size == 1 =>
        Some(set.head)
      case _ => None
    })
  }

  def eliminateOneStep(
      possibilities: List[List[Set[Int]]]
  ): List[List[Set[Int]]] = {
    type T = List[List[Set[Int]]]
    val transformers = List[List[List[Set[Int]]] => List[List[Set[Int]]]](
      getSudokuRows,
      getSudokuColumns,
      getSudokuBlocks
    )

    transformers.foldLeft(possibilities) { (current, transform) =>
      transform(
        transform(current).map(eliminateListFromAllSingles)
      )
    }
  }

  @tailrec
  def eliminateRecursive(
      possibilities: List[List[Set[Int]]]
  ): List[List[Set[Int]]] = {
    val next = eliminateOneStep(possibilities)
    if (areEqual(next, possibilities)) possibilities
    else eliminateRecursive(next)
  }

  def eliminateListFromAllSingles(list: List[Set[Int]]): List[Set[Int]] = {

    val singles = list.collect {
      case s if s.size == 1 => s.head
    }.toSet
    list.map(set => if (set.size > 1) set -- singles else set)
  }

  def eliminationMatrixHasOpenDecisions(
      possibilities: List[List[Set[Int]]]
  ): Boolean = {
    !possibilities.forall(_.forall(_.size == 1))
  }

  def eliminationMatrixIsSolved(
      possibilities: List[List[Set[Int]]]
  ): Boolean = {
    val sudoku = getSudokuFromEliminationMatrix(possibilities)
    isCompleteSudoku(sudoku) && !hasLogicalErrors(sudoku)
  }

}
