package position

import position.ICodage._
import position.PieceType._
import position.Roques._
import position.TypeDeCoups._


import scala.Array._
import scala.collection.mutable._

class GPositionS extends A with TGPositionS {
  val roques = Roques.roques
  val R = new Roques
  var caseEP = 0
  var gp: GPositionS = _
  var side = 0
  var coupsvalides: ListBuffer[GCoups] = _
  var estEnEchec = false
  var caseO = 0
  var recherchePionAttaqueRoque = false
  var coupsAttaqueRoque: ListBuffer[GCoups] = _

  def this(p: GPositionS, pCouleur: Int) {
    this()
    moves = new ListBuffer[GCoups]
    gp = p
    couleur = pCouleur
    etats = gp.etats
  }

  def this(recherchePionAttaqueRoque: Boolean) {
    this()
    recherchePionAttaqueRoque_$eq(recherchePionAttaqueRoque)
  }


  def ajouterCoupsEP(): Unit = {
    val caseEP = gp.caseEP
    if (caseEP != PAS_DE_CASE) {
      pionEstOuest(caseEP, est)
      pionEstOuest(caseEP, ouest)
    }
  }

  def pionEstOuest(caseEP: Int, estouest: Int): Unit = {
    val caseEstOuest = caseEP + couleur * nord + estouest
    if (pionDeCouleur(caseEstOuest, couleur))
      add(new GCoups(couleur * PION, caseEstOuest, caseEP, 0, 0, 0, EnPassant, 0))
  }


  def coupsEnEchec(): ListBuffer[GCoups] = {
    val aRetirer = new ListBuffer[GCoups]
    // var caseRoiCouleur = 0
    moves.foreach { (coups: GCoups) => {
      val positionSimul = fPositionSimul(coups, couleur)
      val caseRoiCouleur = pCaseRoi(positionSimul, couleur)
      val pseudoCoupsPosSimul = new GPositionS(true).pseudoC(positionSimul, -couleur)
      estEnEchec_$eq(fAttaque(caseRoiCouleur, -1, -1, pseudoCoupsPosSimul))
      if (estEnEchec) aRetirer += coups
    }
    }
    aRetirer
  }

  def fPositionSimul(m: GCoups, couleur: Int) = {
    val p = new GPositionS
    copy(etats, 0, p.etats, 0, NB_CELLULES)
    val O = m.caseO
    val X = m.caseX
    val t = m.getTypeDeCoups
    val piecePromotion = m.getPiecePromotion
    t match {
      case Deplacement | Prise | EnPassant =>
        e(p, X, O)
        e(p, O)
        if (t eq EnPassant) e(p, X + nord * couleur)
      case Promotion =>
        p.etats(X) = piecePromotion
        e(p, O)
      case _ =>
    }
    p
  }

  def exec(m: GCoups, ug: UndoGCoups): Boolean = {
    copy(etats, 0, ug.etats, 0, NB_CELLULES)
    ug.setKQkq(roques)
    ug.caseEP_$eq(caseEP)
    val O = m.caseO
    val X = m.caseX
    val p = m.piece
    val t = m.type_de_coups
    caseEP_$eq(-1)
    R.side = side
    if (typePiece(p) == PION && abs(X - O) == nord - sud) {
      caseEP_$eq(if (side == NOIR) X + 12 else X - 12)
    }
    t match {
      // ! attention repeter code pour Deplacement et Prise
      case Deplacement =>
        e(X, O)
        e(O)
        valideDroitRoque(m)
      case Prise =>
        e(X, O)
        e(O)
        valideDroitRoque(m)
      case EnPassant =>
        e(X, O)
        e(O)
        e(X + nord * side)
      case Promotion =>
        etats(X) = m.piecePromotion
        e(O)
      case Roque =>
        e(X, O)
        e(O)
        e(m.caseXTour, m.caseOTour)
        e(m.caseOTour)
        R.unsetRoque()
      case _ =>
    }
    side = -side
    true
  }


  def valideDroitRoque(gcoups: GCoups) {
    val caseO = gcoups.caseO
    gcoups.getPiece match {
      case ROI =>
        R.unsetRoque()
      case TOUR =>
        if (caseO == caseTourH(side)) unsetK(side)
        if (caseO == caseTourA(side)) unsetQ(side)
      case _ =>
    }
    if (etats(caseTourA(side)) != side * TOUR || etats(caseRoi(side)) != side * ROI) unsetQ(side)
    if (etats(caseTourH(side)) != side * TOUR || etats(caseRoi(side)) != side * ROI) unsetK(side)
  }


