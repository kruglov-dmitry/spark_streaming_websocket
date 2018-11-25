package example.utils

import com.typesafe.scalalogging.LazyLogging
import org.json4s.JsonAST.JNothing
import org.json4s.{CustomSerializer, DefaultFormats, Formats}
import org.json4s.ext.{JavaTypesSerializers, JodaTimeSerializers}
import org.json4s.native.JsonMethods.parse

trait Ignore

object Json4sFormats {
  implicit def json4sFormats: Formats = DefaultFormats ++ JavaTypesSerializers.all ++ JodaTimeSerializers.all +
    new CustomSerializer[Ignore](formats => (
      PartialFunction.empty,
      { case _: Ignore => JNothing }
    ))
}

object JsonUtils extends LazyLogging {
  import Json4sFormats._

  def parseStringToJson(someString: String) = parse(someString)

  def jsonStringToClass[T](someString: String)(implicit ev: scala.reflect.Manifest[T]) = {
    parseStringToJson(someString).extract[T]
  }
}