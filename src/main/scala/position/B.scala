package position

import scala.collection.mutable.ListBuffer

trait B {
  // Unit
  def ajouterCoupsEP(): Unit

  def pionEstOuest(caseEP: Int, estouest: Int): Unit

  def valideDroitRoque(gcoups: GCoups): Unit

  def ajouterRoques(): Unit

  def select(t: Int): Unit

  def pseudoCoups(etat: PieceType): Unit

  def pseudoCoups(recherchePionAttaqueRoque: Boolean): Unit

  def diagonalePionAttaqueRoque(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int): Unit

  def diagonalePionPrise(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int): Unit

  def unexec(ug: UndoGCoups): Unit

  //ListBuffer
  def allMoves(): ListBuffer[GCoups]

  def coupsEnEchec(): ListBuffer[GCoups]

  def pseudoC(gp: GPositionS, pCouleur: Int): ListBuffer[GCoups]

  def coupsValides(): ListBuffer[GCoups]

  def coupsValides(t: Int): ListBuffer[GCoups]

  //Boolean
  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean

  def exec(m: GCoups, ug: UndoGCoups): Boolean

  def rangFinal(caseX: Int): Boolean

  def rangInitial(caseX: Int): Boolean

  def isInCheck(pCouleur: Int): Boolean

  //GPositionS
  def fPositionSimul(m: GCoups, couleur: Int): GPositionS

  //Int
  def pCaseRoi(p: GPositionS, couleur: Int): Int

  //String
  def print: String

}
