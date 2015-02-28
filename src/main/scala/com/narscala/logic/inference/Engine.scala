package com.narscala.logic.inference

import com.narscala.logic.grammar._

import org.kie.api.KieServices
import org.kie.api.runtime.{StatelessKieSession, KieContainer}

import java.util.ArrayList
import scala.collection.JavaConversions.iterableAsScalaIterable

object Engine {
    private lazy val kieServices: KieServices = KieServices.Factory.get()
    private lazy val kContainer: KieContainer = kieServices.getKieClasspathContainer

    // def newStatelessSession: StatelessKieSession = kContainer.newStatelessKieSession()
    // def executeStateless(facts: List[Any]) = newStatelessSession.execute(facts)
    def newSession = kContainer.newKieSession()

    def run(tasks: Vector[Sentence]): Vector[Sentence] = {
        val mutableState = new ArrayList[Sentence]
        val session = newSession

        session.setGlobal("mutableState", mutableState)
        tasks map session.insert

        session.fireAllRules()
        session.dispose()

        mutableState.toVector
    }
}