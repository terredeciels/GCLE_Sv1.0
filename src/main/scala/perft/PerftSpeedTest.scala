package perft

import java.io.IOException

import position._

import scala.collection.Iterator
import scala.collection.mutable.ListBuffer

object PerftSpeedTest {
  @throws[IOException]
  def main(args: Array[String]) {
    perftTest
  }

  def perftTest() {
    val f: String = ICodage.FEN_INITIALE
    val gp: GPositionS = FenToGPosition.toGPosition(f)
    val max_depth: Int = 6
    val t0: Double = System.nanoTime
    var depth: Int = 1
    while (depth <= max_depth) {
      {
        val res = perft(gp, depth)
        val t1: Double = System.nanoTime
        System.out.println("Depth " + depth + " : " + (t1 - t0) / 1000000000 + " sec")
        System.out.println("Count = " + res.moveCount)
      }
      {
        depth += 1
      }
    }
  }

  def perft(gp: GPositionS, depth: Int): PerftResult = {
    val result: PerftResult = new PerftResult
    if (depth == 0) {
      result.moveCount += 1
      return result
    }
    val moves: ListBuffer[GCoups] = gp.getCoupsValides()
    val it: Iterator[GCoups] = moves.iterator
    while (it.hasNext) {
      {
        val ui: UndoGCoups = new UndoGCoups
        if (gp.exec(it.next, ui)) {
          val subPerft = perft(gp, depth - 1)
          gp.unexec(ui)
          result.moveCount += subPerft.moveCount
        }
      }
    }
    result
  }
}