package position

import position.ICodage._
import position.TypeDeCoups._

object GCoups {

  def getString(coups: GCoups): String = {
    if (coups.piece == ROI && coups.caseO == e1 && coups.caseX == g1 ||
      coups.piece == ROI && coups.caseO == e8 && coups.caseX == g8) "O-O"
    else if (coups.piece == ROI && coups.caseO == e1 && coups.caseX == c1 ||
      coups.piece == ROI && coups.caseO == e8 && coups.caseX == c8) "O-O-O"
    else {
      var string_caseO: String = STRING_CASES(INDICECASES(coups.caseO))
      var string_caseX: String = STRING_CASES(INDICECASES(coups.caseX))
      if (coups.type_de_coups eq EnPassant) string_caseO + "x" + string_caseX
      else if (coups.type_de_coups eq Promotion) {
        var string_piecepromotion: String = STRING_PIECE(Math.abs(coups.piecePromotion))

        if (coups.pieceprise != 0) string_caseO + "x" + string_caseX + string_piecepromotion
        else string_caseO + "-" + string_caseX + string_piecepromotion
      }
      else if (coups.type_de_coups eq Prise) string_caseO + "x" + string_caseX
      else
        string_caseO + "-" + string_caseX
    }
  }
}

class GCoups(val piece: Int, val caseO: Int, val caseX: Int,
             val caseOTour: Int, val caseXTour: Int, val pieceprise: Int,
             val type_de_coups: TypeDeCoups, val piecePromotion: Int) {
  val PAS_DE_PIECE: Int = -1

  def getCaseO: Int = caseO

  def getCaseX: Int = caseX

  def getCaseOTour: Int = caseOTour

  def getCaseXTour: Int = caseXTour

  def getPiece: Int = piece

  def getPiecePrise: Int = pieceprise

  def getTypeDeCoups: TypeDeCoups = type_de_coups

  def getPiecePromotion: Int = piecePromotion

  override def toString: String = GCoups.getString(this)
}