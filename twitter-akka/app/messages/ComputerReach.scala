package messages

/**
  * Created by dylan on 2/13/16.
  */
case class ComputerReach(tweetId: BigInt)
case class TweetReach(tweetId: BigInt, score: Int)

case class FetchFollowerCount(user: String)
case class FollowerCount(user: String, followersCount: Int)

case class StoreReach(tweetId: BigInt, score: Int)
case class ReachStored(tweetId: BigInt)

