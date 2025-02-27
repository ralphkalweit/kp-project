package sudoku

import sudoku.SudokuIO.getCellString

object LinterStyler {

  def toStringWithLen(cellContent: Option[Int], uniformLen: Int): String = {
    val original = getCellString(cellContent)
    val remainingLen = uniformLen - original.length
    require(
      remainingLen >= 0,
      s"Number too big for maximum length $uniformLen. (Number: $cellContent)"
    )
    " " * remainingLen + original
  }

  def getUniformLength(dimensionSize: Int): Int = dimensionSize.toString.length

}
