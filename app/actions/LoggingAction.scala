package actions

import play.api.Logger
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

case class LoggingAction[A](action: Action[A]) extends Action[A] {
  def apply(request: Request[A]): Future[Result] = {
    action(request).map{ result =>
      lazy val message = "%s %s?%s %s => %s".format(request.method,request.path,request.rawQueryString, request.body.toString, result.header.status)
      if(result.header.status >= 400)
        Logger.error(message)
      else if(Logger.isDebugEnabled)
        Logger.debug(message)
      else
        Logger.info("%s %s?%s => %s".format(request.method,request.path,request.rawQueryString, result.header.status))
      result
    }
  }
  lazy val parser = action.parser
}