package dotty.tools.dotc.transform.linker.callgraph

import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Types.{PolyType, Type}

case class Cast(from: Type, to: Type)(implicit ctx: Context) {

  assert(!from.widenDealias.isInstanceOf[PolyType], from.widenDealias)
  assert(!to.widenDealias.isInstanceOf[PolyType], to.widenDealias)

  override def equals(other: Any): Boolean = {
    other match {
      case Cast(a, b) =>
        a =:= from && b =:= to
      case _ => false
    }
  }

  override def hashCode(): Int =
    from.typeSymbol.hashCode() * 31 + to.typeSymbol.hashCode()

  override def toString: String = s"Cast($from, $to)"
}