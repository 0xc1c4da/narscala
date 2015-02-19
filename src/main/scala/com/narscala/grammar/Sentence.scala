package com.narscala.grammar

import org.parboiled2._

sealed trait Sentence
case class Judgement(statement:Statement, tense:Option[Tense], truth:Option[Truth]) extends Sentence
case class Question(statement:Statement, tense:Option[Tense])                       extends Sentence
case class Desire(statement:Statement, tense:Option[Tense])                         extends Sentence
case class Goal(statement:Statement, truth:Option[Truth])                           extends Sentence