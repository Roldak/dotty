object Test {
  def main(args: Array[String]): Unit = {
    System.out.println(List(4, 1, -5, 42, 31).sortWith(_ > _).head)
  }
}