package com.narscala.grammar

import scala.util.{Failure, Success}
import org.specs2.mutable.Specification
import org.parboiled2._

class NarseseSpec extends Specification {

    "Narsese Parser" should {

        // Unicode Terms
        "<王培 --> человек>?" in {
            parse("<王培 --> человек>?") ===
                Vector(Task(None,Question(RelationalStatement(Word("王培"),Inheritance(),Word("человек")),None)))
        }

        // Budget(Confidence) with Simple Judgement
        "$0.5$ <bird --> swimmer>." in {
            parse("$0.5$ <bird --> swimmer>.") ===
                Vector(Task(Some(Budget(0.5,None)),Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,None)))
        }

        // Budget(Confidence, Durability) with Simple Judgement
        "$0.5;0.7$ <bird --> swimmer>." in {
            parse("$0.5;0.7$ <bird --> swimmer>.") ===
                Vector(Task(Some(Budget(0.5,Some(0.7))),Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,None)))
        }

        // Simple Judgement
        "<bird --> swimmer>." in {
            parse("<bird --> swimmer>.") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,None)))
        }

        // Judgement with Past Tense
        "<bird --> swimmer>. :\\:" in {
            parse("<bird --> swimmer>. :\\:") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),Some(Past()),None)))
        }

        // Judgement with Present Tense
        "<bird --> swimmer>. :|:" in {
            parse("<bird --> swimmer>. :|:") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),Some(Present()),None)))
        }

        // Judgement with Future Tense
        "<bird --> swimmer>. :/:" in {
            parse("<bird --> swimmer>. :/:") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),Some(Future()),None)))
        }

        // Judgement with Truth(Frequency)
        "<bird --> swimmer>. %0.10%" in {
            parse("<bird --> swimmer>. %0.10%") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(0.1,None)))))
        }

        // Judgement with Truth(Frequency & Confidence)
        "<bird --> swimmer>. %0.10;0.60%" in {
            parse("<bird --> swimmer>. %0.10;0.60%") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(0.10,Some(0.60))))))
        }

        // Judgement with Tense, Truth(Frequency & Confidence)
        "<bird --> swimmer>. :/: %0.10;0.60%" in {
            parse("<bird --> swimmer>. :/: %0.10;0.60%") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),Some(Future()),Some(Truth(0.10,Some(0.60))))))
        }

        // Multiple Tasks
        "<bird --> swimmer>. %0.10;0.60% <duck --> swimmer>. %0.50;0.90%" in {
            parse("<bird --> swimmer>. %0.10;0.60% <duck --> swimmer>. %0.5;0.90%") ===
                Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(0.1,Some(0.6))))), Task(None,Judgement(RelationalStatement(Word("duck"),Inheritance(),Word("swimmer")),None,Some(Truth(0.5,Some(0.9))))))
        }

        // Broken because of Left Recursion
        "(--,<Java --> coffee>)." in {
            parse("(--,<Java --> coffee>).") ===
            Vector(Task(None,Judgement())) // TODO
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