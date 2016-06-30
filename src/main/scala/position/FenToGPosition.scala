package position

import org.chesspresso.Chess
import org.chesspresso.position.Position
import position.ICodage._

import scala.Seq.range

object FenToGPosition {

  def toGPosition(fen: String): GPositionS = toGPosition(new Position(fen))

  private def toGPosition(position: Position) = {
    val gp = new GPositionS()
    val cp_etats = new Array[Int](NB_CASES)
    val etats = new Array[Int](NB_CELLULES)

    CASES64.foreach(caseO => cp_etats(caseO) = position.getStone(caseO))
    range(0, NB_CELLULES).foreach(caseO => etats(caseO) = OUT)

    for (index <- cp_etats.indices) {
      etats(CASES117(index)) = cp_etats(index)
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