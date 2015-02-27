package com.narscala.logic.grammar

import scala.collection.immutable.Seq

sealed trait CompoundTerm extends Term
case class ExtensionalSet(terms: Seq[Object]) extends CompoundTerm
case class IntensionalSet(terms: Seq[Object]) extends CompoundTerm

/** ConnectedSet
  *
  * CompoundTerm that relates a set of Terms by a TermConnector
  */
case class ConnectedSet(connector: TermConnector, terms: Seq[Object]) extends CompoundTerm

/** Describes the Relation between Terms in;
  * NAL-3, NAL-4, NAL-5 & NAL-7
  *
  * Can use mathematical notation in book or OpenNARS syntax
  */
sealed trait TermConnector
case class ExtensionalIntersection()  extends TermConnector // &  , ∩ , NAL-3
case class IntensionalIntersection()  extends TermConnector // |  , ∪ , NAL-3
case class ExtensionalDifference()    extends TermConnector // -  , - , NAL-3
case class IntensionalDifference()    extends TermConnector // ~  , ⊖ , NAL-3
case class Product()                  extends TermConnector // *  , × , NAL-4
case class ExtensionalImage()         extends TermConnector // /  , / , NAL-4
case class IntensionalImage()         extends TermConnector // \  , \ , NAL-4
case class Negation()                 extends TermConnector // -- , ¬ , NAL-5
case class Disjunction()              extends TermConnector // || , ∨ , NAL-5
case class Conjunction()              extends TermConnector // && , ∧ , NAL-5
case class SequentialConjunction()    extends TermConnector // &/ , , , NAL-7
case class ParallelConjunction()      extends TermConnector // &| , ; , NAL-7