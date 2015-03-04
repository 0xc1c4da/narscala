package com.narscala.logic.inference.engine.util

import scala.language.implicitConversions


object Func {

  implicit def toListHelper[A](l: List[A]) = new {
    def +?[B <: A] (item: Option[B]) = item match {
      case Some(b) => b :: l
      case None => l
    }
  }
}