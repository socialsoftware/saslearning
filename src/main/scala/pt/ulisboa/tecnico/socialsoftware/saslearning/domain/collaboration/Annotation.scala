package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import scala.collection.immutable.Seq

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{NonEmptyString, User, WithId}

case class Annotation(id: Option[Long],
                      position: Natural, offset: Positive, content: NonEmptyString,
                      creator: User,
                      thread: Option[Thread] = None) extends WithId {

  def post(comment: Comment): Either[String, Annotation] = {
    val threadWithComment = thread match {
      case Some(t) => t.add(comment)
      case _ => Thread.fromUnsafe(Seq(comment))
    }

    threadWithComment.map(thread => this.copy(thread = Some(thread)))
  }

  def deletePost(comment: Comment): Annotation = {
    val updatedAnnotation: Option[Annotation] = for {
      concreteThread <- thread
    } yield this.copy(thread = concreteThread.delete(comment).toOption)

    updatedAnnotation.getOrElse(this)
  }

}

object Annotation {

  def fromUnsafe(id: Option[Long] = None,
                 position: Long, offset: Long, content: String,
                 creator: User,
                 thread: Option[Thread] = None): Either[String, Annotation] = for {
    position <- Natural(position)
    offset <- Positive(offset)
    content <- NonEmptyString(content)
  } yield Annotation(id, position, offset, content, creator, thread)

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.refined._

  implicit val decodeJson: Decoder[Annotation] = deriveDecoder
  implicit val decodePartialJson: Decoder[User => Annotation] = deriveDecoder
  implicit val encodeJson: Encoder[Annotation] = deriveEncoder
}
