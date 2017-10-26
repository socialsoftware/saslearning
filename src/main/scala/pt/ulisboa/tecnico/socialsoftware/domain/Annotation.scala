package pt.ulisboa.tecnico.socialsoftware.domain

case class Annotation(id: Option[Long], position: Int, offset: Int, content: String, creator: User) extends WithId

object Annotation {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[Annotation] = deriveDecoder
  implicit val decodePartialJson: Decoder[User => Annotation] = deriveDecoder
  implicit val encodeJson: Encoder[Annotation] = deriveEncoder
}
