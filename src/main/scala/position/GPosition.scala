package position

import scala.collection.mutable.ListBuffer

class GPosition extends EGen {

  def coupsValides(side: Int): ListBuffer[GCoups] = {
    var gen = new Gen
    val allmoves = gen.regularMoves(rechPionAttacRoq = false, etats, side, couleur)
    gen = new Gen()
    val coupsAttaqueRoque = gen.castlesMoves(rechPionAttacRoq = true, etats, -couleur)
    allmoves --= coupsAttaqueRoque

    val g = new Gen
    allmoves.foreach { (coups: GCoups) => {
      val pos: Array[Int] = g.fPositionSimul(etats, coups, couleur)
      val caseRoi = g.pCaseRoi(pos, couleur)
      gen = new Gen()
      val coupsPos = gen.regularMoves(true, pos, side, -couleur)

      if (gen.fAttaque(caseRoi, -1, -1, coupsPos)) {
        allmoves -= coups
        estEnEchec = true
      }
    }

      allmoves
    }


  }
