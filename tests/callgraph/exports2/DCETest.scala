import scala.annotation.internal.DoNotDCE

object DCETest {
  @DoNotDCE def dceTest: Unit = {
    Test.shouldDCE(Foo.bar())
    Foo.foo()
  }
}

object Foo {
  @scala.export def foo(): Unit = System.out.println(42)
  def bar(): Unit = System.out.println(43)
}