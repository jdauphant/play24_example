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

  def mongoWritesObjectId[T](w: Writes[T]) = OWrites[T] {
    a: T =>
      val js = w.writes(a)
      js.as[JsObject] - "id" ++ Json.obj("_id" -> Json.obj("$oid" -> (js \ "id").get))
  }

  def typeReads[T](r: Reads[T]) =
    JsPath.json.update((JsPath \ '_type).json.copyFrom((JsPath \ 'type).json.pick[JsString])) andThen r

  def mongoReadsObjectId[T](r: Reads[T]) =
    JsPath.json.update((__ \ 'id).json.copyFrom((JsPath \ '_id \ '$oid).json.pick[JsString] )) andThen r

  implicit val feedRead = typeReads[User](mongoReadsObjectId[User](Json.reads[User]))
  implicit val userWrite = typeWrites[User](mongoWritesObjectId[User](Json.writes[User]))
}
