object Test {
  object Twice {
    def unapply(x: Int): Option[(Int, Int)] = if (x % 2 == 0) Some((x / 2, x / 2)) else None
  }
  
  def main(args: Array[String]): Unit = {
    84 match {
      case Twice(x, y) => System.out.println(x)
      case _ => 
    }
  }
}