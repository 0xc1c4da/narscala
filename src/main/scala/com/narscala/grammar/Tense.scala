package com.narscala.grammar

/* Tense
 *
 * Describes an Event in NAL-7
 * Internally represented as Int
 */
object Tense {
  
    val PAST    = 0     // :\: , \⇒ , NAL-7
    val PRESENT = 1     // :|: , |⇒ , NAL-7
    val FUTURE  = 2     // :/: , /⇒ , NAL-7

}