  def rangFinal(caseX: Int) = {
    //   if (range(a1,h1).contains(caseX) && couleur==NOIR) true else
    //     range(a8,h8).contains(caseX) && couleur==BLANC
    if (caseX >= a1 && caseX <= h1 && couleur == NOIR) true
    else caseX >= a8 && caseX <= h8 && couleur == BLANC
  }

  def rangInitial(caseX: Int) = {
    //    if (range(98,105).contains(caseX) && couleur==NOIR) true else
    //      range(38,45).contains(caseX) && couleur==BLANC
    if (caseX >= 98 && caseX <= 105 && couleur == NOIR) true
    else caseX >= 38 && caseX <= 45 && couleur == BLANC
  }


  def pCaseRoi(p: GPositionS, couleur: Int)
  = CASES117.find(caseO => p.etats(caseO) == couleur * ROI).get


  def ajouterRoques(): Unit ={
    coupsAttaqueRoque = new GPositionS(true).pseudoC(gp, -couleur)
    itRoque.foreach(t => {
      val e = gp.etats
      val _c0 = o_o(t)(0)
      val _c1 = o_o(t)(1)
      val _c2 = o_o(t)(2)
      val _c3 = o_o(t)(3)
      val e_c4 = if (t == 1 || t == 3) e(o_o(t)(4)) else VIDE
      if (gp.roques(t))
        if (e(_c0) == couleur * ROI && e(_c2) == couleur * TOUR && e(_c3) == VIDE && e(_c1) == VIDE && e_c4 == VIDE && !fAttaque(_c0, _c3, _c1, coupsAttaqueRoque))
          moves += new GCoups(ROI, _c0, _c1, _c2, _c3, 0, Roque, 0)
    })

  }

  def pseudoC(gp: GPositionS, pCouleur: Int): ListBuffer[GCoups] = {
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
    moves
  }


  def pseudoCoups(etat: PieceType) {
    val it = etat.DIR_PIECE.iterator
    etat.glissant match {
      case `non_glissant` =>
        if (etat eq pion) pseudoCoups(recherchePionAttaqueRoque)
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


  def pseudoCoups(recherchePionAttaqueRoque: Boolean): Unit = {
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


  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean = {
    coups.foreach(coup => {
      if (coup.caseX == caseRoi || coup.caseX == F1ouF8 || coup.caseX == G1ouG8) return true
    }
    )
    false
  }

  def allMoves(): ListBuffer[GCoups] = {
    pseudoC(this, couleur)
    ajouterRoques()
    ajouterCoupsEP()
    moves --= coupsEnEchec
    moves
  }

  def coupsValides() = {
    val generateur = new GPositionS(this, side)
    coupsvalides = generateur.allMoves()
    estEnEchec = generateur.estEnEchec
    coupsvalides
  }

  def coupsValides(t: Int) = {
    val generateur = new GPositionS(this, t)
    coupsvalides = generateur.allMoves()
    estEnEchec = generateur.estEnEchec
    coupsvalides
  }

  def isInCheck(pCouleur: Int) = {
    coupsValides(pCouleur)
    estEnEchec
  }

  def unexec(ug: UndoGCoups) {
    copy(ug.etats, 0, etats, 0, NB_CELLULES)
    copy(ug.roques, 0, roques, 0, 4)
    caseEP_$eq(ug.caseEP)
    side = -side
  }


  def print: String = {
    var str: String = ""
    var e_str: String = null
    var Clr_str: String = null
    var rg: Int = 0
    for (e <- etats) {
      val piecetype = abs(e)
      Clr_str = if (piecetype == e) "B" else "N"
      e_str = piecetype match {
        case PION =>
          "P"
        case ROI =>
          "K"
        case VIDE =>
          " "
        case OUT =>
          "X"
        case _ =>
          STRING_PIECE(piecetype)
      }
      Clr_str = if ("X" == e_str) "X" else Clr_str
      Clr_str = if (" " == e_str) "_" else Clr_str
      str += e_str + Clr_str + " "
      rg += 1
      if (rg == 12) {
        str += '\n'
        rg = 0
      }
    }
    str
  }
}
