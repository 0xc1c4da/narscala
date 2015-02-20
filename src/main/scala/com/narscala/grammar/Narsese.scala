package com.narscala.grammar

import scala.language.implicitConversions
import org.parboiled2._

/** Narsese Parser that attempts to reflect;
  *
  * - Non-Axiomatic Logic: A Model of Intelligent Reasoning (2013)
  *     pg. 218, Table A.1
  *
  * - I/O Format, Narsese Grammar
  *     https://code.google.com/p/open-nars/wiki/InputOutputFormat
  *
  * - NARS 1.6.3 Draft Format (to a lesser extent)
  *     https://docs.google.com/spreadsheets/d/1qrf1c82WXc6c6sYzBrij735r9aTNI-awNVH-te-4ShI/
  *
  * TODO: TermConnector mixins for shorthand 1.6.3 narsese (discuss order of operations)
  */
class Narsese(val input: ParserInput) extends Parser with StringBuilding {

    import CharPredicate.{Digit}

    def InputLine = rule { oneOrMore(task) ~ EOI }

    // Helper Rules
    implicit def wspStr(s: String): Rule0 = rule { zeroOrMore(' ') ~ str(s) ~ zeroOrMore(' ') }
    def num:Rule1[Double] = rule { capture(digits ~ optional(str(".") ~ digits)) ~> ((s:String) => s.toDouble) }
    def digits = rule { oneOrMore(Digit) }

    // Grammar Rules
    def task:Rule1[Task] = rule {                                                   // task to be processed
        optional(budget) ~ sentence ~ optional("\n") ~> Task
    }    

    def sentence:Rule1[Sentence] = rule {                                               
        term ~ "." ~ optional(tense) ~ optional(truth) ~> Judgement |          // judgment to be remembered, NAL-8
        term ~ "?" ~ optional(tense) ~> Question                    |          // question to be answered, NAL-8
        term ~ "@" ~ optional(tense) ~> Desire                      |          // question on desire value to be answered, NAL-8
        term ~ "!" ~ optional(truth) ~> Goal                                   // goal to be realized, NAL-8
    }

    def statement:Rule1[Statement] = rule { 
         "<" ~ term ~ copula ~ term ~ ">" ~> RelationalStatement |                  // two terms related to each other
         // term  ~> TermStatement | // commented to avoid recursion issue                           // a term can name a statement
         "(^" ~ word ~ "," ~ oneOrMore(term).separatedBy(",") ~ ")" ~> OperationStatement // an operation to be executed 
    }

    def copula:Rule1[Copula] = rule {
        ("<->" | "↔"  )  ~> Similarity              |                               // similarity, NAL-2
        ("{-]" | "◦→◦")  ~> InstanceProperty        |                               // instance-property, NAL-2
        ("{--" | "◦→" )  ~> Instance                |                               // instance, NAL-2
        ("--]" | "→◦" )  ~> Property                |                               // property, NAL-2
        ("-->" | "→"  )  ~> Inheritance             |                               // inheritance, NAL-1
        ("==>" | "⇒"  )  ~> Implication             |                               // implication, NAL-5
        ("<=>" | "⇔"  )  ~> Equivalence             |                               // equivalence, NAL-5
        ("=/>" | "/⇒" )  ~> PredictiveImplication   |                               // predictive implication, NAL-7
        ("=|>" | "|⇒" )  ~> ConcurrentImplication   |                               // concurrent implication, NAL-7
        ("=\\>"| "\\⇒")  ~> RetrospectiveImplication|                               // retrospective implication, NAL-7
        ("</>" | "/⇔" )  ~> PredictiveEquivalence   |                               // predictive equivalence, NAL-7
        ("<|>" | "|⇔" )  ~> ConcurrentEquivalence                                   // concurrent equivalence, NAL-7
    }

    def term = rule {
        compoundterm |                                                              // a term with internal structure
        statement    |                                                              // a statement can serve as a term
        variable     |                                                              // an atomic variable term
        word                                                                        // an atomic constant term
    }

    def compoundterm:Rule1[CompoundTerm] = rule {
        "{" ~ oneOrMore(term).separatedBy(",") ~ "}" ~> ExtensionalSet |            // extensional set, NAL-2
        "[" ~ oneOrMore(term).separatedBy(",") ~ "]" ~> IntensionalSet |            // intensional set, NAL-2
        "(" ~ termconnector ~ "," ~ oneOrMore(term).separatedBy(",") ~ ")" ~> ConnectedSet // See termconnector
    }

    def termconnector:Rule1[TermConnector] = rule {                                 // Term Connectors
        ("||" | "∨" ) ~> Disjunction            |                                   // disjunction, NAL-5
        ("|"  | "∪" ) ~> IntensionalIntersection|                                   // intensional intersection, NAL-3
        ("--" | "¬" ) ~> Negation               |                                   // negation, NAL-5
        ("-"  | "-" ) ~> ExtensionalDifference  |                                   // extensional difference, NAL-3
        ("~"  | "⊖" ) ~> IntensionalDifference  |                                   // intensional difference, NAL-3
        ("*"  | "×" ) ~> Product                |                                   // product, NAL-4
        ("/"  | "/" ) ~> ExtensionalImage       |                                   // extensional image, NAL-4
        ("\\" | "\\") ~> IntensionalImage       |                                   // intensional image, NAL-4
        ("&&" | "∧" ) ~> Conjunction            |                                   // conjunction, NAL-5
        ("&/" | "," ) ~> SequentialConjunction  |                                   // sequential events/conjunction, NAL-7
        ("&|" | ";" ) ~> ParallelConjunction    |                                   // parallel events/conjunciton, NAL-7
        ("&"  | "∩" ) ~> ExtensionalIntersection                                    // extensional intersection, NAL-3
    }

    def variable:Rule1[Variable] = rule {                                               
        "$" ~ word ~> IndependentVariable |                                         // independent variable / variable in judgment(?), NAL-6
        "#" ~ word ~> DependentVariable   |                                         // dependent variable, operator(?), NAL-8
        "?" ~ word ~> QueryVariable                                                 // query variable in question, NAL-6
    }

    def tense:Rule1[Tense] = rule {
        (":/:" | "/⇒" ) ~> Future  |                                                // future event, NAL-7
        (":|:" | "|⇒" ) ~> Present |                                                // present event, NAL-7
        (":\\:"| "\\⇒") ~> Past                                                     // past event, NAL-7
    }

    def truth = rule {                                                              // two numbers in [0,1]x(0,1)
        "%" ~ frequency ~ optional(";" ~ confidence) ~ "%" ~> Truth             
    }

    def budget = rule {                                                             // two numbers in [0,1]x(0,1)
        "$" ~ priority  ~ optional(";" ~ durability) ~ "$" ~> Budget              
    }

    def word:Rule1[Word] = rule {                                                   // unicode string in an arbitrary alphabet
        capture(oneOrMore(noneOf("<>{}[]()&~*/\\|:;$%\r\n'\", \t∩∪⊖×¬∨∧⇒→↔◦⇔"))) ~> Word
    }

    def frequency :Rule1[Double] = rule { num }
    def confidence:Rule1[Double] = rule { num }
    def priority  :Rule1[Double] = rule { num }
    def durability:Rule1[Double] = rule { num }
}