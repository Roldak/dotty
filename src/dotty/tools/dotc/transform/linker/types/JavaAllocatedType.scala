package dotty.tools.dotc.transform.linker.types

import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Hashable
import dotty.tools.dotc.core.Types.{SingletonType, Type}

class JavaAllocatedType(private[JavaAllocatedType] val u: Type) extends SingletonType {

  /** customized hash code of this type.
    * NotCached for uncached types. Cached types
    * compute hash and use it as the type's hashCode.
    */
  def hash: Int = {
    val underlying = u.hash
    if (underlying == Hashable.NotCached) Hashable.NotCached
    else if (underlying == Hashable.NotCached - 1) underlying
    else underlying + 1
  }

  override def hashCode(): Int = hash

  def underlying(implicit ctx: Context): Type = u

  override def equals(other: Any): Boolean = other match {
    case that: JavaAllocatedType => this.u == that.u
    case _ => false
  }

  override def toString: String = s"JavaAllocatedType($u)"
}