package controllers

import play.api.libs.concurrent.Execution.Implicits._
import com.iheart.playSwagger.SwaggerSpecGenerator
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class ApiSpecs extends Controller {
  implicit val cl = getClass.getClassLoader
  private lazy val generator = SwaggerSpecGenerator()

  def specs = Action.async { _ =>
      Future.fromTry(generator.generate()).map(Ok(_))
    }

}
