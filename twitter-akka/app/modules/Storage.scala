package modules

import akka.actor.{Props, Actor}

/**
  * Created by dylan on 2/13/16.
  */
object Storage {
  def props = Props[Storage]
}

class Storage extends Actor {
  override def receive: Receive = ???
}
