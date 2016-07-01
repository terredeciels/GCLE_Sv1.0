package position

import scala.collection.mutable.ListBuffer
import ICodage._

class Gen() extends EGen {


  def allMoves(gp: GPosition, side: Int) :ListBuffer[GCoups] ={
    moves = new ListBuffer[GCoups]
    etats = gp.etats
    couleur = pCouleur

    CASES117.foreach { case s =>
      if (pieceQuiALeTrait(s)) {
        val etat = pieceType(etats(s))
        caseO = s
        pseudoCoups(etat)
      }
    }
    ajouterRoques()
    ajouterCoupsEP()
    moves --= coupsEnEchec
    moves

  }

}
