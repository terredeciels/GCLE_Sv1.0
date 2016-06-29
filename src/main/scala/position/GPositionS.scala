package position

import java.lang.System._

import org.apache.commons.lang3.Range
import org.apache.commons.lang3.Range._
import position.GPositionS._
import position.ICodage._
import position.PieceType._
import position.Roques._
import position.TypeDeCoups._

import scala.collection.mutable._

object GPositionS {
  def abs(x: Int): Int = {
    if (x < 0) -x
    else x
  }

  def typePiece(x: Int): Int = {
    if (x < 0) -x
    else x
  }
}

class GPositionS {
  val roques = Roques.roques
  val R = new Roques
  var etats = new Array[Int](ICodage.NB_CELLULES)
  var caseEP = 0
  var couleur = 0
  var pseudoCoups: ListBuffer[GCoups] = _
  var rang_final: Range[Integer] = _
  var rang_initial: Range[Integer] = _
  var gp: GPositionS = _
  var `trait` = 0
  var _halfmoveCount = 0
  var _fullmoveNumber = 0
  var coupsvalides: ListBuffer[GCoups] = _
  var estEnEchec = false
  var caseO = 0
  var recherchePionAttaqueRoque = false

  def this(g_position: GPositionS, couleur: Int) {
    this()
    setPseudoCoups(new ListBuffer[GCoups])
    gp_$eq(g_position)
    couleur_$eq(couleur)
    etats_$eq(gp.etats)
    range()
  }

  def this(recherchePionAttaqueRoque: Boolean) {
    this()
    this.recherchePionAttaqueRoque_$eq(recherchePionAttaqueRoque)
    range()
  }

  def e(p: GPositionS, co: Int, cx: Int) {
    p.etats(co) = p.etats(cx)
  }

  def e(p: GPositionS, co: Int) {
    p.etats(co) = ICodage.VIDE
  }

  def ajouterCoupsEP() {
    val caseEP = gp.caseEP
    if (caseEP != ICodage.PAS_DE_CASE) {
      pionEstOuest(caseEP, est)
      pionEstOuest(caseEP, ouest)
    }
  }

  def pionEstOuest(caseEP: Int, estouest: Int) {
    val caseEstOuest = caseEP + couleur * nord + estouest
    if (pionDeCouleur(caseEstOuest, couleur)) {
      add(new GCoups(couleur * ICodage.PION, caseEstOuest, caseEP, 0, 0, 0, TypeDeCoups.EnPassant, 0))
    }
  }

  def add(coups: GCoups) {
    pseudoCoups += coups
  }

  def getCoups = {
    pseudoC(this, couleur)
    ajouterRoques()
    ajouterCoupsEP()
    pseudoCoups --= coupsEnEchec
    pseudoCoups
  }

  def coupsEnEchec = {
    val aRetirer = new ListBuffer[GCoups]
    var caseRoiCouleur: Int = 0
    for (coups <- pseudoCoups) {
      val positionSimul = fPositionSimul(coups, couleur)
      caseRoiCouleur = fCaseRoi(positionSimul, couleur)
      val pseudoCoupsPosSimul = new GPositionS(true).pseudoC(positionSimul, -couleur)
      estEnEchec_$eq(fAttaque(caseRoiCouleur, -1, -1, pseudoCoupsPosSimul))
      if (estEnEchec) {
        aRetirer += coups
      }
    }
    aRetirer
  }

  def fPositionSimul(m: GCoups, couleur: Int) = {
    val p = new GPositionS
    System.arraycopy(etats, 0, p.etats, 0, NB_CELLULES)
    val O: Int = m.caseO
    val X: Int = m.caseX
    val t: TypeDeCoups = m.getTypeDeCoups
    val piecePromotion = m.getPiecePromotion
    t match {
      case Deplacement | Prise | EnPassant =>
        e(p, X, O)
        e(p, O)
        if (t eq EnPassant) {
          e(p, X + nord * couleur)
        }
      case Promotion =>
        p.etats(X) = piecePromotion
        e(p, O)
      case _ =>
    }
    p
  }

