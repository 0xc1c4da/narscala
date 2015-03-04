package com.narscala.logic.inference

import com.narscala.logic.grammar._

import scala.util.{Failure, Success}
import org.specs2.mutable.Specification


class EngineSpec extends Specification {

    "Engine" should {


        /* Revision
         * <bird --> swimmer>.
         * // Bird is a type of swimmer.
         * <bird --> swimmer>. %0.10;0.60%
         * // Bird is probably not a type of swimmer.
         * 1
         *  OUT: <bird --> swimmer>. %0.87;0.91%
         * // Bird is very likely to be a type of swimmer.
         */
        "Revision" in {
            // val facts = Vector(
            //     Task(None,Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(1.0,Some(0.9))))),
            //     Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(0.1,Some(0.6))))
            // )

            val sentences = Vector(
                Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(1.0))),
                Judgement(RelationalStatement(Word("bird"),Inheritance(),Word("swimmer")),None,Some(Truth(0.1,Some(0.6))))
            )

            // val sentences = tasks.map( task => task.sentence )

            println(Engine.run(sentences))
            true
        }
    }

}