package position

import position.ICodage._

object Roques {
  val roques: Array[Boolean] = new Array[Boolean](4)
  var o_o: Array[Array[Integer]] = Array(Array(ICodage.e1, ICodage.g1, ICodage.h1, ICodage.f1), Array(ICodage.e1, ICodage.c1, ICodage.a1, ICodage.d1, ICodage.b1), Array(ICodage.e8, ICodage.g8, ICodage.h8, ICodage.f8), Array(ICodage.e8, ICodage.c8, ICodage.a8, ICodage.d8, ICodage.b8))

  def unsetKQ() {
    roques(0) = false
    roques(1) = false
  }

  def unsetkq() {
    roques(2) = false
    roques(3) = false
  }

  def unsetK(color: Int) {
    if (color == BLANC) {
      unsetK()
    }
    else if (color == NOIR) {
      unsetk()
    }
  }

  def unsetK() {
    roques(0) = false
  }

  def unsetk() {
    roques(2) = false
  }

  def unsetQ(color: Int) {
    if (color == BLANC) {
      unsetQ()
    }
    else if (color == NOIR) {
      unsetq()
    }
  }

  def unsetQ() {
    roques(1) = false
  }

  def unsetq() {
    roques(3) = false
  }

  def caseTourH(couleur: Int): Int = if (couleur == BLANC) h1
  else h8

  def caseTourA(couleur: Int): Int = if (couleur == BLANC) a1
  else a8

  def caseRoi(couleur: Int): Int = if (couleur == BLANC) e1
  else e8
}

class Roques {

  var `trait`: Int = 0

  def unsetRoque() {
    if (`trait` == BLANC) {
      Roques.unsetKQ()
    }
    else if (`trait` == NOIR) {
      Roques.unsetkq()
    }
  }
}