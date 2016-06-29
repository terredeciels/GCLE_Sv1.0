package position

import java.util
import position.ICodage._

object PieceType {

  val fou = new PieceType(FOU, "B", 350, DIR_FOU, 1)
  val roi = new PieceType(ROI, "K", 0, DIR_ROI, 0)
  val cavalier = new PieceType(CAVALIER, "N", 300, DIR_CAVALIER, 0)
  val pion = new PieceType(PION, "", 100, DIR_PION, 0)
  val dame = new PieceType(DAME, "Q", 1000, DIR_DAME, 1)
  val tour = new PieceType(TOUR, "R", 550, DIR_TOUR, 1)

  def getValue(type_de_piece: Int): Int = _PIECE_TYPE(type_de_piece).value

  def _PIECE_TYPE(type_de_piece: Int): PieceType = {
    type_de_piece match {
      case FOU =>
        fou
      case ROI =>
        roi
      case CAVALIER =>
        cavalier
      case PION =>
        pion
      case DAME =>
        dame
      case TOUR =>
        tour
      case _ =>
        null
    }
  }

}

class PieceType(var code: Int,
                var english_fen: String,
                var value: Int,
                var DIR_PIECE: util.List[Int],
                var glissant: Int) {

  def getDIR_PIECE = DIR_PIECE

}