package com.narscala.grammar

import org.parboiled2._

class Narsese(val input: ParserInput) extends Parser with StringBuilding {
    /*
    Non-Axiomatic Logic: A Model of Intelligent Reasoning (2013)
    pg. 218, Table A.1

    https://code.google.com/p/open-nars/wiki/InputOutputFormat

    TODO: Fix Recursion on Term
    */
    import CharPredicate.{Digit}

    def InputLine = rule { oneOrMore(Task) ~ EOI }

    // Helper Rules
    implicit def wspStr(s: String): Rule0 = rule { str(s) ~ zeroOrMore(' ') }
    def Num = rule { Digits ~ optional(Dec) }
    def Digits = rule { oneOrMore(Digit) }
    def Dec = rule { str(".") ~ Digits }

    // Grammar
    def Task = rule { optional(Budget) ~ Sentence ~ optional("\n") }    // task to be processed

    def Sentence = rule {                                               
        Statement ~ "." ~ optional(Tense) ~ optional(Truth) |           // judgment to be remembered, NAL-8
        Statement ~ "?" ~ optional(Tense) |                             // question to be answered, NAL-8
        Statement ~ "@" ~ optional(Tense) |                             // question on desire value to be answered, NAL-8
        Statement ~ "!" ~ optional(Truth)                               // goal to be realized, NAL-8
    }

    def Statement = rule { 
         "<" ~ Term ~ Copula ~ Term ~ ">" |                             // two terms related to each other
         // Term  | // comments to avoid recursion issue                // a term can name a statement
         "(^" ~ Word ~ oneOrMore(Term).separatedBy(",") ~ ")"           // an operation to be executed 
    }

    def Copula = rule {
            "-->"  |                                                    // inheritance, NAL-1
            "<->"  |                                                    // similarity, NAL-2
            "{--"  |                                                    // instance, NAL-2
            "--]"  |                                                    // property, NAL-2
            "{-]"  |                                                    // instance-property, NAL-2
            "==>"  |                                                    // implication, NAL-5
            "<=>"  |                                                    // equivalence, NAL-5
            "=/>"  |                                                    // predictive implication, NAL-7
            "=|>"  |                                                    // concurrent implication, NAL-7
            "=\\>" |                                                    // retrospective implication, NAL-7
            "</>"  |                                                    // predictive equivalence, NAL-7
            "<|>"
    }

    def Term:Rule0 = rule {
        Word         |                                                  // an atomic constant term
        Variable     |                                                  // an atomic variable term
        CompoundTerm |                                                  // a term with internal structure
        Statement                                                       // a statement can serve as a term
    }

    def CompoundTerm = rule {
        "{" ~ oneOrMore(Term).separatedBy(",") ~ "}" |               // extensional set, NAL-2
        "[" ~ oneOrMore(Term).separatedBy(",") ~ "]" |               // intensional set, NAL-2
        "(" ~ optional(Op ~ ",") ~ oneOrMore(Term).separatedBy(Op) ~ ")"
    }

    def Op = rule {
        "&" |                                                           // extensional intersection, NAL-3
        "|" |                                                           // intensional intersection, NAL-3
        "-" |                                                           // extensional difference, NAL-3
        "~" |                                                           // intensional difference, NAL-3
        "*" |                                                           // product, NAL-4
        "/" |                                                           // extensional image, NAL-4
        "\\"|                                                           // intensional image, NAL-4
        "--"|                                                           // negation, NAL-5
        "||"|                                                           // disjunction, NAL-5
        "&&"|                                                           // conjunction, NAL-5
        "&/"|                                                           // sequential events/conjunction, NAL-7
        "&|"|                                                           // parallel events/conjunciton, NAL-7
        "," |                                                           // seperator
        " "

    }

    def Variable = rule {                                               
        "$" ~ Word |                                                    // independent variable / variable in judgment(?), NAL-6
        "#" ~ Word |                                                    // dependent variable, operator(?), NAL-8
        "?" ~ Word                                                      // query variable in question, NAL-6
    }

    def Tense = rule {
        ":/:" |                                                         // future event, NAL-7
        ":|:" |                                                         // present event, NAL-7
        ":\\:"                                                          // past event, NAL-7
    }

    def Truth = rule {
        "%" ~ Frequency ~ optional(";" ~ Confidence) ~ "%"              // two numbers in [0,1]x(0,1)
    }

    def Budget = rule {
        "$" ~ Priority ~ optional(";" ~ Durability) ~ "$"               // two numbers in [0,1]x(0,1)
    }

    def Word = rule {
        oneOrMore(noneOf("<>{}[]()&-~*/\\|:$%\n'\""))                  // Anything apart from chars used in grammar
    }

    def Frequency = rule { Num }
    def Confidence = rule { Num }
    def Priority = rule { Num }
    def Durability = rule { Num }
}