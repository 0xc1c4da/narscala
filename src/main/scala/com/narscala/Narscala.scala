package com.narscala

import akka.actor._
import sys.ShutdownHookThread

import com.narscala.io.Input

object Narscala extends App {
	private val system = ActorSystem("system")

	system.actorOf(Props[Input]) // UDP Input Server

	ShutdownHookThread {
		println("Shutting Down...")
		system.shutdown()
		system.awaitTermination()
	}
}