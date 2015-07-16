package dao

import javax.inject.Singleton
import models.User
import scala.concurrent.Future

@Singleton
class UserDAO {
    def retrieveByName(name: String) = Future.successful(Some(User(name)))
}
