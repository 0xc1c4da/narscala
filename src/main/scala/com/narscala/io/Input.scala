package com.narscala.io

import java.net.InetSocketAddress
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.util.ByteString
import akka.io._
import com.typesafe.config.ConfigFactory

import com.narscala.grammar.Narsese

class Input() extends Actor with ActorLogging {
    /*
    Actor for handling input tasks over UDP
    */
    import context.system

    val config = ConfigFactory.load()

    IO(Udp) ! Udp.Bind(self, new InetSocketAddress(
        config.getString("narscala.io.address"), config.getInt("narscala.io.port")
    ))
 
    def receive = {
        case Udp.Bound(local) =>
            context.become(ready(sender()))
    }
 
    def ready(socket: ActorRef): Receive = {
        case Udp.Received(data, remote) =>
            log.debug(data.decodeString("UTF-8"))

            val parser = new Narsese()
            val parsed = parser.parseAll(parser.task, data.decodeString("UTF-8")).toString
            val newData = ByteString.fromString(parsed + "\n")

            // val processed = // parse data etc., e.g. using PipelineStage
            socket ! Udp.Send(newData, remote)

    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}