package com.narscala.grammar

import org.parboiled2._

sealed trait Sentence
case class Judgement(statement:Term, tense:Option[Tense], truth:Option[Truth]) extends Sentence
case class Question (statement:Term, tense:Option[Tense])                       extends Sentence
case class Desire   (statement:Term, tense:Option[Tense])                         extends Sentence
case class Goal     (statement:Term, truth:Option[Truth])                           extends Sentence