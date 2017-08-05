package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController extends Controller {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(name:Option[String]) = Action { /*implicit request: Request[AnyContent] =>*/
    val newPage = Redirect(routes.HomeController.getName())
    name.fold(newPage.withNewSession){name =>newPage.withSession("name"->name)}
  }

  def getAge() = Action{
    implicit request: Request[AnyContent] =>
      Ok {
        request.flash.get("age").map(value =>
          s"${request.session.get("name").map(name=>name).getOrElse("unknown")}'s age is $value years old"
        ).getOrElse("couldnt find age  for user")
      }
  }

  def setAge(age:Long) = Action{
    Redirect(routes.HomeController.getAge()).flashing{
      "age" -> s"$age"
    }
  }

  def getName() = Action{
    request =>
      request.session.get("name").map{
        user => Ok(s"name is $user")
      }.getOrElse{
        Ok("no user available")
      }
  }

}
