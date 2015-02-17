package com.narscala.grammar

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
  * TODO: Fix Recursion on term in statement
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
        statement ~ "." ~ optional(tense) ~ optional(truth) ~> Judgement |          // judgment to be remembered, NAL-8
        statement ~ "?" ~ optional(tense) ~> Question |                             // question to be answered, NAL-8
        statement ~ "@" ~ optional(tense) ~> Desire |                               // question on desire value to be answered, NAL-8
        statement ~ "!" ~ optional(truth) ~> Goal                                   // goal to be realized, NAL-8
    }

    def statement:Rule1[Statement] = rule { 
         "<" ~ term ~ copula ~ term ~ ">" ~> RelationalStatement |                  // two terms related to each other
         // term  | // commented to avoid recursion issue                           // a term can name a statement
         "(^" ~ word ~ oneOrMore(term).separatedBy(",") ~ ")" ~> OperationStatement // an operation to be executed 
    }

    def copula:Rule1[Int] = rule {
        ("-->" | "→"  )  ~ push(Copula.INHERITANCE)              |                  // inheritance, NAL-1
        ("<->" | "↔"  )  ~ push(Copula.SIMILARITY)               |                  // similarity, NAL-2
        ("{--" | "◦→" )  ~ push(Copula.INSTANCE)                 |                  // instance, NAL-2
        ("--]" | "→◦" )  ~ push(Copula.PROPERTY)                 |                  // property, NAL-2
        ("{-]" | "◦→◦")  ~ push(Copula.INSTANCE_PROPERTY)        |                  // instance-property, NAL-2
        ("==>" | "⇒"  )  ~ push(Copula.IMPLICATION)              |                  // implication, NAL-5
        ("<=>" | "⇔"  )  ~ push(Copula.EQUIVALENCE)              |                  // equivalence, NAL-5
        ("=/>" | "/⇒" )  ~ push(Copula.PREDICTIVE_IMPLICATION)   |                  // predictive implication, NAL-7
        ("=|>" | "|⇒" )  ~ push(Copula.CONCURRENT_IMPLICATION)   |                  // concurrent implication, NAL-7
        ("=\\>"| "\\⇒")  ~ push(Copula.RETROSPECTIVE_IMPLICATION)|                  // retrospective implication, NAL-7
        ("</>" | "/⇔" )  ~ push(Copula.PREDICTIVE_EQUIVALENCE)   |                  // predictive equivalence, NAL-7
        ("<|>" | "|⇔" )  ~ push(Copula.CONCURRENT_EQUIVALENCE)                      // concurrent equivalence, NAL-7
    }

    def term = rule {
        compoundterm |                                                              // a term with internal structure
        statement    |                                                              // a statement can serve as a term
        variable  |                                                                 // an atomic variable term
        word                                                                        // an atomic constant term
    }

    def compoundterm:Rule1[CompoundTerm] = rule {
        "{" ~ oneOrMore(term).separatedBy(",") ~ "}" ~> ExtensionalSet |            // extensional set, NAL-2
        "[" ~ oneOrMore(term).separatedBy(",") ~ "]" ~> IntensionalSet |            // intensional set, NAL-2
        "(" ~ termconnector ~ "," ~ oneOrMore(term).separatedBy(",") ~ ")" ~> ConnectedSet // See termconnector
    }

    def termconnector:Rule1[Int] = rule {                                           // Term Connectors
        ("|"  | "∪" ) ~ push(TermConnector.INTENSIONAL_INTERSECTION)|               // intensional intersection, NAL-3
        "-"           ~ push(TermConnector.EXTENSIONAL_DIFFERENCE)  |               // extensional difference, NAL-3
        ("~"  | "⊖" ) ~ push(TermConnector.INTENSIONAL_DIFFERENCE)  |               // intensional difference, NAL-3
        ("*"  | "×" ) ~ push(TermConnector.PRODUCT)                 |               // product, NAL-4
        "/"           ~ push(TermConnector.EXTENSIONAL_IMAGE)       |               // extensional image, NAL-4
        "\\"          ~ push(TermConnector.INTENSIONAL_IMAGE)       |               // intensional image, NAL-4
        ("--" | "¬")  ~ push(TermConnector.NEGATION)                |               // negation, NAL-5
        ("||" | "∨")  ~ push(TermConnector.DISJUNCTION)             |               // disjunction, NAL-5
        ("&&" | "∧")  ~ push(TermConnector.CONJUNCTION)             |               // conjunction, NAL-5
        ("&/" | ",")  ~ push(TermConnector.SEQUENTIAL_CONJUNCTION)  |               // sequential events/conjunction, NAL-7
        ("&|" | ";")  ~ push(TermConnector.PARALLEL_CONJUNCTION)    |               // parallel events/conjunciton, NAL-7
        ("&"  | "∩" ) ~ push(TermConnector.EXTENSIONAL_INTERSECTION)                // extensional intersection, NAL-3
    }

    def variable:Rule1[Variable] = rule {                                               
        "$" ~ word ~> IndependentVariable |                                         // independent variable / variable in judgment(?), NAL-6
        "#" ~ word ~> DependentVariable |                                           // dependent variable, operator(?), NAL-8
        "?" ~ word ~> QueryVariable                                                 // query variable in question, NAL-6
    }

    def tense:Rule1[Int] = rule {
        (":/:" | "/⇒" ) ~ push(Tense.FUTURE)  |                                     // future event, NAL-7
        (":|:" | "|⇒" ) ~ push(Tense.PRESENT) |                                     // present event, NAL-7
        (":\\:"| "\\⇒") ~ push(Tense.PAST)                                          // past event, NAL-7
    }

    def truth = rule {                                                              // two numbers in [0,1]x(0,1)
        "%" ~ frequency ~ optional(";" ~ confidence) ~ "%" ~> Truth             
    }

    def budget = rule {                                                             // two numbers in [0,1]x(0,1)
        "$" ~ priority  ~ optional(";" ~ durability) ~ "$" ~> Budget              
    }

    def word:Rule1[Word] = rule {                                                   // unicode string in an arbitrary alphabet
        capture(oneOrMore(noneOf("<>{}[]()&-~*/\\|:;$%\n'\", ∩∪-⊖×¬∨∧⇒→↔◦⇔"))) ~> Word
    }

    def frequency :Rule1[Double] = rule { num }
    def confidence:Rule1[Double] = rule { num }
    def priority  :Rule1[Double] = rule { num }
    def durability:Rule1[Double] = rule { num }
}