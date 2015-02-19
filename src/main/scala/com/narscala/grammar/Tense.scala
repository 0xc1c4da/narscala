package com.narscala.grammar

/* Tense
 *
 * Describes an Event in NAL-7
 */
sealed trait Tense
case class Past()       extends Tense
case class Present()    extends Tense
case class Future()     extends Tense