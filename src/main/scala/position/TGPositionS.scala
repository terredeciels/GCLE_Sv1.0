package position

import scala.collection.mutable.ListBuffer

trait TGPositionS {

  def ajouterCoupsEP(): Unit

  def pionEstOuest(caseEP: Int, estouest: Int): Unit

  def valideDroitRoque(gcoups: GCoups): Unit

  def ajouterRoques(): Unit

  def pseudoCoups(etat: PieceType): Unit

  def pseudoCoups(recherchePionAttaqueRoque: Boolean): Unit

  def diagonalePionAttaqueRoque(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int): Unit

  def diagonalePionPrise(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int): Unit

  def unexec(ug: UndoGCoups): Unit

  def allMoves(): ListBuffer[GCoups]

  def coupsEnEchec(): ListBuffer[GCoups]

  def pseudoC(gp: Generateur, pCouleur: Int): ListBuffer[GCoups]

  def coupsValides(): ListBuffer[GCoups

  def coupsValides(t: Int): ListBuffer[GCoups]

  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean

  def exec(m: GCoups, ug: UndoGCoups): Boolean

  def rangFinal(caseX: Int): Boolean

  def rangInitial(caseX: Int): Boolean

  def isInCheck(pCouleur: Int): Boolean

  def fPositionSimul(m: GCoups, couleur: Int): Generateur

  def pCaseRoi(p: Generateur, couleur: Int): Int

  def print: String

}
