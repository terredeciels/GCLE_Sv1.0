package position

import scala.collection.mutable.ListBuffer

trait TGPositionS {

  def ajouterCoupsEP(): Unit

  def pionEstOuest(caseEP: Int, estouest: Int): Unit

  def getCoups(): ListBuffer[GCoups]

  def coupsEnEchec(): ListBuffer[GCoups]

  def fPositionSimul(m: GCoups, couleur: Int): GPositionS

  def exec(m: GCoups, ug: UndoGCoups): Boolean

  def valideDroitRoque(gcoups: GCoups): Unit

  def rangFinal(caseX: Int): Boolean

  def rangInitial(caseX: Int): Boolean
  def fRangInitial: (Int) => Boolean.type = (caseX: Int) => Boolean

  def pCaseRoi(p: GPositionS, couleur: Int): Int
  //def fCaseRoi: (GPositionS, Int) => Int.type = (p: GPositionS, couleur: Int) => Int
 // def fCaseRoi: (GPositionS, Int) => Int.type

  def ajouterRoques(): Unit

  def select(t: Int): Unit
  //def select: (Int) => Unit.type = (t: Int)=> Unit

  def pseudoC(gp: GPositionS, pCouleur: Int): ListBuffer[GCoups]

  def pseudoCoups(etat: PieceType): Unit

  def pseudoCoups(recherchePionAttaqueRoque: Boolean): Unit

  def diagonalePionAttaqueRoque(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int): Unit

  def diagonalePionPrise(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int): Unit

  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean

  def coupsValides(): ListBuffer[GCoups]

  def coupsValides(t: Int): ListBuffer[GCoups]

  def isInCheck(pCouleur: Int): Boolean

  def unexec(ug: UndoGCoups): Unit

  def print: String

}
