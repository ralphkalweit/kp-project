package sudoku

import sudoku.SudokuIO.areEqual
import sudoku.SudokuTypes.{SudokuEliminationMatrix, SudokuLogicalGrid}
import sudoku.SudokuValidation.*
import sudoku.SudokuContextual.sudokuExtensions
import scala.annotation.tailrec

object SolverHelper {

  def insertAtFirstBlank(
      sudoku: SudokuLogicalGrid,
      element: Int
  ): SudokuLogicalGrid = {
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
      sudoku: SudokuLogicalGrid
  ): SudokuEliminationMatrix = {
    val fullSet = (1 to sudoku.length).toSet
    sudoku.map(_.map {
      case Some(number) => Set(number)
      case None         => fullSet
    })
  }

  def getSudokuFromEliminationMatrix(
      eliminationMatrix: SudokuEliminationMatrix
  ): SudokuLogicalGrid = {
    eliminationMatrix.map(_.map {
      case set if set.size == 1 =>
        Some(set.head)
      case _ => None
    })
  }

  def eliminateOneStep(
      possibilities: SudokuEliminationMatrix
  ): SudokuEliminationMatrix = {
    type T = SudokuEliminationMatrix
    val transformers =
      Vector[SudokuEliminationMatrix => SudokuEliminationMatrix](
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
      possibilities: SudokuEliminationMatrix
  ): SudokuEliminationMatrix = {
    val next = eliminateOneStep(possibilities)
    if (areEqual(next, possibilities)) possibilities
    else eliminateRecursive(next)
  }

  def eliminateListFromAllSingles(list: Vector[Set[Int]]): Vector[Set[Int]] = {
    val singles = list.collect {
      case s if s.size == 1 => s.head
    }.toSet
    list.map(set => if (set.size > 1) set -- singles else set)
  }

  def eliminationMatrixHasOpenDecisions(
      possibilities: SudokuEliminationMatrix
  ): Boolean = {
    !possibilities.forall(_.forall(_.size == 1))
  }

  def eliminationMatrixIsSolved(
      possibilities: SudokuEliminationMatrix
  ): Boolean = {
    val sudoku = getSudokuFromEliminationMatrix(possibilities)
    sudoku.isValidAndCompleteComplete
  }

  def isPartialSolution(
      part: SudokuLogicalGrid,
      complete: SudokuLogicalGrid
  ): Boolean = {
    if (part.isEmpty) return true
    if (part.length != complete.length) return false
    if (!part.isCorrect) return false

    part
      .zip(complete)
      .forall((leftRow, rightRow) =>
        leftRow
          .zip(rightRow)
          .forall((leftItem, rightItem) =>
            (leftItem, rightItem) match {
              case (Some(p), Some(c)) if p == c => true
              case (None, _)                    => true
              case (_, _)                       => false
            }
          )
      )

  }
}
