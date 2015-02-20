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

        // Test Term Connectors

        "(|, <Java --> coffee>)." in {
            parse("(|, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(IntensionalIntersection(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(--,<Java --> coffee>)." in {
            parse("(--,<Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Negation(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(-, <Java --> coffee>)." in {
            parse("(-, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(ExtensionalDifference(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(~, <Java --> coffee>)." in {
            parse("(~, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(IntensionalDifference(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(*, <Java --> coffee>)." in {
            parse("(*, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Product(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(/, <Java --> coffee>)." in {
            parse("(/,<Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(ExtensionalImage(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(\\, <Java --> coffee>)." in {
            parse("(\\, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(IntensionalImage(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(||,<Java --> coffee>)." in {
            parse("(||,<Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Disjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(&&,<Java --> coffee>)." in {
            parse("(&&,<Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Conjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(&/,<Java --> coffee>)." in {
            parse("(&/,<Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(SequentialConjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(&|,<Java --> coffee>)." in {
            parse("(&|,<Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(ParallelConjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(&, <Java --> coffee>)." in {
            parse("(&, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(ExtensionalIntersection(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        // Test Term Connectors (Mathematical Notation)

        "(∪, <Java --> coffee>)." in {
            parse("(∪, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(IntensionalIntersection(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(¬, <Java --> coffee>)." in {
            parse("(¬, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Negation(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(⊖, <Java --> coffee>)." in {
            parse("(⊖, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(IntensionalDifference(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(×, <Java --> coffee>)." in {
            parse("(×, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Product(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(∨, <Java --> coffee>)." in {
            parse("(∨, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Disjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(∧, <Java --> coffee>)." in {
            parse("(∧, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(Conjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(,, <Java --> coffee>)." in {
            parse("(,, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(SequentialConjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(;, <Java --> coffee>)." in {
            parse("(;, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(ParallelConjunction(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        "(∩, <Java --> coffee>)." in {
            parse("(∩, <Java --> coffee>).") ===
            Vector(Task(None,Judgement(ConnectedSet(ExtensionalIntersection(),Vector(RelationalStatement(Word("Java"),Inheritance(),Word("coffee")))),None,None)))
        }

        // Test Copulas

        "<bird <-> swimmer>." in {
            parse("<bird <-> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Similarity(),Word("swimmer")),None,None)))
        }

        "<bird {-- swimmer>." in {
            parse("<bird {-- swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Instance(),Word("swimmer")),None,None)))
        }

        "<bird --] swimmer>." in {
            parse("<bird --] swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Property(),Word("swimmer")),None,None)))
        }

        "<bird {-] swimmer>." in {
            parse("<bird {-] swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),InstanceProperty(),Word("swimmer")),None,None)))
        }

        "<bird ==> swimmer>." in {
            parse("<bird ==> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Implication(),Word("swimmer")),None,None)))
        }

        "<bird <=> swimmer>." in {
            parse("<bird <=> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Equivalence(),Word("swimmer")),None,None)))
        }

        "<bird =/> swimmer>." in {
            parse("<bird =/> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),PredictiveImplication(),Word("swimmer")),None,None)))
        }

        "<bird =|> swimmer>." in {
            parse("<bird =|> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),ConcurrentImplication(),Word("swimmer")),None,None)))
        }

        "<bird =\\> swimmer>." in {
            parse("<bird =\\> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),RetrospectiveImplication(),Word("swimmer")),None,None)))
        }

        "<bird </> swimmer>." in {
            parse("<bird </> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),PredictiveEquivalence(),Word("swimmer")),None,None)))
        }

        "<bird <|> swimmer>." in {
            parse("<bird <|> swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),ConcurrentEquivalence(),Word("swimmer")),None,None)))
        }

        // Test Copulas Mathematical Notation

        "<bird → swimmer>." in {
            parse("<bird → swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,None)))
        }

        "<bird ↔ swimmer>." in {
            parse("<bird ↔ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Similarity(),Word("swimmer")),None,None)))
        }

        "<bird ◦→ swimmer>." in {
            parse("<bird ◦→ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Instance(),Word("swimmer")),None,None)))
        }

        "<bird →◦ swimmer>." in {
            parse("<bird →◦ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Property(),Word("swimmer")),None,None)))
        }

        "<bird ◦→◦ swimmer>." in {
            parse("<bird ◦→◦ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),InstanceProperty(),Word("swimmer")),None,None)))
        }

        "<bird ⇒ swimmer>." in {
            parse("<bird ⇒ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Implication(),Word("swimmer")),None,None)))
        }

        "<bird ⇔ swimmer>." in {
            parse("<bird ⇔ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),Equivalence(),Word("swimmer")),None,None)))
        }

        "<bird /⇒ swimmer>." in {
            parse("<bird /⇒ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),PredictiveImplication(),Word("swimmer")),None,None)))
        }

        "<bird |⇒ swimmer>." in {
            parse("<bird |⇒ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),ConcurrentImplication(),Word("swimmer")),None,None)))
        }

        "<bird \\⇒ swimmer>." in {
            parse("<bird \\⇒ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),RetrospectiveImplication(),Word("swimmer")),None,None)))
        }

        "<bird /⇔ swimmer>." in {
            parse("<bird /⇔ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),PredictiveEquivalence(),Word("swimmer")),None,None)))
        }

        "<bird |⇔ swimmer>." in {
            parse("<bird |⇔ swimmer>.") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("bird"),ConcurrentEquivalence(),Word("swimmer")),None,None)))
        }

        //

        "(^go-to,{switch0}). :|: %1.00;0.90%" in {
            parse("(^go-to,{switch0}). :|: %1.00;0.90%") ===
            Vector(Task(None,Judgement(OperationStatement(Word("go-to"),Vector(ExtensionalSet(Vector(Word("switch0"))))),Some(Present()),Some(Truth(1.0,Some(0.9))))))
        }

        "<{switch0} --> [at]>. :|: %1.00;0.90%" in {
            parse("<{switch0} --> [at]>. :|: %1.00;0.90%") ===
            Vector(Task(None,Judgement(RelationalStatement(ExtensionalSet(Vector(Word("switch0"))),Inheritance(),IntensionalSet(Vector(Word("at")))),Some(Present()),Some(Truth(1.0,Some(0.9))))))
        }

        "(--,<{door5} --> [opened]>). :|: %1.00;0.90%" in {
            parse("(--,<{door5} --> [opened]>). :|: %1.00;0.90%") ===
            Vector(Task(None,Judgement(ConnectedSet(Negation(),Vector(RelationalStatement(ExtensionalSet(Vector(Word("door5"))),Inheritance(),IntensionalSet(Vector(Word("opened")))))),Some(Present()),Some(Truth(1.0,Some(0.9))))))
        }

        // Question
        "<{door5} --> [opened]>?" in {
            parse("<{door5} --> [opened]>?") ===
            Vector(Task(None,Question(RelationalStatement(ExtensionalSet(Vector(Word("door5"))),Inheritance(),IntensionalSet(Vector(Word("opened")))),None)))
        }

        // Desire
        "<{door5} --> [opened]>@" in {
            parse("<{door5} --> [opened]>@") ===
            Vector(Task(None,Desire(RelationalStatement(ExtensionalSet(Vector(Word("door5"))),Inheritance(),IntensionalSet(Vector(Word("opened")))),None)))
        }

        // Goal
        "<{door5} --> [opened]>!" in {
            parse("<{door5} --> [opened]>!") ===
            Vector(Task(None,Goal(RelationalStatement(ExtensionalSet(Vector(Word("door5"))),Inheritance(),IntensionalSet(Vector(Word("opened")))),None)))
        }

        "<<$1 --> swimmer> ==> <$1 --> bird>>. %1.00;0.42%" in {
            parse("<<$1 --> swimmer> ==> <$1 --> bird>>. %1.00;0.42%") ===
            Vector(Task(None,Judgement(RelationalStatement(RelationalStatement(IndependentVariable(Word("1")),Inheritance(),Word("swimmer")),Implication(),RelationalStatement(IndependentVariable(Word("1")),Inheritance(),Word("bird"))),None,Some(Truth(1.0,Some(0.42))))))
        }

        "<swan --> (&,bird,swimmer)>. %0.90;0.81%" in {
            parse("<swan --> (&,bird,swimmer)>. %0.90;0.81%") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("swan"),Inheritance(),ConnectedSet(ExtensionalIntersection(),Vector(Word("bird"), Word("swimmer")))),None,Some(Truth(0.9,Some(0.81))))))
        }

        "(&&,<#1 --> bird>,<#1 --> swimmer>). %0.90;0.81%" in {
            parse("(&&,<#1 --> bird>,<#1 --> swimmer>). %0.90;0.81%") ===
            Vector(Task(None,Judgement(ConnectedSet(Conjunction(),Vector(RelationalStatement(DependentVariable(Word("1")),Inheritance(),Word("bird")), RelationalStatement(DependentVariable(Word("1")),Inheritance(),Word("swimmer")))),None,Some(Truth(0.9,Some(0.81))))))
        }

        "<{?1} --> swimmer>?" in {
            parse("<{?1} --> swimmer>?") ===
            Vector(Task(None,Question(RelationalStatement(ExtensionalSet(Vector(QueryVariable(Word("1")))),Inheritance(),Word("swimmer")),None)))
        }

        "<robin --> (-,mammal,swimmer)>. %0.00%" in {
            parse("<robin --> (-,mammal,swimmer)>. %0.00%") ===
            Vector(Task(None,Judgement(RelationalStatement(Word("robin"),Inheritance(),ConnectedSet(ExtensionalDifference(),Vector(Word("mammal"), Word("swimmer")))),None,Some(Truth(0.0,None)))))
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