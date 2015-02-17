package com.narscala.grammar

import scala.collection.immutable

trait CompoundTerm extends Term {
    
}

case class ExtensionalSet(terms: immutable.Seq[Object]) extends CompoundTerm {

}

case class IntensionalSet(terms: immutable.Seq[Object]) extends CompoundTerm {

}

/** ConnectedSet
  *
  * CompoundTerm that relates a set of Terms by a TermConnector
  */
case class ConnectedSet(connector: Int, terms: immutable.Seq[Object]) extends CompoundTerm {

}

/** Describes the Relation between Terms in;
  * NAL-3, NAL-4, NAL-5 & NAL-7
  *
  * Can use mathematical notation in book or OpenNARS syntax
  * Internally represented as Int
  */
object TermConnector {

    val EXTENSIONAL_INTERSECTION = 0      // &  , ∩ , NAL-3
    val INTENSIONAL_INTERSECTION = 1      // |  , ∪ , NAL-3
    val EXTENSIONAL_DIFFERENCE   = 2      // -  , - , NAL-3
    val INTENSIONAL_DIFFERENCE   = 3      // ~  , ⊖ , NAL-3
    val PRODUCT                  = 4      // *  , × , NAL-4
    val EXTENSIONAL_IMAGE        = 5      // /  , / , NAL-4
    val INTENSIONAL_IMAGE        = 6      // \  , \ , NAL-4
    val NEGATION                 = 7      // -- , ¬ , NAL-5
    val DISJUNCTION              = 8      // || , ∨ , NAL-5
    val CONJUNCTION              = 9      // && , ∧ , NAL-5
    val SEQUENTIAL_CONJUNCTION   = 10     // &/ , , , NAL-7
    val PARALLEL_CONJUNCTION     = 11     // &| , ; , NAL-7

}