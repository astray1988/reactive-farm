package actors

import akka.actor.{Props, Actor, ActorRef}
import play.api.Logger
import play.api.libs.json.Json

class TwitterStreamer(out: ActorRef) extends Actor {
  def receive = {
    case "subscribe" => // handling the case receive "subscribe" msg
      Logger.info("Received subscription from a client")
      out ! Json.obj("text" -> "Hello, World")
  }
}

object TwitterStreamer {
  def props(out: ActorRef) = Props(new TwitterStreamer(out)) // init a new Props object
}
