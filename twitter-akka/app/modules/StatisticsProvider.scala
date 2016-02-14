package modules

import akka.actor.{ActorRef, Props, Actor, ActorLogging}


object StatisticsProvider {
  def props = Props[StatisticsProvider]
}


class StatisticsProvider extends Actor with ActorLogging {
  var reachComputer: ActorRef = _
  var storage: ActorRef = _
  var followersCounter: ActorRef = _

  override def preStart(): Unit = {
    log.info("Starting Statistics Provider")
    followersCounter = context.actorOf(
      UserFollowersCounter.props, "userFollowersCounter"
    )
    storage = context.actorOf(
      Storage.props, "storage"
    )
    reachComputer = context.actorOf(
      TweetReachComputer.props(followersCounter, storage), "tweetReachComputer"
    )
  }
  override def receive: Receive = ???
}
