package position

import position.ICodage._
import Roques._

object Roques {
  val roques= new Array[Boolean](4)
  var o_o = Array(Array(e1, g1, h1, f1), Array(e1, c1, a1, d1, b1), Array(e8, g8, h8, f8), Array(e8, c8, a8, d8, b8))

  def unsetKQ() ={
    roques(0) = false
    roques(1) = false
  }

  def unsetkq() {
    roques(2) = false
    roques(3) = false
  }

  def unsetK(color: Int) {
    if (color == BLANC) unsetK()
    else if (color == NOIR) unsetk()
  }

  def unsetK() ={
    roques(0) = false
  }

  def unsetk() {
    roques(2) = false
  }

  def unsetQ(color: Int) {
    if (color == BLANC) unsetQ()
    else if (color == NOIR) unsetq()
  }

  def unsetQ() {
    roques(1) = false
  }

  def unsetq() {
    roques(3) = false
  }

  def caseTourH(couleur: Int) = if (couleur == BLANC) h1 else h8

  def caseTourA(couleur: Int) = if (couleur == BLANC) a1 else a8

  def caseRoi(couleur: Int) = if (couleur == BLANC) e1 else e8
}

class Roques {

  var side = 0

  def unsetRoque()= {
    if (side == BLANC) unsetKQ()
    else if (side == NOIR) unsetkq()
  }
}