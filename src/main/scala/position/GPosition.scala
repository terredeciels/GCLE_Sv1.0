package position
import scala.collection.mutable.ListBuffer

class GPosition extends  EGen  {

  override def coupsValides(gp: GPosition, side: Int): ListBuffer[GCoups] = {
   // val moves: ListBuffer[GCoups] = new ListBuffer[GCoups]
    val gen = new Gen()
    moves = gen.allMoves(gp, side)
    estEnEchec = gen.estEnEchec
    moves
  }


}
