package models

import akka.actor.{PoisonPill, Actor, ActorRef}
import message._

/**
  * Created by dylan on 2/14/16.
  */
class Client(val username: String, server: ActorRef) extends Actor {
  server ! Connect(username)  // connect to server


  override def receive: Receive = {
    case NewMsg(from, msg) => {
      println(f"[$username's client] - $from: $msg")
    }
    case Send(msg) => server ! Broadcast(msg)
    case Info(msg) => {
      println(s"[$username's client] - $msg" )
    }
    case Disconnect => {
      self ! PoisonPill
    }
  }
}
