package formats

import models._

import play.api.libs.json._
import scala.language.implicitConversions

object JsonFormats {
  import play.api.libs.json.Json

  def typeWrites[T](w: Writes[T]) = OWrites[T] {
    a: T =>
      val js = w.writes(a)
      js.as[JsObject] - "_type"  ++ Json.obj("type" ->  (js \ "_type").get)
  }

  def typeReads[T](r: Reads[T]) = JsPath.json.update((JsPath \ '_type).json.copyFrom((JsPath \ 'type).json.pick[JsString])) andThen r

  implicit val feedRead = typeReads[User](Json.reads[User])
  implicit val userWrite = typeWrites[User](Json.writes[User])
}
