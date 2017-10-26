package pt.ulisboa.tecnico.socialsoftware.domain

case class User(id: Option[Long], username: String, email: String, displayName: String) extends WithId

object User {

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto._

  implicit val decodeJson: Decoder[User] = deriveDecoder
  implicit val encodeJson: Encoder[User] = deriveEncoder
}
