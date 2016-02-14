package modules

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import messages._

import scala.concurrent.Future

/**
  * Created by dylan on 2/13/16.
  */
object TweetReachComputer {
  def props(followersCounter: ActorRef, storage: ActorRef) = {
    Props(classOf[TweetReachComputer], followersCounter, storage)
  }
}



class TweetReachComputer(userFollowersCounter: ActorRef, storage: ActorRef)
  extends Actor with ActorLogging {

  implicit val ec = context.dispatcher
  var followerCountsByRetweet = Map.empty[FetchedRetweet, List[FollowerCount]]
  override def receive: Receive = {
    case ComputerReach(tweetId) => // feteched tweets reached
      fetchRetweets(tweetId, sender()).map { fetchedRetweets =>
        followerCountsByRetweet = followerCountsByRetweet + (fetchedRetweets -> List.empty)

        fetchedRetweets.retweeters.foreach { rt =>
          userFollowersCounter ! FetchFollowerCount(tweetId)

        }

      }

    case count @ FollowerCount(tweetId, _, _) =>

    case ReachStored(tweetId) =>

  }

  case class FetchedRetweet (tweetId: BigInt, retweeters: List[String], client: ActorRef)

  def fetchedRetweetsFor(tweetId: BigInt) =
    followerCountsByRetweet.keys.find(tweet => tweet.tweetId == tweetId)

  def updateFollowersCount(tweetId: BigInt, fetchedRetweets: FetchedRetweet, count: FollowerCount ) = {
    val existingCounts = followerCountsByRetweet(fetchedRetweets)
    followerCountsByRetweet = followerCountsByRetweet.updated(fetchedRetweets, count :: existingCounts)

    val newCounts = followerCountsByRetweet(fetchedRetweets)
    if (newCounts.length == fetchedRetweets.retweeters.length) {
      log.info("Received all retweeters followers count for tweet {}" + ", computing sum", tweetId)
      val score = newCounts.map(_.followersCount).sum

      fetchedRetweets.client ! TweetReach(tweetId, score)
      storage ! StoreReach(tweetId, score)

    }
  }

  def fetchRetweets(tweetId: BigInt, client: ActorRef): Future[FetchedRetweet] = ???
}