  def exec(m: GCoups, ug: UndoGCoups): Boolean = {
    arraycopy(etats, 0, ug.etats, 0, ICodage.NB_CELLULES)
    ug.setKQkq(roques)
    ug.caseEP_$eq(caseEP)
    val O: Int = m.caseO
    val X: Int = m.caseX
    val p: Int = m.piece
    val t: TypeDeCoups = m.type_de_coups
    caseEP_$eq(-1)
    R.`trait` = `trait`
    if (typePiece(p) == PION && abs(X - O) == nord - sud) {
      caseEP_$eq(if (`trait` == NOIR) X + 12 else X - 12)
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
        e(X + nord * `trait`)
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
    `trait` = -`trait`
    true
  }


  def e(co: Int, cx: Int) {
    etats(co) = etats(cx)
  }

  def e(co: Int) {
    etats(co) = VIDE
  }

  def valideDroitRoque(gcoups: GCoups) {
    val caseO: Int = gcoups.caseO
    gcoups.getPiece match {
      case ROI =>
        R.unsetRoque()
      case TOUR =>
        if (caseO == caseTourH(`trait`)) {
          unsetK(`trait`)
        }
        if (caseO == caseTourA(`trait`)) {
          unsetQ(`trait`)
        }
      case _ =>
    }
    if (etats(caseTourA(`trait`)) != `trait` * TOUR || etats(caseRoi(`trait`)) != `trait` * ROI) unsetQ(`trait`)
    if (etats(caseTourH(`trait`)) != `trait` * TOUR || etats(caseRoi(`trait`)) != `trait` * ROI) unsetK(`trait`)
  }

  def pionDeCouleur(s: Int, couleur: Int) = {
    val typeDePiece = if (etats(s) < 0) -etats(s) else etats(s)
    val couleurPiece = if (etats(s) < 0) BLANC else NOIR
    typeDePiece == PION && couleurPiece == couleur
  }

  def range() {
    rang_final_$eq(if (couleur == NOIR) between(a1, h1) else between(a8, h8))
    rang_initial_$eq(if (couleur == NOIR) between(98, 105) else between(38, 45))
  }

  def fCaseRoi(position: GPositionS, couleur: Int) = {
    var caseRoi = OUT
    for (caseO <- CASES117) {
      val etatO = position.etats(caseO)
      val typeO = abs(etatO)
      if (typeO == ROI && etatO * couleur > 0) {
        caseRoi = caseO
        caseRoi
      }
    }
    caseRoi
  }

  def ajouterRoques() {
    val coupsAttaque = new GPositionS(true).pseudoC(gp, -couleur)
    val e = gp.etats
    var `type` = 0
    while (`type` < 4) {
      val _c0 = o_o(`type`)(0)
      val _c1 = o_o(`type`)(1)
      val _c2 = o_o(`type`)(2)
      val _c3 = o_o(`type`)(3)
      val e_c4 = if (`type` == 1 || `type` == 3) e(o_o(`type`)(4)) else VIDE
      if (gp.roques(`type`))
        if ((e(_c0) == couleur * ROI && e(_c2) == couleur * TOUR && e(_c3) == VIDE && e(_c1) == VIDE && e_c4 == VIDE) && !fAttaque(_c0, _c3, _c1, coupsAttaque))
          add(new GCoups(ROI, _c0, _c1, _c2, _c3, 0, Roque, 0))
      `type` += 1
    }
  }

  def pseudoC(gp: GPositionS, couleur: Int) = {
    setPseudoCoups(new ListBuffer[GCoups])
    etats_$eq(gp.etats)
    couleur_$eq(couleur)
    for (s <- CASES117) {
      if (pieceQuiALeTrait(s)) {
        val etat = _PIECE_TYPE(if (etats(s) < 0) -etats(s) else etats(s))
        caseO = s
        pseudoCoups(etat)
      }
    }
    pseudoCoups
  }

  def setPseudoCoups(pseudoc: ListBuffer[GCoups]) {
    pseudoCoups = pseudoc
  }

  def pieceQuiALeTrait(caseO: Int) = {
    val couleurPiece: Int = if (etats(caseO) < 0) BLANC else NOIR
    !(etats(caseO) == VIDE) && couleurPiece == couleur
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

  def ajouterCoups(caseO: Int, caseX: Int, type_de_coups: TypeDeCoups) {
    if (type_de_coups ne Null) pseudoCoups +=
      new GCoups(etats(caseO), caseO, caseX, 0, 0, etats(caseX), type_de_coups, 0)
  }

  def pseudoCoups(recherchePionAttaqueRoque: Boolean) {
    val NordSudSelonCouleur = if (couleur == BLANC) nord else sud
    var caseX = caseO + NordSudSelonCouleur
    if (etats(caseX) == VIDE) {
      if (rang_final.contains(caseX)) addPseudoCoupsPromotion(caseO, caseX, 0)
      else add(new GCoups(couleur * PION, caseO, caseX, 0, 0, 0, Deplacement, 0))

      if (rang_initial.contains(caseO)) {
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
    if (etats(caseX) != OUT) add(new GCoups(couleur * PION, caseO, caseX, 0, 0, etats(caseX), Attaque, 0))
  }

  def diagonalePionPrise(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int) {
    val caseX = caseO + NordSudSelonCouleur + estOuOuest
    if (pieceAdverse(caseX)) {
      if (rang_final.contains(caseX)) addPseudoCoupsPromotion(caseO, caseX, etats(caseX))
      else add(new GCoups(couleur * PION, caseO, caseX, 0, 0, etats(caseX), Prise, 0))
    }
  }

  def pieceAdverse(caseX: Int) = etats(caseX) != OUT && etats(caseX) * couleur < 0

  def addPseudoCoupsPromotion(caseO: Int, caseX: Int, pieceprise: Int) {
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, Promotion, couleur * FOU))
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, Promotion, couleur * CAVALIER))
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, Promotion, couleur * DAME))
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, Promotion, couleur * TOUR))
  }

  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean = {
    for (coup <- coups) {
      val caseX = coup.caseX
      if (caseX == caseRoi || caseX == F1ouF8 || caseX == G1ouG8) return true
    }
    false
  }

  def getCoupsValides() = {
    val generateur = new GPositionS(this, `trait`)
    coupsvalides = generateur.getCoups
    estEnEchec = generateur.estEnEchec
    coupsvalides
  }


  def isInCheck(pCouleur: Int) = {
    getCoupsValides(pCouleur)
    estEnEchec
  }

  def getCoupsValides(t: Int) = {
    val _trait = `trait`
    if (t == BLANC) {
      `trait`=BLANC
      val generateur = new GPositionS(this, `trait`)
      coupsvalides = generateur.getCoups
      estEnEchec = generateur.estEnEchec
      `trait`=_trait
      coupsvalides
    }
    else {
      `trait`=NOIR
      val generateur = new GPositionS(this, `trait`)
      coupsvalides = generateur.getCoups
      estEnEchec = generateur.estEnEchec
      `trait`=_trait
      coupsvalides
    }
  }

  def unexec(ug: UndoGCoups) {
    arraycopy(ug.etats, 0, etats, 0, NB_CELLULES)
    arraycopy(ug.roques, 0, roques, 0, 4)
    caseEP_$eq(ug.caseEP)
    `trait` = -`trait`
  }

  def getFullmoveNumber = _fullmoveNumber

  def getHalfmoveCount = _halfmoveCount

  def print: String = {
    var str: String = ""
    var e_str: String = null
    var Clr_str: String = null
    var rg: Int = 0
    for (e <- etats) {
      val piecetype: Int = Math.abs(e)
      Clr_str = if (piecetype == e) "B"
      else "N"
      piecetype match {
        case PION =>
          e_str = "P"
        case ROI =>
          e_str = "K"
        case VIDE =>
          e_str = " "
        case OUT =>
          e_str = "X"
        case _ =>
          e_str = STRING_PIECE(piecetype)
      }
      Clr_str = if ("X" == e_str) "X"
      else Clr_str
      Clr_str = if (" " == e_str) "_"
      else Clr_str
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
