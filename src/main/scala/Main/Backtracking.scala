package Main

import Main.SolverHelper.insertAtFirstBlank
import Main.SudokuValidation.{hasErrors, isCompleteSudoku}

import scala.annotation.tailrec

object Backtracking {

  private def expand(grid: List[List[Option[Int]]]): List[List[List[Option[Int]]]] = {
    (1 to grid.length).flatMap { candidate =>
      val newGrid = insertAtFirstBlank(grid, candidate)
      if (!hasErrors(newGrid)) Some(newGrid)
      else None
    }.toList
  }

  @tailrec
  def backtracking(
      frontier: List[List[List[Option[Int]]]]
  ): Option[List[List[Option[Int]]]] = {
    frontier match {
      case currentGrid :: rest =>
        if (isCompleteSudoku(currentGrid)) {
          Some(currentGrid)
        } else {
          val nextStates = expand(currentGrid)
          // TODO add option t2o choose between DFS and BFS
          backtracking(nextStates ::: rest) //DFS
        }
      case Nil => None
    }

  }

}