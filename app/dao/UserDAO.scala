package dao

import javax.inject.{Inject, Singleton}
import models.User
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json._
import play.modules.reactivemongo.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@Singleton
class UserDAO @Inject() (val reactiveMongoApi: ReactiveMongoApi) {
    import formats.JsonFormats._

    lazy val db = reactiveMongoApi.db
    def collection: JSONCollection = db.collection[JSONCollection]("users")

    def retrieveByName(name: String) =
        collection.find(Json.obj("name" -> name))
          .cursor[User]().collect[List]()
          .map { _.headOption }
    def insert(user: User) = collection.insert(user)
}
