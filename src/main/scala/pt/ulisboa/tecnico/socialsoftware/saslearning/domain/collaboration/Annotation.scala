package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration


import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.types.numeric.{NonNegLong, PosLong}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{User, WithId}

case class Annotation(id: Option[Long],
                      position: NonNegLong, offset: PosLong, content: NonEmptyString,
                      creator: User,
                      thread: Option[Thread] = None) extends WithId with Post[Annotation] {

  override def updateThread(thread: Option[Thread]): Annotation = this.copy(thread = thread)
}

object Annotation {

  def fromUnsafe(id: Option[Long] = None,
                 position: Long, offset: Long, content: String,
                 creator: User,
                 thread: Option[Thread] = None): Either[String, Annotation] = for {
    position <- NonNegLong.from(position)
    offset <- PosLong.from(offset)
    content <- NonEmptyString.from(content)
  } yield Annotation(id, position, offset, content, creator, thread)

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.refined._

  implicit val decodeJson: Decoder[Annotation] = deriveDecoder
  implicit val decodePartialJson: Decoder[User => Annotation] = deriveDecoder
  implicit val encodeJson: Encoder[Annotation] = deriveEncoder
}
