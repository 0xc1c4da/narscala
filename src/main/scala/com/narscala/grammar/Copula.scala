package com.narscala.grammar

/** Describes the Relation between Terms in;
  * NAL-1, NAL-2, NAL-5 & NAL-7
  *
  * Can use mathematical notation in book or OpenNARS syntax
  * Internally represented as Int
  */
object Copula { 

    val INHERITANCE                 = 0  // --> , →   , NAL-1
    val SIMILARITY                  = 1  // <-> , ↔   , NAL-2
    val INSTANCE                    = 2  // {-- , ◦→  , NAL-2
    val PROPERTY                    = 3  // --] , →◦  , NAL-2
    val INSTANCE_PROPERTY           = 4  // {-] , ◦→◦ , NAL-2
    val IMPLICATION                 = 5  // ==> , ⇒   , NAL-5
    val EQUIVALENCE                 = 6  // <=> , ⇔   , NAL-5
    val PREDICTIVE_IMPLICATION      = 7  // =/> , /⇒  , NAL-7
    val CONCURRENT_IMPLICATION      = 8  // =|> , |⇒  , NAL-7
    val RETROSPECTIVE_IMPLICATION   = 9  // =\> , \⇒  , NAL-7
    val PREDICTIVE_EQUIVALENCE      = 10 // </> , /⇔  , NAL-7
    val CONCURRENT_EQUIVALENCE      = 11 // <|> , |⇔  , NAL-7

}