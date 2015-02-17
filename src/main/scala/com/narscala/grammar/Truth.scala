package com.narscala.grammar

import org.parboiled2._

case class Truth(frequency: Double, confidence: Option[Double]) {
    
    // TODO: Choose correct defaults
    // def this(frequency: Double) = this(frequency, 0.1)
    // def this() = this(0.1, 0.1)
}