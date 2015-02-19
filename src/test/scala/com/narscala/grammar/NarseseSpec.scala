package com.narscala.grammar

import scala.util.{Failure, Success}
import org.specs2.mutable.Specification
import org.parboiled2._

class NarseseSpec extends Specification {

  "Narsese Parser" should {
    "<bird --> swimmer>." in {
      parse("<bird --> swimmer>.") === Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,None)))
    }

    "<bird --> swimmer>. %0.10;0.60%" in {
      parse("<bird --> swimmer>. %0.10;0.60%") === Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(0.10,Some(0.60))))))
    }
  }

  def parse(s: String):Seq[Task] = {
    val parser = new Narsese(s)
    parser.InputLine.run() match {
      case Success(result)        => result
      case Failure(e: ParseError) => sys.error(parser.formatError(e))
      case Failure(e)             => throw e
    }
  }
}