package com.narscala.grammar

import org.parboiled2._

trait Variable extends Term
case class IndependentVariable(word: Word) extends Variable
case class DependentVariable(word: Word) extends Variable
case class QueryVariable(word: Word) extends Variable