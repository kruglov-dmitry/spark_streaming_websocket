package example

import java.util.concurrent.atomic.AtomicInteger

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.http.scaladsl.settings.ClientConnectionSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging
import example.entities.OrderBookUpdate
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

import scala.concurrent.{Future, Promise}

class BinanceOrderBookUpdates(pairName: String) extends Receiver[OrderBookUpdate](StorageLevel.MEMORY_ONLY_SER) with LazyLogging {

  val SUBSCRIPTION_URL = s"wss://stream.binance.com:9443/ws/$pairName@depth"

  def onStart() {
    // Start the thread that receives data over a connection
    new Thread("Web Socket Receiver") {
      override def run() { receive() }
    }.start()
  }

  def onStop() {
    // There is nothing much to do as the thread calling receive()
    // is designed to stop by itself if isStopped() returns false
  }

  private def receive(): Unit = {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    val defaultSettings = ClientConnectionSettings(system)

    val pingCounter = new AtomicInteger()
    val customWebsocketSettings =
      defaultSettings.websocketSettings
        .withPeriodicKeepAliveData(() ⇒ ByteString(s"heartbeat-${pingCounter.incrementAndGet()}"))

    val customSettings =
      defaultSettings.withWebsocketSettings(customWebsocketSettings)

    val messageSink: Sink[Message, NotUsed] =
      Flow[Message]
        .map(message => store(OrderBookUpdate(message.asTextMessage.getStrictText)))
        .to(Sink.seq)

    val flow: Flow[Message, Message, Promise[Option[TextMessage.Strict]]] =
      Flow.fromSinkAndSourceMat(
        messageSink,
        Source.maybe[TextMessage.Strict]
      )(Keep.right)

    val (upgradeResponse, promise) = Http().singleWebSocketRequest(
      WebSocketRequest(SUBSCRIPTION_URL),
      flow,
      settings = customSettings)

    upgradeResponse flatMap { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        Future.successful(Done)
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }

  }
}
