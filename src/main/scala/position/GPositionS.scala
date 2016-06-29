package position

import java.lang.System._
import java.util
import org.apache.commons.lang3.Range
import org.apache.commons.lang3.Range._
import position.GPositionS._
import position.ICodage._
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
  val roques: Array[Boolean] = Roques.roques
  val R: Roques = new Roques
  val ROI: Int = 6
  val CAVALIER: Int = 1
  val TOUR: Int = 3
  val FOU: Int = 2
  val DAME: Int = 4
  val PION: Int = 5
  val VIDE: Int = 0
  val OUT: Int = 9
  var etats: Array[Int] = new Array[Int](ICodage.NB_CELLULES)
  var caseEP: Int = 0
  var couleur: Int = 0
  //var pseudoCoups: util.List[GCoups] = null
  var pseudoCoups: ListBuffer[GCoups] = null
  var rang_final: Range[Integer] = null
  var rang_initial: Range[Integer] = null
  var gp: GPositionS = _
  var `trait`: Int = 0
  var _halfmoveCount: Int = 0
  var _fullmoveNumber: Int = 0
  var coupsvalides: ListBuffer[GCoups] = null
  var estEnEchec: Boolean = false
  var caseO: Int = 0
  var recherchePionAttaqueRoque: Boolean = false

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
    val caseEP: Int = gp.caseEP
    if (caseEP != ICodage.PAS_DE_CASE) {
      pionEstOuest(caseEP, est)
      pionEstOuest(caseEP, ouest)
    }
  }

  def pionEstOuest(caseEP: Int, estouest: Int) {
    val caseEstOuest: Int = caseEP + couleur * nord + estouest
    if (pionDeCouleur(caseEstOuest, couleur)) {
      add(new GCoups(couleur * ICodage.PION, caseEstOuest, caseEP, 0, 0, 0, TypeDeCoups.EnPassant, 0))
    }
  }

  def add(coups: GCoups) {
    this.pseudoCoups+=coups
  }

  def getCoups: ListBuffer[GCoups] = {
    pseudoC(this, couleur)
    ajouterRoques()
    ajouterCoupsEP()
    pseudoCoups --= coupsEnEchec
    pseudoCoups
  }

  def coupsEnEchec: ListBuffer[GCoups] = {
    val aRetirer: ListBuffer[GCoups] = new ListBuffer[GCoups]
    var caseRoiCouleur: Int = 0
    import scala.collection.JavaConversions._
    for (coups <- pseudoCoups) {
      val positionSimul: GPositionS = fPositionSimul(coups, couleur)
      caseRoiCouleur = fCaseRoi(positionSimul, couleur)
      val pseudoCoupsPosSimul: ListBuffer[GCoups] = new GPositionS(true).pseudoC(positionSimul, -couleur)
      estEnEchec_$eq(fAttaque(caseRoiCouleur, -1, -1, pseudoCoupsPosSimul))
      if (estEnEchec) {
        aRetirer.add(coups)
      }
    }
    aRetirer
  }

  def fPositionSimul(m: GCoups, couleur: Int): GPositionS = {
    val p: GPositionS = new GPositionS
    System.arraycopy(etats, 0, p.etats, 0, ICodage.NB_CELLULES)
    val O: Int = m.caseO
    val X: Int = m.caseX
    val t: TypeDeCoups = m.getTypeDeCoups
    val piecePromotion: Int = m.getPiecePromotion
    t match {
      case Deplacement | Prise | EnPassant =>
        //      case Prise =>
        //      case EnPassant =>
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
    if (typePiece(p) == ICodage.PION && abs(X - O) == nord - sud) {
      caseEP_$eq(if (`trait` == ICodage.NOIR) X + 12
      else X - 12)
    }
    execSwitch(m, O, X, t)
    `trait` = -`trait`
    true
  }

  def execSwitch(m: GCoups, o: Int, x: Int, t: TypeDeCoups) {
    t match {
      // ! attention repeter code pour Deplacement et Prise
      case Deplacement =>
        e(x, o)
        e(o)
        valideDroitRoque(m)
      case Prise =>
        e(x, o)
        e(o)
        valideDroitRoque(m)
      case EnPassant =>
        e(x, o)
        e(o)
        e(x + nord * `trait`)
      case Promotion =>
        etats(x) = m.piecePromotion
        e(o)
      case Roque =>
        e(x, o)
        e(o)
        e(m.caseXTour, m.caseOTour)
        e(m.caseOTour)
        R.unsetRoque()
      case _ =>
    }
  }

  def e(co: Int, cx: Int) {
    etats(co) = etats(cx)
  }

  def e(co: Int) {
    etats(co) = ICodage.VIDE
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
    if (etats(caseTourA(`trait`)) != `trait` * TOUR || etats(caseRoi(`trait`)) != `trait` * ROI) {
      unsetQ(`trait`)
    }
    if (etats(caseTourH(`trait`)) != `trait` * TOUR || etats(caseRoi(`trait`)) != `trait` * ROI) {
      unsetK(`trait`)
    }
  }

  def pionDeCouleur(s: Int, couleur: Int) = {
    val typeDePiece: Int = if (etats(s) < 0) -etats(s)
    else etats(s)
    val couleurPiece: Int = if (etats(s) < 0) BLANC
    else NOIR
    typeDePiece == PION && couleurPiece == couleur
  }

  def range() {
    rang_final_$eq(if (couleur == NOIR) between(a1, h1)
    else between(a8, h8))
    rang_initial_$eq(if (couleur == NOIR) between(98, 105)
    else between(38, 45))
  }

  def fCaseRoi(position: GPositionS, couleur: Int) = {
    var caseRoi: Int = OUT
    var etatO: Int = 0
    var typeO: Int = 0
    for (caseO <- CASES117) {
      etatO = position.etats(caseO)
      typeO = Math.abs(etatO)
      if (typeO == ROI && etatO * couleur > 0) {
        caseRoi = caseO
        caseRoi
        // break //todo: break is not supported
      }
    }
    caseRoi
  }

  def ajouterRoques() {
    val coupsAttaque: ListBuffer[GCoups] = new GPositionS(true).pseudoC(gp, -couleur)
    val e: Array[Int] = gp.etats
    var `type`: Integer = 0
    while (`type` < 4) {

      val _c0: Int = Roques.o_o(`type`)(0)
      val _c1: Int = Roques.o_o(`type`)(1)
      val _c2: Int = Roques.o_o(`type`)(2)
      val _c3: Int = Roques.o_o(`type`)(3)
      val e_c4: Int = if (`type` == 1 || `type` == 3) e(Roques.o_o(`type`)(4))
      else VIDE
      if (gp.roques(`type`)) {
        if ((e(_c0) == couleur * ROI && e(_c2) == couleur * TOUR && e(_c3) == VIDE && e(_c1) == VIDE && e_c4 == VIDE) && !fAttaque(_c0, _c3, _c1, coupsAttaque)) {
          add(new GCoups(ROI, _c0, _c1, _c2, _c3, 0, TypeDeCoups.Roque, 0))
        }
      }
      `type` += 1
    }
  }

  def pseudoC(gp: GPositionS, couleur: Int): ListBuffer[GCoups] = {
    setPseudoCoups(new ListBuffer[GCoups])
    etats_$eq(gp.etats)
    couleur_$eq(couleur)
    for (s <- CASES117) {
      if (pieceQuiALeTrait(s)) {
        val etat: PieceType = PieceType._PIECE_TYPE(if (etats(s) < 0) -etats(s)
        else etats(s))
        caseO = s
        pseudoCoups(etat)
      }
    }
    pseudoCoups
  }

  def setPseudoCoups(pseudoc: ListBuffer[GCoups]) {
    this.pseudoCoups = pseudoc
  }

  def pieceQuiALeTrait(caseO: Int): Boolean = {
    val couleurPiece: Int = if (etats(caseO) < 0) BLANC
    else NOIR
    !(etats(caseO) == VIDE) && couleurPiece == couleur
  }

  def pseudoCoups(etat: PieceType) {
    val it: util.Iterator[Int] = etat.DIR_PIECE.iterator
    etat.glissant match {
      case 0 =>
        if (etat eq PieceType.pion) {
          pseudoCoups(recherchePionAttaqueRoque)
        }
        else {
          while (it.hasNext) {
            {
              val dir: Int = it.next
              val caseX: Int = caseO + dir
              val t: TypeDeCoups = if (etats(caseX) == VIDE) TypeDeCoups.Deplacement
              else if (pieceAdverse(caseX)) TypeDeCoups.Prise
              else TypeDeCoups.Null
              ajouterCoups(caseO, caseX, t)
            }
          }
        }
      // break //todo: break is not supported
      case 1 =>
        while (it.hasNext) {
          {
            val direction: Int = it.next
            var caseX: Int = caseO + direction
            var etatX: Int = etats(caseX)
            while (etatX == VIDE) {
              {
                ajouterCoups(caseO, caseX, TypeDeCoups.Deplacement)
                caseX += direction
                etatX = etats(caseX)
              }
            }
            if (pieceAdverse(caseX)) {
              ajouterCoups(caseO, caseX, TypeDeCoups.Prise)
            }
          }
        }
    }
  }

  def ajouterCoups(caseO: Int, caseX: Int, type_de_coups: TypeDeCoups) {
    if (type_de_coups ne TypeDeCoups.Null) pseudoCoups+=
      new GCoups(etats(caseO), caseO, caseX, 0, 0, etats(caseX), type_de_coups, 0)
  }

  def pseudoCoups(recherchePionAttaqueRoque: Boolean) {
    val NordSudSelonCouleur: Int = if (couleur == BLANC) nord
    else sud
    var caseX: Int = caseO + NordSudSelonCouleur
    if (etats(caseX) == VIDE) {
      if (rang_final.contains(caseX)) {
        addPseudoCoupsPromotion(caseO, caseX, 0)
      }
      else {
        add(new GCoups(couleur * PION, caseO, caseX, 0, 0, 0, TypeDeCoups.Deplacement, 0))
      }
      if (rang_initial.contains(caseO)) {
        caseX += NordSudSelonCouleur
        if (etats(caseX) == VIDE) {
          add(new GCoups(couleur * PION, caseO, caseX, 0, 0, 0, TypeDeCoups.Deplacement, 0))
        }
      }
    }
    if (!recherchePionAttaqueRoque) {
      diagonalePionPrise(caseO, NordSudSelonCouleur, est)
      diagonalePionPrise(caseO, NordSudSelonCouleur, ouest)
    }
    else {
      diagonalePionAttaqueRoque(caseO, NordSudSelonCouleur, est)
      diagonalePionAttaqueRoque(caseO, NordSudSelonCouleur, ouest)
    }
  }

  def diagonalePionAttaqueRoque(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int) {
    val caseX: Int = caseO + NordSudSelonCouleur + estOuOuest
    if (etats(caseX) != OUT) {
      add(new GCoups(couleur * PION, caseO, caseX, 0, 0, etats(caseX), TypeDeCoups.Attaque, 0))
    }
  }

  def diagonalePionPrise(caseO: Int, NordSudSelonCouleur: Int, estOuOuest: Int) {
    val caseX: Int = caseO + NordSudSelonCouleur + estOuOuest
    if (pieceAdverse(caseX)) {
      if (rang_final.contains(caseX)) {
        addPseudoCoupsPromotion(caseO, caseX, etats(caseX))
      }
      else {
        add(new GCoups(couleur * PION, caseO, caseX, 0, 0, etats(caseX), TypeDeCoups.Prise, 0))
      }
    }
  }

  def pieceAdverse(caseX: Int): Boolean = {
    etats(caseX) != OUT && etats(caseX) * couleur < 0
  }

  def addPseudoCoupsPromotion(caseO: Int, caseX: Int, pieceprise: Int) {
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, TypeDeCoups.Promotion, couleur * FOU))
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, TypeDeCoups.Promotion, couleur * CAVALIER))
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, TypeDeCoups.Promotion, couleur * DAME))
    add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, TypeDeCoups.Promotion, couleur * TOUR))
  }

  def fAttaque(caseRoi: Int, F1ouF8: Int, G1ouG8: Int, coups: ListBuffer[GCoups]): Boolean = {
    import scala.collection.JavaConversions._
    for (coup <- coups) {
      val caseX: Int = coup.caseX
      if (caseX == caseRoi || caseX == F1ouF8 || caseX == G1ouG8) return true
    }
    false
  }

  def getCoupsValides() = {
    val generateur: GPositionS = new GPositionS(this, `trait`)
    coupsvalides = generateur.getCoups
    estEnEchec = generateur.estEnEchec
    coupsvalides
  }



  def isInCheck(pCouleur: Int) = {
    getCoupsValides(pCouleur)
    estEnEchec
  }

  def getCoupsValides(t: Int): ListBuffer[GCoups] = {
    val _trait: Int = `trait`
    if (t == BLANC) {
      setTrait(BLANC)
      val generateur: GPositionS = new GPositionS(this, `trait`)
      coupsvalides = generateur.getCoups
      estEnEchec = generateur.estEnEchec
      setTrait(_trait)
      coupsvalides
    }
    else {
      setTrait(NOIR)
      val generateur: GPositionS = new GPositionS(this, `trait`)
      coupsvalides = generateur.getCoups
      estEnEchec = generateur.estEnEchec
      setTrait(_trait)
      coupsvalides
    }
  }

  def unexec(ug: UndoGCoups) {
    arraycopy(ug.etats, 0, etats, 0, NB_CELLULES)
    arraycopy(ug.roques, 0, roques, 0, 4)
    caseEP_$eq(ug.caseEP)
    `trait` = -`trait`
    // trait_$eq(-`trait`)
  }

  def getCaseEP: Int = caseEP

  def getEtats: Array[Int] = etats

  def getFullmoveNumber: Int = _fullmoveNumber

  def getHalfmoveCount: Int = _halfmoveCount

  def getTrait: Int = `trait`

  def setTrait(`trait`: Int) {
    this.`trait` = `trait`
  }

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
