package controllers

import actors.TwitterStreamer
import play.api.Play.current
import play.api.libs.iteratee.{Enumeratee, Enumerator, Concurrent, Iteratee}
import play.api.libs.json.{JsValue, JsObject}
import play.api.libs.oauth.{ConsumerKey, OAuthCalculator, RequestToken}
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.{Logger, Play}
import play.extras.iteratees.{JsonIteratees, Encoding}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {
  final val TwitterStatus = "https://stream.twitter.com/1.1/statuses/filter.json"
  // set up a joined iteratee and enumerator
  val (iteratee, enumerator) = Concurrent.joined[Array[Byte]]
  // define pipeline
  val jsonStream: Enumerator[JsObject] =
    enumerator &>
    Encoding.decode() &> // parse Array[Byte] into utf-8 charsrting
    Enumeratee.grouped(JsonIteratees.jsSimpleObject) // have the stream consumed by iteratee

  val loggingIteratee = Iteratee.foreach[JsObject] { value =>
    Logger.info(value.toString)
  }
  // plug the transformed json stream into the logging iteratee
  jsonStream run loggingIteratee

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def tweetsActor = WebSocket.acceptWithActor[String, JsValue] {
    request => out => TwitterStreamer.props(out)
  }

  def tweets = Action.async {
    credentials map {
      case (consumerKey, requestToken) =>
        WS.url(TwitterStatus)
          .sign(OAuthCalculator(consumerKey, requestToken))
        .withQueryString("track" -> "reactive")
        .get { response =>
          Logger.info("Status: " + response.status)
          iteratee
        }
        .map { _ =>
          Ok("Stream closed")
        }
    } getOrElse {
      Future {
        InternalServerError("Twitter credentials not match")
      }
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

  def loggingIterates = Iteratee.foreach[Array[Byte]] { array =>
    Logger.info(array.map(_.toChar).mkString)
  }





}
