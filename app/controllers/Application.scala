package controllers

import javax.inject.{Singleton, Inject}

import actions.{LoggingAction, CheckUserAction}
import dao.UserDAO
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

@Singleton
class Application @Inject() (userDao: UserDAO, checkUserAction: CheckUserAction) extends Controller {

  def index = LoggingAction {
    checkUserAction.async {
      request =>
        Future.successful(Ok(s"My name is ${request.user.name}"))
    }
  }

}
