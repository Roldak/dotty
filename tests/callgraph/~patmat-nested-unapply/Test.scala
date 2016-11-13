object Test {
  object Double {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x / 2) else None
  }
  
  object Triple {
    def unapply(x: Int): Option[Int] = if (x % 3 == 0) Some(x / 3) else None
  }
  
  object In2 {
    def unapply(x: Int): Option[(Int, Int)] = if (x % 2 == 0) Some(x / 2, x / 2) else None
  }
  
  def main(args: Array[String]): Unit = {
    84 match {
      case In2(Double(x), Double(Triple(y))) => 
        System.out.println(x)
        System.out.println(y)
      case _ => 
    }
  }
}