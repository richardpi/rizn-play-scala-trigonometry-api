package controllers

import javax.inject.Inject

import play.api.libs.json.{JsValue}
import play.api.mvc.{Action, Controller}
import services.{ValidatorService, TrigonometryService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._

class TrigController @Inject()(trigonometryService: TrigonometryService, validatorService: ValidatorService) extends Controller {

  /**
    * generating triangle data in json using laws of sine and cosine
    *
    * @return
    */
  def generateTriangle = Action.async(parse.json) { implicit request =>
    val json: JsValue = request.body

    Future {
        val data = validatorService.validate(json)
        val triangleData = data._1
        val errors = data._2

        if (0 == errors.length) {
          val mapTrigData = trigonometryService.calculate(triangleData)
          if (mapTrigData.contains("error"))
            BadRequest(Json.toJson(Map("error" -> TrigonometryService.errors(mapTrigData("error").toInt))))
          else
            Ok(Json.toJson(Map("result" -> mapTrigData)))
        } else {
          BadRequest(Json.toJson(Map("error" -> errors.toList.mkString(", "))))
        }
    }
  }

}
