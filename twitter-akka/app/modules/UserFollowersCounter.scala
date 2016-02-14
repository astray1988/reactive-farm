package modules

import akka.actor.{Props, Actor}

/**
  * Created by dylan on 2/13/16.
  */

object UserFollowersCounter {
  def props = Props[UserFollowersCounter]
}

class UserFollowersCounter extends Actor {
  override def receive: Receive = ???
}
