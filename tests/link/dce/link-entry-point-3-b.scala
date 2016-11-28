import scala.annotation.internal

object Test {
  def main(args: Array[String]): Unit = {
    val classLoader = Test.getClass.getClassLoader()

    try {
      val mainClass = classLoader.loadClass("Test")
      val mainMethod = mainClass.getMethod("dceTest")
      mainMethod.invoke(null);
    } catch {
      case e: java.lang.Exception => e.getCause.printStackTrace()
    }
  }

  @internal.link.AssertNotReachable
  @internal.link.DoNotDeadCodeEliminate
  def dceTest: Unit = {
    foo.Foo.bar()
    foo.Foo.foo()
  }
}

package foo {

  object Foo {

    bar()

    @scala.EntryPoint def foo(): Unit = System.out.println(42)

    @internal.link.AssertReachable def bar(): Unit = System.out.println(43)
  }

}