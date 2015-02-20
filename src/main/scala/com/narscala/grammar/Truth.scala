package com.narscala.grammar

import com.typesafe.config.ConfigFactory
import org.parboiled2._

// TODO: TruthFactory, minimize memory usage of Truth values

object TruthDefaults {
    lazy val frequency:  Double = ConfigFactory.load().getDouble("narscala.defaults.frequency")
    lazy val confidence: Double = ConfigFactory.load().getDouble("narscala.defaults.confidence")
}

case class Truth(   frequency:  Double = TruthDefaults.frequency,
                    confidence: Option[Double] = Some(TruthDefaults.confidence)
    ) {
    require(frequency <= 1.0, "Frequency > 1.0 at " + frequency)
    require(frequency >= 0.0, "Frequency < 0.0 at " + frequency)
    require(confidence.getOrElse(TruthDefaults.confidence) <= 1.0, "Confidence > 1.0 at " + confidence.getOrElse(TruthDefaults.confidence))
    require(confidence.getOrElse(TruthDefaults.confidence) >= 0.0, "Confidence < 0.0 at " + confidence.getOrElse(TruthDefaults.confidence))
}