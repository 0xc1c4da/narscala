package com.narscala.grammar

import scala.collection.immutable

trait Statement {

}

case class RelationalStatement(subject: Object, copula: Int, predicate: Object) extends Statement {

}

case class OperationStatement(word: Word, terms: immutable.Seq[Object]) extends Statement {

}