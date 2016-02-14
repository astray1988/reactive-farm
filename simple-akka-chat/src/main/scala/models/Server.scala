package models

import akka.actor.{Terminated, Actor, ActorRef}
import message._

/**
  * Created by dylan on 2/14/16.
  */
class Server extends Actor {
  var clients = List.empty[(String, ActorRef)]

  override def receive: Receive = {
    case Connect(username) => {
      broadcast(Info(s"$username join the chat"))
      clients = (username, sender()) :: clients
      context.watch(sender())
    }
    case Broadcast(msg) => {
      val username = getUsername(sender())
      broadcast(NewMsg(username, msg))
    }
    case Terminated(client) => {
      val username = getUsername(sender())
      clients = clients.filter(sender != _._2)
      broadcast(Info(f"$username%s left the chat!"))
    }

  }

  def broadcast(msg: Msg): Unit = {
    clients.foreach(client => client._2 ! msg) // broadcast msg to each client
  }


  def getUsername(actor: ActorRef): String = {
    clients.filter(a => a._2 == actor).head._1
  }
}
