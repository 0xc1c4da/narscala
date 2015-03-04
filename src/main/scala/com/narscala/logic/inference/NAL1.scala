package com.narscala.logic.inference

import scala.language.reflectiveCalls
import com.narscala.logic.inference.engine.Rule._

import com.narscala.logic.grammar._
import com.narscala.logic.semantics.TruthValue

object NAL1 {

    val rules = Set(

        /* Revision. Appendix B, 1.1
        * When the task is a judgment and contains neither tense nor dependent variable,
        * the system matches it with the existing judgments on the same statement.

        * If a matching judgment is found and the two judgments have distinct evidential bases,
        * the revision rule is applied to produce a new judgment with the same statement
        * and a truth-value calculated by Frev.
        
        * When the task is a goal, the same revision process is done to its desire-value.

        * 27th Feb, 2015 from Patrick Hammer on IRC
        * the thing with the tense is wrong in the book
        * it only captures the special case of ETERNAL tense
        * in tensed situation it is the following: before two tensed beliefs/goals can be revised,
        * one has to be projected to the occurence time of the other
        * the most elegant way to say when revision can happen is: both beliefs/goals share their occurence time (no matter if ETERNAL or not)
        * ehm have equal occurence time (sharing a number sounds strange :D )
        * projection is the way to achieve equal occurence time in case of both are tensed.
        * eternalization is the way to achieve it in case of one is tensed and the other isnt
        * a.occurenceTime = b.occurenceTime
        * occurenceTime, comes from stamp
        * tense is something relative which depends on the current time.
        * for example an event will be "future" if its occurenceTime is bigger than memory.time()
        */

        rule ("Revision") let {
            val a = any(kindOf[Judgement]) // TODO: or Goal
            val b = any(kindOf[Judgement])

            when {
                (a.truth != b.truth) and (a.term == b.term)
                // TODO: and neither term is DependentVariable, recursively?
            } perform {
                // TODO: produce puts judgement into WorkingMemory and gets processed, causing infinite loop
                produce(
                    Judgement( a.term, None, Some(TruthValue.revision(a.truth.get, b.truth.get)) )
                )
                
            }
        }
    )
}