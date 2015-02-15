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

    def InputLine = rule { Task ~ optional("\n") ~ EOI }

    // Helper Rules
    implicit def wspStr(s: String): Rule0 = rule { str(s) ~ zeroOrMore(' ') }
    def Flt = rule { Digits ~ Dec }
    def Digits = rule { oneOrMore(Digit) }
    def Dec = rule { str(".") ~ Digits }

    // Grammar
    def Task = rule { optional(Budget) ~ Sentence }

    def Sentence = rule {
        Statement ~ "." ~ optional(Tense) ~ optional(Truth) |
        Statement ~ "?" ~ optional(Tense) |
        Statement ~ "@" ~ optional(Tense) |
        Statement ~ "!" ~ optional(Truth)
    }

    def Statement = rule { 
         "<" ~ Term ~ Copula ~ Term ~ ">" |
         Term  |
         "(^" ~ Word ~ oneOrMore(Term).separatedBy(",") ~ ")"
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
        Statement                                                      // a statement can serve as a term
    }

    def CompoundTerm = rule {
        "{"   ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ "}" |
        "["   ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ "]" |
        "(&,"  ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(|,"  ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "-,"   ~ Term ~ "," ~ Term ~ ")" |
        "~,"   ~ Term ~ "," ~ Term ~ ")" |
        "(*,"  ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(/,"  ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(\\," ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(--," ~ Term ~ ")" |
        "(||," ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(&&," ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(&/," ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" |
        "(&|," ~ Term ~ zeroOrMore(Term).separatedBy(",") ~ ")" 
    }

    def Variable = rule {
        "$" ~ Word |
        "#" ~ Word |
        "?" ~ Word 
    }

    def Tense = rule {
        ":/:" |
        ":|:" |
        ":\\:"
    }

    def Truth = rule {
        "%" ~ Frequency ~ optional(";" ~ Confidence) ~ "%"
    }

    def Budget = rule {
        "$" ~ Priority ~ optional(";" ~ Durability) ~ "$"
    }

    def Word = rule {
        oneOrMore(noneOf("<>{}[]()&-~*/\\|:$%\n"))
    }

    def Frequency = rule { Flt }
    def Confidence = rule { Flt }
    def Priority = rule { Flt }
    def Durability = rule { Flt }
}