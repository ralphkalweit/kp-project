package sudoku

import sudoku.SudokuTypes.{SudokuLogicalGrid, SudokuLogicalVector}
import sudoku.SudokuContextual.sudokuExtensions

object SudokuValidation {

  def getSudokuRows[T](grid: Vector[Vector[T]]): Vector[Vector[T]] = {
    grid
  }

  def getSudokuColumns[T](
      grid: Vector[Vector[T]]
  ): Vector[Vector[T]] = {
    grid.transpose
  }

  def getSudokuBlocks[T](
      grid: Vector[Vector[T]]
  ): Vector[Vector[T]] = {
    val dimension = grid.length
    val boxSize = math.sqrt(dimension).toInt

    if (boxSize * boxSize != dimension)
      return grid // don't do blocks when we can't define them

    (for {
      rowBlock <- 0 until boxSize
      colBlock <- 0 until boxSize
    } yield {
      (for {
        row <- rowBlock * boxSize until (rowBlock + 1) * boxSize
        col <- colBlock * boxSize until (colBlock + 1) * boxSize
      } yield grid(row)(col)).toVector
    }).toVector
  }

  def getSudokuFromSudokuBlocks[T](
      grid: Vector[Vector[T]]
  ): Vector[Vector[T]] =
    getSudokuBlocks(grid)

  def isCompleteVector(vector: SudokuLogicalVector): Boolean = {
    vector.nonEmpty && !vector.contains(None)
  }

  def hasLogicalErrors(grid: SudokuLogicalGrid): Boolean = {
    val regions =
      Vector(
        grid.getRows,
        grid.getColumns,
        grid.getBlocks
      ).flatten

    !regions.forall(list => {
      val noneCorrection = (list.count(_.isEmpty) - 1).max(0)
      list.toSet.size + noneCorrection == list.size
    })
  }

  def isCompleteSudoku(sudoku: SudokuLogicalGrid): Boolean = {
    if (sudoku.isEmpty) false
    else {
      val noMissing =
        Vector(
          sudoku.getRows,
          sudoku.getColumns,
          sudoku.getBlocks
        ).flatten.forall(isCompleteVector)

      noMissing && sudoku.isCorrect
    }
  }

}
