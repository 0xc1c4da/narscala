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

import scala.util.{ Success, Failure }
import org.parboiled2._

import com.narscala.Narscala
import com.narscala.grammar.Narsese

/** Actor for handling input Narsese over UDP
  * 
  * Narsese comes in packet over string, is parsed
  * TODO: and will be sent to Reasoner.
  */
class Input() extends Actor {
    import context.system

    IO(Udp) ! Udp.Bind(self, new InetSocketAddress(
        Narscala.config.getString("narscala.io.address"), Narscala.config.getInt("narscala.io.port")
    ))
 
    def receive = {
        case Udp.Bound(local) =>
            context.become(ready(sender()))
    }
 
    def ready(socket: ActorRef): Receive = {
        case Udp.Received(data, remote) =>
            // log.debug(data.decodeString("UTF-8"))

            val parser = new Narsese(data.decodeString("UTF-8"))
            val result = parser.InputLine.run() match {
                  case Success(ast) => {
                    println(ast)
                    "valid"
                  }
                  case Failure(e: ParseError) => parser.formatError(e)
                  case Failure(e)             => "unexpected error: " + e
                }
            val newData = ByteString.fromString(result.toString + "\n")

            // val processed = // parse data etc., e.g. using PipelineStage
            socket ! Udp.Send(newData, remote)

    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}