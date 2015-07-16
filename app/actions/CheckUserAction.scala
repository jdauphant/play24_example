package actions

import javax.inject.Inject

import dao.UserDAO
import models._
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by julien on 7/16/15.
 */
class UserRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

class CheckUserAction @Inject()(userDAO: UserDAO) extends ActionBuilder[UserRequest] with ActionRefiner[Request, UserRequest] {
  def refine[A](request: Request[A]) = {
    request.headers.get("X-Name") match {
      case None =>
        Future.successful {
          Left(Unauthorized("No X-Name Header"))
        }
      case Some(name) =>
        userDAO.retrieveByName(name).map {
          case Some(user) =>
            Right(new UserRequest(user, request))
          case _ =>
            Left(Unauthorized(s"No user $name"))
        }
    }

  }
}
