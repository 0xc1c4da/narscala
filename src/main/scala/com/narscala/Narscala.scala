package com.narscala

import akka.actor._
import scala.concurrent.Await
import scala.concurrent.duration._
import sys.ShutdownHookThread
import com.typesafe.config.ConfigFactory
import collection.JavaConversions._

import com.narscala.io.Input

object Defaults {
    val config = ConfigFactory.load()
}

object Narscala extends App {
    private val system = ActorSystem("system")

    system.actorOf(Props[Input]) // UDP Input Server


    ShutdownHookThread {
        println("Shutting Down...")
        system.terminate()
        Await.result(system.whenTerminated, Duration.Inf)
    }
}