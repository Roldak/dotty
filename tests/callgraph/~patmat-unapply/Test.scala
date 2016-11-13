object Test {
  object Double {
    def unapply(x: Int): Option[Int] = if (x % 2 == 0) Some(x) else None
  }
  
  def main(args: Array[String]): Unit = {
    84 match {
      case Double(x) => System.out.println(x)
      case _ => 
    }
  }
}