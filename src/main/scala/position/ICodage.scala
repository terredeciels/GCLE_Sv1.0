package position

import java.util.Arrays._

object ICodage {
  val INFINI: Int = Integer.MAX_VALUE
  val PAS_DE_CASE: Int = -1
  val FEN_INITIALE: String = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
  val BLANC: Int = -1
  val NOIR: Int = 1
  val a1: Int = 26
  val h1: Int = 33
  val a8: Int = 110
  val h8: Int = 117
  val e1: Int = 30
  val f1: Int = 31
  val g1: Int = 32
  val d1: Int = 29
  val c1: Int = 28
  val b1: Int = 27
  val e8: Int = 114
  val f8: Int = 115
  val g8: Int = 116
  val d8: Int = 113
  val c8: Int = 112
  val b8: Int = 111
  val d7: Int = 101
  val e7: Int = 102
  val d2: Int = 41
  val e2: Int = 42
  val a2: Int = 38
  val h2: Int = 45
  val a7: Int = 98
  val h7: Int = 105
  val d4: Int = 65
  val e4: Int = 66
  val e6: Int = 90
  val f6: Int = 91
  val d5: Int = 77
  val f3: Int = 55
  val c2: Int = 40
  val c4: Int = 64
  val g7: Int = 104
  val g6: Int = 92
  val c3: Int = 52
  val e3: Int = 54
  val a6: Int = 86
  val c7: Int = 100
  val c5: Int = 76
  val b7: Int = 99
  val b6: Int = 87
  val f7: Int = 103
  val b4: Int = 63
  val e5: Int = 78
  val f5: Int = 79
  val a4: Int = 62
  val d6: Int = 89
  val ROI: Int = 6
  val CAVALIER: Int = 1
  val TOUR: Int = 3
  val FOU: Int = 2
  val DAME: Int = 4
  val PION: Int = 5
  val NB_CASES: Int = 64
  val NB_CELLULES: Int = 144
  val VIDE: Int = 0
  val OUT: Int = 9
  val glissant: Int = 1
  val non_glissant: Int = 0
  val CASES117: Array[Int] = Array(26, 27, 28, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42, 43, 44, 45, 50, 51, 52, 53, 54, 55, 56, 57, 62, 63, 64, 65, 66, 67, 68, 69, 74, 75, 76, 77, 78, 79, 80, 81, 86, 87, 88, 89, 90, 91, 92, 93, 98, 99, 100, 101, 102, 103, 104, 105, 110, 111, 112, 113, 114, 115, 116, 117)
  val CASES64: Array[Int] = Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63)
  val INDICECASES: Array[Int] = Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, -1, -1, -1, -1, 8, 9, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, 16, 17, 18, 19, 20, 21, 22, 23, -1, -1, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, 32, 33, 34, 35, 36, 37, 38, 39, -1, -1, -1, -1, 40, 41, 42, 43, 44, 45, 46, 47, -1, -1, -1, -1, 48, 49, 50, 51, 52, 53, 54, 55, -1, -1, -1, -1, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)
  val STRING_CASES: Array[String] = Array("a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8")
  val STRING_PIECE: Array[String] = Array("", "N", "B", "R", "Q")
  val nord: Int = +12
  val est: Int = -1
  val sud: Int = -12
  val ouest: Int = +1
  val nordest: Int = nord + est
  val nordouest: Int = nord + ouest
  val sudest: Int = sud + est
  val sudouest: Int = sud + ouest
  val DIR_CAVALIER = asList(2 * nord + est, 2 * nord + ouest, 2 * est + nord, 2 * est + sud, 2 * sud + est, 2 * sud + ouest, 2 * ouest + nord, 2 * ouest + sud)
  val DIR_DAME = asList(nord, nordest, est, sudest, sud, sudouest, ouest, nordouest)
  val DIR_ROI = asList(nord, nordest, est, sudest, sud, sudouest, ouest, nordouest)
  val DIR_FOU = asList(nordest, sudest, sudouest, nordouest)
  val DIR_TOUR = asList(nord, est, sud, ouest)
  val DIR_PION = asList(0)
}
