package com.narscala.logic.semantics

import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success}
import org.specs2.mutable.Specification

import com.narscala.logic.grammar.Truth

/* Example numbers provided by NAL-TruthFunctions.xls
 */
class TruthValueSpec extends Specification {
    ConfigFactory.invalidateCaches()

    "TruthValue" should {

        val t  = Truth(0.60, Some(0.90))
        val t1 = Truth(0.90, Some(0.90)) 
        val t2 = Truth(0.80, Some(0.90))

        // One Premise Rules

        "Conversion" in {
            TruthValue.conversion(t) ===
            Truth(1, Some(0.35064935064935066))
        }

        "Negation" in {
            TruthValue.negation(t) ===
            Truth(0.40, Some(0.90))
        }

        "Contraposition" in {
            TruthValue.contraposition(t) ===
            Truth(0.0, Some(0.2647058823529412))
        }

        // Two Premise Rules

        "Revision" in {
            TruthValue.revision(t1, t2) ===
            Truth(0.85, Some(0.9473684210526316))
        }

        "Deduction" in {
            TruthValue.deduction(t1, t2) ===
            Truth(0.7200000000000001, Some(0.5832000000000002))
        }

        "Abduction" in {
            TruthValue.abduction(t1, t2) ===
            Truth(0.80, Some(0.42163100057836905))
        }

        "Induction" in {
            TruthValue.induction(t1, t2) ===
            Truth(0.90, Some(0.3932038834951457))
        }

        "Exemplification" in {
            TruthValue.exemplification(t1, t2) ===
            Truth(1.0, Some(0.36836786255684695))
        }

        "Comparison" in {
            TruthValue.comparison(t1, t2) ===
            Truth(0.7346938775510204, Some(0.4425242501951166))
        }

        "Analogy" in {
            TruthValue.analogy(t1, t2) ===
            Truth(0.7200000000000001, Some(0.6480000000000001))
        }

        "Resemblence" in {
            TruthValue.resemblence(t1, t2) ===
            Truth(0.7200000000000001, Some(0.7938000000000002))
        }

        "Intersection" in {
            TruthValue.intersection(t1, t2) ===
            Truth(0.7200000000000001, Some(0.81))
        }

        "Union" in {
            TruthValue.union(t1, t2) ===
            Truth(0.9800000000000001, Some(0.81))
        }

        "Difference" in {
            TruthValue.difference(t1, t2) ===
            Truth(0.17999999999999997, Some(0.81))
        }

    }
}