package sudoku

object CellValidation {

  private def isValidString(str: String, maxNumber: Int): Boolean = str match
    case s if s.matches("\\d+") =>
      val num = s.toInt
      num <= maxNumber && num > 0
    case "_" => true
    case _ => false

  def vectorContainsOnlyValidStrings(strings: Vector[String], maxDim: Int): Boolean = strings.forall(* => isValidString(*, maxDim))

}
