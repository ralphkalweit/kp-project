package Main

import Main.SolverHelper.insertAtFirstBlank
import Main.SudokuIO.getString
import Main.SudokuValidation.{hasErrors, isCompleteSudoku}

import scala.annotation.tailrec

object Backtracking {

  private def expand(grid: List[List[Option[Int]]]): List[List[List[Option[Int]]]] = {
    val halla = (1 to grid.size).flatMap { candidate =>
      val newGrid = insertAtFirstBlank(grid, candidate)
      println(getString(newGrid))
      if (!hasErrors(newGrid)) Some(newGrid)
      else None
    }.toList
    println(s"halla: $halla")
    halla
  }

  @tailrec
  def backtracking(
      frontier: List[List[List[Option[Int]]]]
  ): Option[List[List[Option[Int]]]] = {
    println(s"Frontier: ${frontier.mkString(" ")}")
    frontier match {
      case currentGrid :: rest =>
        if (isCompleteSudoku(currentGrid)) {
          Some(currentGrid)
        } else {
          val nextStates = expand(currentGrid)
          // TODO add option to choose between DFS and BFS
          backtracking(nextStates ::: rest) //DFS
        }
      case Nil => None
    }

  }

}
