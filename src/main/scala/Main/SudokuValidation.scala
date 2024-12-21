package Main

object SudokuValidation {

  def getSudokuRows(grid: List[List[Option[Int]]]): List[List[Option[Int]]] = {
    grid
  }

  def getSudokuColumns(
      grid: List[List[Option[Int]]]
  ): List[List[Option[Int]]] = {
    grid.transpose
  }

  def getSudokuSquareBlocks(
      grid: List[List[Option[Int]]]
  ): List[List[Option[Int]]] = {
    grid.transpose
  }

  def getSudokuBlocks(
      grid: List[List[Option[Int]]]
  ): List[List[Option[Int]]] = {
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

  def isCompleteList(list: List[Option[Int]]): Boolean = {
    list.nonEmpty && !list.contains(None)
  }

  def hasErrors(grid: List[List[Option[Int]]]): Boolean = {
    val regions =
      List(
        getSudokuRows(grid),
        getSudokuColumns(grid),
        getSudokuBlocks(
          grid
        )
      ).flatten

    !regions.forall(list => {
      val noneCorrection = (list.count(_.isEmpty) - 1).max(0)
      list.toSet.size + noneCorrection == list.size
    })
  }

  def isCompleteSudoku(grid: List[List[Option[Int]]]): Boolean = {
    val noMissing =
      List(
        getSudokuRows(grid),
        getSudokuColumns(grid),
        getSudokuBlocks(
          grid
        )
      ).flatten.forall(isCompleteList)

    val unique = !hasErrors(grid)

    noMissing && unique
  }

}
