package example

import com.typesafe.config.ConfigFactory
import example.entities.{MarketStats, OrderBookUpdate}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object Streaming {

  val rootConfig = ConfigFactory.load()

  val windowDuration = Duration(60000)
  val slideDuration = Duration(15000)

  def reduceFunc(first: OrderBookUpdate, second: OrderBookUpdate) =
    OrderBookUpdate(first.pairName,
      first.tsMsEpoch,
      first.sequenceId,
      first.sequenceIdEnd,
      first.asks ++ second.asks,
      first.bids ++ second.bids)

  def main(args: Array[String]) {

    // Create the context with a batch accumulated for 15 second of streaming

    val sparkConf = new SparkConf()
      .setAppName(rootConfig.getString("spark.app_name"))
      .setMaster(rootConfig.getString("spark.spark_master"))
      .setAppName("CustomReceiver")

    val ssc = new StreamingContext(sparkConf, Seconds(15))

    // Create custom receiver and
    val updates = ssc.
      receiverStream(new BinanceOrderBookUpdates("dashbtc")).
      reduceByWindow(reduceFunc, windowDuration, slideDuration)

    val res = updates.map(entry=>MarketStats.fromAccumulatedOrderBookUpdates(entry))

    res.print()

    ssc.start()
    ssc.awaitTermination()

  }
}