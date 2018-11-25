package example.entities

import example.utils.JsonUtils

case class OrderBookUpdate(pairName: String,
                           tsMsEpoch: Long,
                           sequenceId: Long,
                           sequenceIdEnd: Long,
                           asks: List[PriceLevel],
                           bids: List[PriceLevel])

object OrderBookUpdate {
  def apply(e: RawBinanceUpdate): OrderBookUpdate = {

    val asks = e.a.map(entry=>PriceLevel(entry))
    val bids = e.b.map(entry=>PriceLevel(entry))

    OrderBookUpdate(pairName=e.s, tsMsEpoch= e.E, sequenceId= e.U, sequenceIdEnd= e.u, asks=asks, bids=bids)
  }

  def apply(someString: String): OrderBookUpdate = {
    OrderBookUpdate(JsonUtils.jsonStringToClass[RawBinanceUpdate](someString))
  }
}