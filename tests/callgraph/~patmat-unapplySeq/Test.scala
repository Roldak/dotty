object Test {
  object Abs {
    def unapplySeq(xs: Seq[Int]): Option[Seq[Int]] = Some(xs.foldLeft(Seq[Int]())((seq, e) => seq :+ (if (e < 0) -e else e)))
  }
  
  def main(args: Array[String]): Unit = {
    List(-4, 31, -42) match {
      case Abs(4, 31, x) => System.out.println(x)
      case _ => 
    }
  }
}