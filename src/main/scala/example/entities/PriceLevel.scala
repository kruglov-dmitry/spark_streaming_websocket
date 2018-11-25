package example.entities

case class PriceLevel(price: Double, volume: Double)

object PriceLevel {
  def apply(initList: List[Any]):PriceLevel = PriceLevel(initList(0).toString.toDouble, initList(1).toString.toDouble)
}
