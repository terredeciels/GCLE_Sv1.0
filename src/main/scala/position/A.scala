package position

import position.ICodage._
import position.TypeDeCoups._

import scala.collection.mutable.ListBuffer

class A {
  var couleur = 0
  var etats = new Array[Int](NB_CELLULES)
  var moves: ListBuffer[GCoups] = _
  var _halfmoveCount = 0
  var _fullmoveNumber = 0

  def getFullmoveNumber = _fullmoveNumber

  def getHalfmoveCount = _halfmoveCount

  def pieceAdverse(caseX: Int) = etats(caseX) != OUT && etats(caseX) * couleur < 0

  def e(p: GPositionS, co: Int, cx: Int) {
    p.etats(co) = p.etats(cx)
  }

  def e(p: GPositionS, co: Int) {
    p.etats(co) = VIDE
  }

  def e(co: Int, cx: Int) {
    etats(co) = etats(cx)
  }

  def e(co: Int) {
    etats(co) = VIDE
  }


  def pionDeCouleur(s: Int, couleur: Int) = {
    typeDePiece(s) == PION && couleurPiece(s) == couleur
  }

  def typeDePiece(s: Int) = if (etats(s) < 0) -etats(s) else etats(s)

  def couleurPiece(s: Int) = if (etats(s) < 0) BLANC else NOIR

  def typePiece(x: Int) = abs(x)

  def abs(x: Int) = if (x < 0) -x else x

  def ajouterCoups(caseO: Int, caseX: Int, type_de_coups: TypeDeCoups) {
    if (type_de_coups ne Null)
      moves += new GCoups(etats(caseO), caseO, caseX, 0, 0, etats(caseX), type_de_coups, 0)
  }

  def addPseudoCoupsPromotion(caseO: Int, caseX: Int, pieceprise: Int) {
    List(FOU, CAVALIER, DAME, TOUR).foreach(ppromo =>
      add(new GCoups(couleur * PION, caseO, caseX, 0, 0, pieceprise, Promotion, couleur * ppromo)))
  }

  def add(coups: GCoups) = moves += coups

  def pieceQuiALeTrait(caseO: Int) = {
    val couleurPiece = if (etats(caseO) < 0) BLANC else NOIR
    !(etats(caseO) == VIDE) && couleurPiece == couleur
  }
}
