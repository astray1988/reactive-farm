import akka.actor.{Props, ActorSystem}
import message.{Disconnect, Send}
import models.{Client, Server}

/**
  * Created by dylan on 2/14/16.
  */
object ChatMain extends App {
  val system = ActorSystem("ChatSystem")
  val server = system.actorOf(Props[Server])
  val client1 = system.actorOf(Props(new Client("Sam", server)))
  client1 ! Send("Hi, anayone here?")
  val client2 = system.actorOf(Props(new Client("Tim", server)))
  val client3 = system.actorOf(Props(new Client("Luke", server)))

  client2 ! Send("Hello")
  client3 ! Send("Hello")

  client3 ! Disconnect


}
