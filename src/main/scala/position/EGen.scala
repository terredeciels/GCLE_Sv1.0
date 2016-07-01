package position

import ICodage._

abstract class EGen extends  T{

   var couleur: Int
   var caseEP: Int
   var R: Roques
   var side: Int
   var estEnEchec: Boolean
   var etats: Array[Int] 
  //override var moves: ListBuffer[GCoups] = _

  def pieceQuiALeTrait(caseO: Int) = {
    val couleurPiece = if (etats(caseO) < 0) BLANC else NOIR
    !(etats(caseO) == VIDE) && couleurPiece == couleur
  }
}
