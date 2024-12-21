package Main

object CellValidation {

  private def isValidString(str: String, maxNumber: Int): Boolean = str match
    case s if s.matches("\\d+") =>
      val num = s.toInt
      num <= maxNumber
    case "_" => true
    case _ => false

  def listContainsOnlyValidStrings(strings: List[String], maxDim: Int): Boolean = strings.forall(* => isValidString(*, maxDim))

}
