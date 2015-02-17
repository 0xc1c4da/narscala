package com.narscala.grammar

import org.parboiled2._

case class Budget(priority: Double, durability: Option[Double]) {
    
    // TODO: Choose correct defaults
    // def this(priority: Double) = this(priority, 0.1)
    // def this() = this(0.1, 0.1)

}