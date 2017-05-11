package services

import play.api.libs.json.{JsObject, JsValue}
import scala.collection.mutable.ListBuffer

class ValidatorService {

  def validate(json: JsValue):(Map[String, Option[Double]], ListBuffer[String]) = {
    var errors = new ListBuffer[String]()

    val dataLength = (json \ "data").asOpt[JsObject].map(j => j.fields.size).getOrElse(0)

    if (dataLength != 3) {
      errors += "Numbers of arguments mismatch"
    }

    (retrieveData(json), errors)
  }

  def retrieveData(json: JsValue):Map[String, Option[Double]] = {
    val map = Map(
      "angleA" -> jsonRetrieve(json, "angleA"),
      "angleB" -> jsonRetrieve(json, "angleB"),
      "angleC" -> jsonRetrieve(json, "angleC"),
      "sideA" -> jsonRetrieve(json, "sideA"),
      "sideB" -> jsonRetrieve(json, "sideB"),
      "sideC" -> jsonRetrieve(json, "sideC")
    )

    map.filter(_._2.isDefined)
  }

  def jsonRetrieve(json: JsValue, name: String): Option[Double] = json \\ name match {
    case s :: rest => s.asOpt[Double] match {
      case Some(i) => Some(i)
      case _ => None
    }
    case Nil => None
  }

}
