package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

case class User(id: Option[Long], username: String, email: String, displayName: String) extends WithId

object User {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[User] = deriveDecoder[User]
  implicit val encodeJson: Encoder[User] = deriveEncoder[User]
}
