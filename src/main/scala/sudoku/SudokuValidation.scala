package sudoku

import sudoku.SudokuTypes.{SudokuLogicalGrid, SudokuLogicalList}

object SudokuValidation {

  def getSudokuRows[T](grid: List[List[T]]): List[List[T]] = {
    grid
  }

  def getSudokuColumns[T](
      grid: List[List[T]]
  ): List[List[T]] = {
    grid.transpose
  }

  def getSudokuBlocks[T](
      grid: List[List[T]]
  ): List[List[T]] = {
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
      } yield grid(row)(col)).toList
    }).toList
  }

  def getSudokuFromSudokuBlocks[T](
      grid: List[List[T]]
  ): List[List[T]] =
    getSudokuBlocks(grid)

  def isCompleteList(list: SudokuLogicalList): Boolean = {
    list.nonEmpty && !list.contains(None)
  }

  def hasLogicalErrors(grid: SudokuLogicalGrid): Boolean = {
    val regions =
      List(
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
        List(
          getSudokuRows(grid),
          getSudokuColumns(grid),
          getSudokuBlocks(
            grid
          )
        ).flatten.forall(isCompleteList)

      noMissing && !hasLogicalErrors(grid)
    }
  }

}
