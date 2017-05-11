  package controllers

  import javax.inject._
  import play.api.libs.json.Json
  import play.api.mvc._

  /**
    * Homepage controller
    */
  @Singleton
  class HomeController @Inject() extends Controller {

    /**
      * GET request with a path of /
      */
    def index = Action {
      Ok(Json.toJson(Map("name" -> "gateway1")))
    }

  }
