package sudoku

import sudoku.SudokuTypes.{SudokuLogicalGrid, SudokuLogicalList}

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
    val size = grid.length
    val boxSize = math.sqrt(size).toInt
    require(
      boxSize * boxSize == size,
      "Grid must be of shape n^2 x n^2"
    ) // TODO maybe there is a good definition for some non-squared sudoku with special inner blocks (e.g. 5x2)??

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

  def isCompleteVector(list: SudokuLogicalList): Boolean = {
    list.nonEmpty && !list.contains(None)
  }

  def hasLogicalErrors(grid: SudokuLogicalGrid): Boolean = {
    val regions =
      Vector(
        getSudokuRows(grid),
        getSudokuColumns(grid),
        getSudokuBlocks(grid)
      ).flatten

    !regions.forall(list => {
      val noneCorrection = (list.count(_.isEmpty) - 1).max(0)
      list.toSet.size + noneCorrection == list.size
    })
  }

  def isCompleteSudoku(grid: SudokuLogicalGrid): Boolean = {
    if (grid.isEmpty) false
    else {
      val noMissing =
        Vector(
          getSudokuRows(grid),
          getSudokuColumns(grid),
          getSudokuBlocks(
            grid
          )
        ).flatten.forall(isCompleteVector)

      noMissing && !hasLogicalErrors(grid)
    }
  }

}
