package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{NonEmptyString, User, WithId}

case class Annotation(id: Option[Long], position: Natural, offset: Natural, content: NonEmptyString, creator: User) extends WithId

object Annotation {

  def fromUnsafe(id: Option[Long], position: Long, offset: Long, content: String, creator: User): Either[String, Annotation] =
    for {
      position <- Natural(position)
      offset <- Natural(offset)
      content <- NonEmptyString(content)
    } yield Annotation(id, position, offset, content, creator)

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.refined._

  implicit val decodeJson: Decoder[Annotation] = deriveDecoder
  implicit val decodePartialJson: Decoder[User => Annotation] = deriveDecoder
  implicit val encodeJson: Encoder[Annotation] = deriveEncoder
}
