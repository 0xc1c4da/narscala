package com.narscala.grammar

import org.parboiled2._

// TODO: TruthFactory, minimize memory usage of Truth values
// TODO: Handle Confidence default value

// object Truth {

//     val config = ConfigFactory.load()

//     def this(frequency: Double, confidence: Option[Double]) = {
//         this(frequency, confidence)
//         // val c:Option[Double] = Some(confidence.getOrElse(
//         //     config.getDouble("narscala.defaults.confidence")
//         // ))
        
//     }
// }

case class Truth(frequency: Double, confidence: Option[Double]) {
    require(frequency <= 1.0, "Frequency > 1.0")
    require(frequency >= 0.0, "Frequency < 0.0")
}