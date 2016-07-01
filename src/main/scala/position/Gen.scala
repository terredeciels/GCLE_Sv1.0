package position

import position.ICodage._
import position.PieceType._
import position.Roques._
import position.TypeDeCoups._

import scala.Array._
import scala.collection.mutable.ListBuffer

class Gen() extends EGen {

  def regularMoves(rechPionAttacRoq: Boolean,
                   pEtats: Array[Int], side: Int, pCouleur: Int): ListBuffer[GCoups] = {
    val moves = new ListBuffer[GCoups]
    etats = pEtats
    couleur = pCouleur

    CASES117.foreach { caseO =>
      if (pieceQuiALeTrait(caseO)) {
        val etat = pieceType(etats(caseO))
        //caseO = s
        val it = etat.DIR_PIECE.iterator
        etat.glissant match {
          case `non_glissant` =>
            if (etat eq pion) coupsPion(caseO, rechPionAttacRoq)
            else while (it.hasNext) {
              val caseX = caseO + it.next
              val t = if (etats(caseX) == VIDE) Deplacement
              else if (pieceAdverse(caseX)) Prise else Null
              ajouterCoups(caseO, caseX, t)
            }
          case `glissant` =>
            while (it.hasNext) {
              val direction = it.next
              var caseX = caseO + direction
              var etatX = etats(caseX)
              while (etatX == VIDE) {
                ajouterCoups(caseO, caseX, Deplacement)
                caseX += direction
                etatX = etats(caseX)
              }
              if (pieceAdverse(caseX)) ajouterCoups(caseO, caseX, Prise)
            }
        }
      }
    }
    if (caseEP != PAS_DE_CASE) {
      var caseEstOuest: Int = caseEP + couleur * nordest
      if (pionDeCouleur(caseEstOuest, couleur))
        moves += new GCoups(couleur * PION, caseEstOuest, caseEP, 0, 0, 0, EnPassant, 0)
      caseEstOuest = caseEP + couleur * nordouest
      if (pionDeCouleur(caseEstOuest, couleur))
        moves += new GCoups(couleur * PION, caseEstOuest, caseEP, 0, 0, 0, EnPassant, 0)
    }

    moves
  }


  def coupsPion(caseO: Int, recherchePionAttaqueRoque: Boolean): Unit = {
    val NordSudSelonCouleur = if (couleur == BLANC) nord else sud
    var caseX = caseO + NordSudSelonCouleur
    if (etats(caseX) == VIDE) {
      if (rangFinal(caseX)) addPseudoCoupsPromotion(caseO, caseX, 0)
      else add(new GCoups(couleur * PION, caseO, caseX, 0, 0, 0, Deplacement, 0))

      if (rangInitial(caseO)) {
        caseX += NordSudSelonCouleur
        if (etats(caseX) == VIDE) add(new GCoups(couleur * PION, caseO, caseX, 0, 0, 0, Deplacement, 0))
      }
    }
    if (recherchePionAttaqueRoque) {
      diagonalePionAttaqueRoque(caseO, NordSudSelonCouleur, est)
      diagonalePionAttaqueRoque(caseO, NordSudSelonCouleur, ouest)
    } else {
      diagonalePionPrise(caseO, NordSudSelonCouleur, est)
      diagonalePionPrise(caseO, NordSudSelonCouleur, ouest)
    }
  }

  def diagonalePionAttaqueRoque(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int) {
    val caseX = caseO + NordSudSelonCouleur + estOuOuest
    if (etats(caseX) != OUT)
      add(new GCoups(couleur * PION, caseO, caseX, 0, 0, etats(caseX), Attaque, 0))
  }

  def diagonalePionPrise(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int) {
    val caseX = caseO + NordSudSelonCouleur + estOuOuest
    if (pieceAdverse(caseX)) {
      if (rangFinal(caseX)) addPseudoCoupsPromotion(caseO, caseX, etats(caseX))
      else add(new GCoups(couleur * PION, caseO, caseX, 0, 0, etats(caseX), Prise, 0))
    }
  }

  def castlesMoves(rechPionAttacRoq: Boolean, pEtat: Array[Int], couleur: Int): ListBuffer[GCoups] = {
    val coupsAttaqueRoque = new ListBuffer[GCoups]
    val e = pEtat
    itRoque.foreach(t => {
      val _c0 = o_o(t)(0)
      val _c1 = o_o(t)(1)
      val _c2 = o_o(t)(2)
      val _c3 = o_o(t)(3)
      val e_c4 = if (t == 1 || t == 3) e(o_o(t)(4)) else VIDE
      if (roques(t))
        if (e(_c0) == couleur * ROI && e(_c2) == couleur * TOUR && e(_c3) == VIDE && e(_c1) == VIDE && e_c4 == VIDE && !fAttaque(_c0, _c3, _c1, coupsAttaqueRoque))
          coupsAttaqueRoque += new GCoups(ROI, _c0, _c1, _c2, _c3, 0, Roque, 0)
    })
    coupsAttaqueRoque
  }

  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean = {
    coups.foreach(coup => {
      if (coup.caseX == caseRoi || coup.caseX == F1ouF8 || coup.caseX == G1ouG8) return true
    }
    )
    false
  }

  def fPositionSimul(pEtats: Array[Int], m: GCoups, couleur: Int): Array[Int] = {
    copy(etats, 0, pEtats, 0, NB_CELLULES)
    val O = m.caseO
    val X = m.caseX
    val t = m.getTypeDeCoups
    val piecePromotion = m.getPiecePromotion
    t match {
      case Deplacement | Prise | EnPassant =>
        e(pEtats, X, O)
        e(pEtats, O)
        if (t eq EnPassant) e(pEtats, X + nord * couleur)
      case Promotion =>
        pEtats(X) = piecePromotion
        e(pEtats, O)
      case _ =>
    }
    pEtats
  }

  def pCaseRoi(etats: Array[Int], couleur: Int)
  = CASES117.find(caseO => etats(caseO) == couleur * ROI).get

}
