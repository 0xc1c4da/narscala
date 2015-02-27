package com.narscala.logic.semantics

import com.narscala.logic.grammar.Truth
import com.narscala.logic.grammar.TruthDefaults

/* The Truth-Value functions of NAL.
 * Appendix C, Table C.3. 
 */
object TruthValue {

    lazy val no_confidence = new RuntimeException("Missing Confidence in TruthValue")
    
    /* Local inference
     */

    def revision (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            (t1.frequency * c1 * (1.00 - c2) + t2.frequency * c2 * (1.00 - c1)) / (c1 * (1.00 - c2) + c2 * (1.00 - c1)),
            Some(
                (c1 * (1.00 - c2) + c2 * (1.00 - c1)) / (c1 * (1.00 - c2) + c2 * (1.00 - c1) + (1.00 - c1) * (1.00 - c2))
            )
        )
    }

    def expectation_decision () = {}  // ???

    /* Immediate inference
     */

    def negation (t1:Truth) = {
        Truth(1.00 - t1.frequency, t1.confidence )
    }

    def conversion (t1:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        Truth(
            1.00,
            Some(t1.frequency * c1 / (t1.frequency * c1 + TruthDefaults.k))
        )
    }

    def contraposition (t1:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        Truth(
            0.00,
            Some( (1.00 - t1.frequency) * c1 / ((1.00 - t1.frequency) * c1 + TruthDefaults.k) )
        )
    }

    /* Strong syllogism
     */ 
    
    def deduction (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency * t2.frequency,
            Some( t1.frequency * c1 * t2.frequency * c2 )
        )
    }

    def analogy (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency * t2.frequency,
            Some( c1 * t2.frequency * c2 )
        )
    }

    def resemblence (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency * t2.frequency,
            Some(
               c1 * c2 * (t1.frequency + t2.frequency - t1.frequency * t2.frequency) 
            )
        )
    }

    /* Weak syllogism
     */

    def abduction (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t2.frequency,
            Some(
               t1.frequency * c1 * c2 / (t1.frequency * c1 * c2 + TruthDefaults.k)
            )
        )
    }

    def induction (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency,
            Some(
               c1 * t2.frequency * c2 / (c1 * t2.frequency * c2 + TruthDefaults.k)
            )
        )
    }

    def exemplification (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            1.00,
            Some(
               t1.frequency * c1 * t2.frequency * c2 / (t1.frequency * c1 * t2.frequency * c2 + TruthDefaults.k)
            )
        )
    }

    def comparison (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency * t2.frequency / (t1.frequency + t2.frequency - t1.frequency * t2.frequency),
            Some(
               c1 * c2 * (t1.frequency + t2.frequency - t1.frequency * t2.frequency) / (c1 * c2 * (t1.frequency + t2.frequency - t1.frequency * t2.frequency) + TruthDefaults.k)
            )
        )
    }

    /* Term Composition
     */
    def intersection (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency * t2.frequency,
            Some( c1 * c2 )
        )
    }

    def union (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency + t2.frequency - t1.frequency * t2.frequency,
            Some( c1 * c2 )
        )
    }

    def difference (t1:Truth, t2:Truth) = {
        val c1 = t1.confidence.getOrElse(throw no_confidence)
        val c2 = t2.confidence.getOrElse(throw no_confidence)
        Truth(
            t1.frequency * (1.00 - t2.frequency),
            Some( c1 * c2 )
        )
    }

}