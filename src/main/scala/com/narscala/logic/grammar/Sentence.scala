package com.narscala.logic.grammar

import org.parboiled2._

sealed trait Sentence
case class Judgement(term:Term, tense:Option[Tense], truth:Option[Truth]) extends Sentence
case class Question (term:Term, tense:Option[Tense])                      extends Sentence
case class Desire   (term:Term, tense:Option[Tense])                      extends Sentence
case class Goal     (term:Term, truth:Option[Truth])                      extends Sentence