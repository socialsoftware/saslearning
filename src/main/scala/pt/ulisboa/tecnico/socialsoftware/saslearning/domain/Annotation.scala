package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

case class Annotation(id: Option[Long], position: Int, offset: Int, content: NonEmptyString, creator: User) extends WithId

object Annotation {

  def fromUnsafe(id: Option[Long], position: Int, offset: Int, content: String, creator: User): Either[String, Annotation] =
    fromString(content) { str =>
      Annotation(id, position, offset, str, creator)
    }

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[Annotation] = deriveDecoder
  implicit val decodePartialJson: Decoder[User => Annotation] = deriveDecoder
  implicit val encodeJson: Encoder[Annotation] = deriveEncoder
}
