package position

class UndoGCoups {
  var etats= new Array[Int](ICodage.NB_CELLULES)
  var roques = new Array[Boolean](4)
  var caseEP: Int = _


  def setKQkq(roq: Array[Boolean]) {
    roques(0) = roq(0)
    roques(1) = roq(1)
    roques(2) = roq(2)
    roques(3) = roq(3)
  }
}