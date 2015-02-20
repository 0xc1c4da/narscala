package com.narscala.grammar

import scala.collection.immutable.Seq

trait Statement extends Term
case class RelationalStatement(subject: Object, copula: Copula, predicate: Object)  extends Statement
case class OperationStatement(word: Word, terms: Seq[Object])                       extends Statement
case class TermStatement(term: Term)                                                extends Statement