package position

import org.apache.commons.collections.iterators.ArrayIterator
import org.chesspresso.Chess
import org.chesspresso.position.Position
import position.ICodage._

object FenToGPosition {
  var cp_etats: Array[Int] = _
  def toGPosition(fen: String):GPositionS = toGPosition(new Position(fen))

  private def toGPosition(position: Position) = {
    val gp = new GPositionS()
    cp_etats = new Array[Int](NB_CASES)
    for (caseO <- CASES64) {
      cp_etats(caseO) = position.getStone(caseO)
    }
    val etats = new Array[Int](NB_CELLULES)
    var caseO = 0
    while (caseO < NB_CELLULES) {

        etats(caseO) = OUT


        caseO += 1
       // caseO - 1

    }
    val itetats = new ArrayIterator(cp_etats)
    var indice = 0
    while (itetats.hasNext) {
     // val e = itetats.next
      etats(CASES117(indice)) = itetats.next.asInstanceOf[Integer]
      indice += 1
    }
    gp.etats = etats
    gp.side = if (position.getToPlay == Chess.WHITE) BLANC else NOIR
    val cp_roques = position.getCastles
    gp.roques(0) = (2 & cp_roques) == 2
    gp.roques(1) = (1 & cp_roques) == 1
    gp.roques(2) = (8 & cp_roques) == 8
    gp.roques(3) = (4 & cp_roques) == 4
    gp.caseEP = if (position.getSqiEP == PAS_DE_CASE) -1
    else CASES117(position.getSqiEP)
    gp
  }

}