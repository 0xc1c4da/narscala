package com.narscala.logic.inference

import com.narscala.logic.grammar._

import com.narscala.logic.inference.engine.RuleEngine
import com.narscala.logic.inference.engine.WorkingMemory


object Engine {
    val re = RuleEngine(NAL1.rules)

    def run(facts: Vector[Sentence]) = {

        val workingMemory = WorkingMemory(facts.toList)
        
        re.execOn(workingMemory)

        println("result is")
        println(workingMemory.all(classOf[Judgement]))

    }
}