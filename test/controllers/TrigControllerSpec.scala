package controllerstest

import controllers.TrigController
import play.api.libs.json.JsValue
import services.{ValidatorService, TrigonometryService}

import scala.concurrent.Future

import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._

class TrigControllerSpec extends PlaySpec with Results {

  val trigonometryService = new TrigonometryService
  val validatorService = new ValidatorService

  "/api/triangle" should {
    "with correct payload should be valid" in {
      val body: JsValue = Json.parse("""
                                       {
                                         "data": {
                                           "angleA": 46.5,
                                           "sideB": 10.5,
                                           "sideC": 18
                                         }
                                       }
                                     """)

      val result = getBodyJson(body)
      val bodyJson: JsValue = contentAsJson(result)

      assert(status(result) == play.api.http.Status.OK)

      val jsonResult = bodyJson \ "result"
      (jsonResult \ "sideC").asOpt[Double].get mustBe 18
      (jsonResult \ "angleB").asOpt[Double].get mustBe 35.26
    }
  }

  "/api/triangle" should {
    "with incorrect payload should return valid error" in {
      val body: JsValue = Json.parse("""
                                       {
                                         "data": {
                                           "angleA": 46.5
                                         }
                                       }
                                     """)


      val result = getBodyJson(body)
      val bodyJson: JsValue = contentAsJson(result)

      assert(status(result) == play.api.http.Status.BAD_REQUEST)

      (bodyJson \ "error").asOpt[String].get mustBe "Numbers of arguments mismatch"
    }
  }

  def getBodyJson(body:JsValue) = {
    val controller = new TrigController(trigonometryService, validatorService)
    val request: Request[JsValue] = FakeRequest().withBody(body)
    val result: Future[Result] = controller.generateTriangle().apply(request)
    result
  }
}
