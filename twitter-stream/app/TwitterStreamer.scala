package actors

import akka.actor.{Actor, ActorRef, Props}
import play.api.libs.iteratee.{Concurrent, Enumeratee, Enumerator, Iteratee}
import play.api.libs.json.JsObject
import play.api.libs.oauth.{ConsumerKey, OAuthCalculator, RequestToken}
import play.api.libs.ws.WS
import play.api.{Logger, Play}
import play.extras.iteratees.{Encoding, JsonIteratees}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

class TwitterStreamer(out: ActorRef) extends Actor {
  def receive = {
    case "subscribe" => // handling the case receive "subscribe" msg
      Logger.info("Received subscription from a client")
      TwitterStreamer.subscribe(out)
  }
}

object TwitterStreamer {
  def props(out: ActorRef) = Props(new TwitterStreamer(out)) // init a new Props object

  private var broadcastEnumerator: Option[Enumerator[JsObject]] = None

  def connect(): Unit = {
    credentials.map {
      case (consumerKey, requestToken) =>
      val(iteratee, enumerator) = Concurrent.joined[Array[Byte]]
      val jsonStream: Enumerator[JsObject] = enumerator &> Encoding.decode() &> Enumeratee.grouped(JsonIteratees.jsSimpleObject)

      val (be, _) = Concurrent.broadcast(jsonStream)
      broadcastEnumerator = Some(be)

      val url = "https://stream.twitter.com/1.1/statuses/filter.json"

      WS.url(url)
        .withRequestTimeout(-1)
      .sign(OAuthCalculator(consumerKey, requestToken))
      .withQueryString("track" -> "reactive")
      .get { response =>
        Logger.info("Status: " + response.status)
        iteratee
      }.map { _ =>
        Logger.info("Twitter stream closed")
      }
    } getOrElse {
      Logger.error("Twitter credentials missing")
    }
  }

  def subscribe(out: ActorRef): Unit = {
    if (broadcastEnumerator == None) {
      connect()
    }
    val twitterClient = Iteratee.foreach[JsObject] {
      t => out ! t
    }
    broadcastEnumerator.map { enumerator =>
      enumerator run twitterClient
    }
  }

  def credentials: Option[(ConsumerKey,RequestToken)] =
    for {
      consumerKey <- Play.configuration.getString("twitter.consumerKey")
      consumerSecret <- Play.configuration.getString("twitter.consumerSecret")
      token <- Play.configuration.getString("twitter.token")
      tokenSecret <- Play.configuration.getString("twitter.tokenSecret")
    } yield
      (ConsumerKey(consumerKey, consumerSecret),
        RequestToken(token, tokenSecret))

}
