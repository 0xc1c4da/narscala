package com.narscala.grammar

import scala.util.parsing.combinator._

class Narsese extends JavaTokenParsers with PackratParsers {
    /*
    Non-Axiomatic Logic: A Model of Intelligent Reasoning (2013)
    pg. 218, Table A.1

    https://code.google.com/p/open-nars/wiki/InputOutputFormat

    TODO: Stackoverflow on unicode characters? i.e <请核对 --> 请核对>.
    TODO: Stackoverflow on $ ?
    */

    def task : Parser[Any] = opt(budget)~sentence                       // task to be processed

    def sentence : Parser[Any] = statement~"."~opt(tense)~opt(truth) |  // judgment to be remembered, NAL-8
            statement~"?"~opt(tense) |                                  // question to be answered, NAL-8
            statement~"@"~opt(tense) |                                  // question on desire value to be answered, NAL-8
            statement~"!"~opt(truth)                                    // goal to be realized, NAL-8

    def statement : Parser[Any] = "<"~term~copula~term~">" |            // two terms related to each other
            term |                                                      // a term can name a statement
            "(^"~word~rep(","~term)~")"                                 // an operation to be executed  

    def copula : Parser[Any] = "-->" |                                  // inheritance, NAL-1
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
            "<|>"                                                       // concurrent equivalence, NAL-7

    def term : Parser[Any] = word |                                     // an atomic constant term
            variable     |                                              // an atomic variable term
            compoundterm |                                              // a term with internal structure
            statement                                                   // a statement can serve as a term

    def compoundterm : Parser[Any] = "{"~term~rep(","~term)~"}" |       // extensional set, NAL-2
            "["~term~rep(","~term)~"]"   |                              // intensional set, NAL-2
            "(&,"~term~rep(","~term)~")" |                              // extensional intersection, NAL-3
            "(|,"~term~rep(","~term)~")" |                              // intensional intersection, NAL-3
            "(-,"~term~","~term~")"   |                                 // extensional difference, NAL-3
            "(~,"~term~","~term~")"   |                                 // intensional difference, NAL-3
            "(*,"~term~rep(","~term)~")" |                              // product, NAL-4
            "(/,"~term~rep(","~term)~")" |                              // extensional image, NAL-4
            "(\\,"~term~rep(","~term)~")"|                              // intensional image, NAL-4
            "(--,"~term~")"           |                                 // negation, NAL-5
            "(||,"~term~rep(","~term)~")"|                              // disjunction, NAL-5
            "(&&,"~term~rep(","~term)~")"|                              // conjunction, NAL-5
            "(&/,"~term~rep(","~term)~")"|                              // sequential events/conjunction, NAL-7
            "(&|,"~term~rep(","~term)~")"                               // parallel events/conjunciton, NAL-7

    def variable : Parser[Any] = "$$"~word |                             // independent variable / variable in judgment(?), NAL-6
            "#"~opt(word) |                                             // dependent variable, operator(?), NAL-8
            "?"~opt(word)                                               // query variable in question, NAL-6

    def tense : Parser[Any] = ":/:" |                                   // future event, NAL-7
            ":|:" |                                                     // present event, NAL-7
            ":\\:"                                                      // past event, NAL-7

    def truth : Parser[Any] = "%"~frequency~opt(";"~confidence)~"%"     // two numbers in [0,1]x(0,1)
    def budget : Parser[Any] = "$$"~priority~opt(";"~durability)~"$$"     // two numbers in [0,1]x(0,1)
    def word : Parser[String] = """\w+""".r ^^ { _.toString }

    def frequency : Parser[Double] = """(0(\.\d+)?|1(\.0+)?)""".r ^^ { _.toDouble }
    def confidence : Parser[Double] = """(0(\.\d+)?|1(\.0+)?)""".r ^^ { _.toDouble }
    def priority : Parser[Double] = """(0(\.\d+)?|1(\.0+)?)""".r ^^ { _.toDouble }
    def durability : Parser[Double] = """(0(\.\d+)?|1(\.0+)?)""".r ^^ { _.toDouble }

}