package example.enums

object VolumeTrend extends Enumeration {
  type VolumeTrend = Value

  val INCREASE, DECREASE, FLAT, UNKNOWN = Value

  implicit def withNameWithDefault(name: String): Value =
    values.find(_.toString.toLowerCase == name.toLowerCase()).getOrElse(UNKNOWN)

}
