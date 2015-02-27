package com.narscala.logic.grammar

/** Describes the Relation between Terms in;
  * NAL-1, NAL-2, NAL-5 & NAL-7
  *
  * Can use mathematical notation in book or OpenNARS syntax
  */
sealed trait Copula
case class Inheritance()              extends Copula  // --> , →   , NAL-1
case class Similarity()               extends Copula  // <-> , ↔   , NAL-2
case class Instance()                 extends Copula  // {-- , ◦→  , NAL-2
case class Property()                 extends Copula  // --] , →◦  , NAL-2
case class InstanceProperty()         extends Copula  // {-] , ◦→◦ , NAL-2
case class Implication()              extends Copula  // ==> , ⇒   , NAL-5
case class Equivalence()              extends Copula  // <=> , ⇔   , NAL-5
case class PredictiveImplication()    extends Copula  // =/> , /⇒  , NAL-7
case class ConcurrentImplication()    extends Copula  // =|> , |⇒  , NAL-7
case class RetrospectiveImplication() extends Copula  // =\> , \⇒  , NAL-7
case class PredictiveEquivalence()    extends Copula  // </> , /⇔  , NAL-7
case class ConcurrentEquivalence()    extends Copula  // <|> , |⇔  , NAL-7
