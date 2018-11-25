package example.entities

import example.enums.{PriceDirection, VolumeTrend}
import example.enums.PriceDirection.PriceDirection
import example.enums.VolumeTrend.VolumeTrend

case class MarketStats(direction: PriceDirection, volumeTrends: VolumeTrend)

object MarketStats {

  /*
  * NOTE: We are dealing with order book updates only - not necessary top\bottom bids and asks
  *       i.e. it is far from any practical estimation!
  *
  *       In reallity it may worth to :
  *         - specify several features as briefly drafted here
  *         - use them to train some classifier (SVM can be a good start)
  *         - apply it for stream
  * */

  def computePriceTrend(asks: List[PriceLevel], bids: List[PriceLevel]): PriceDirection = {
    val isEmptyUpdates = asks.isEmpty || bids.isEmpty
    if (isEmptyUpdates)
      PriceDirection.UNKNOWN
    else {

      /*
      *                     Features candidates
      * */

      val filledAsks = asks.filter(_.volume <= 0)
      val newAsks = asks.filter(_.volume > 0)

      val priceTrendForAsks = filledAsks.map(_.price).sum - newAsks.map(_.price).sum

      if (priceTrendForAsks > 0)
        PriceDirection.UP
      else if (priceTrendForAsks < 0)
        PriceDirection.DOWN
      else
        PriceDirection.FLAT
    }
  }

  def computeVolumeTrends(asks: List[PriceLevel], bids: List[PriceLevel]): VolumeTrend = {
    val isEmptyUpdates = asks.isEmpty || bids.isEmpty
    if (isEmptyUpdates)
      VolumeTrend.UNKNOWN
    else {

      val askVolume = asks.map(_.volume).sum
      val bidsVolume = bids.map(_.volume).sum
      val volumeDemand = askVolume - bidsVolume

      if (volumeDemand > 0)
        VolumeTrend.INCREASE
      else if (volumeDemand < 0)
        VolumeTrend.DECREASE
      else
        VolumeTrend.FLAT
    }
  }

  def fromAccumulatedOrderBookUpdates(orderBookUpdate: OrderBookUpdate): MarketStats = {

    val priceTrend = computePriceTrend(orderBookUpdate.asks, orderBookUpdate.bids)
    val volumeTrend = computeVolumeTrends(orderBookUpdate.asks, orderBookUpdate.bids)

    MarketStats(priceTrend, volumeTrend)
  }
}
