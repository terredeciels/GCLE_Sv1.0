package position

import scala.collection.mutable.ListBuffer
import ICodage._


trait T {
  var couleur :Int
  var etats : Array[Int]
  var side :Int
  //var moves: ListBuffer[GCoups]
  var estEnEchec : Boolean
  var caseEP:Int
  var R:Roques

 // def coupsValides(gp:GPosition):ListBuffer[GCoups]
  def coupsValides(gp:GPosition,side: Int):ListBuffer[GCoups]

}
