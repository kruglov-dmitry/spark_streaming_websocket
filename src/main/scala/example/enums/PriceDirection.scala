package example.enums

object PriceDirection extends Enumeration {
  type PriceDirection = Value

  val UP, DOWN, FLAT, UNKNOWN = Value

  implicit def withNameWithDefault(name: String): Value =
    values.find(_.toString.toLowerCase == name.toLowerCase()).getOrElse(UNKNOWN)

}