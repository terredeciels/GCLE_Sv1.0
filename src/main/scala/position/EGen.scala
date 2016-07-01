package position

import position.ICodage._

abstract class EGen {

  var couleur: Int
  var caseEP: Int
  var R: Roques
  var side: Int
  var estEnEchec: Boolean
  var etats: Array[Int]
  var recherchePionAttaqueRoque: Boolean

  def e(s: Array[Int], co: Int, cx: Int) {
    s(co) = s(cx)
  }

  def e(s: Array[Int], co: Int) {
    s(co) = VIDE
  }

  def e(co: Int, cx: Int) {
    etats(co) = etats(cx)
  }

  def e(co: Int) {
    etats(co) = VIDE
  }

  def pieceQuiALeTrait(caseO: Int) = {
    val couleurPiece = if (etats(caseO) < 0) BLANC else NOIR
    !(etats(caseO) == VIDE) && couleurPiece == couleur
  }

  def pieceAdverse(caseX: Int) = etats(caseX) != OUT && etats(caseX) * couleur < 0

  def typePiece(x: Int) = abs(x)

  def abs(x: Int) = if (x < 0) -x else x

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

  def pionDeCouleur(s: Int, couleur: Int) = typeDePiece(s) == PION && couleurPiece(s) == couleur

  def typeDePiece(s: Int) = if (etats(s) < 0) -etats(s) else etats(s)

  def couleurPiece(s: Int) = if (etats(s) < 0) BLANC else NOIR

}
