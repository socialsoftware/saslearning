package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.refineV

import scala.collection.immutable.Seq

case class Thread(comments: Seq[Comment] Refined NonEmpty) {

  def add(comment: Comment): Either[String, Thread] = refineV[NonEmpty](comments.value :+ comment).map(Thread(_))
  def delete(position: Int): Either[String, Thread] =
    Thread.fromUnsafe(comments.value.take(position) ++ comments.value.drop(position + 1))
  def delete(comment: Comment): Either[String, Thread] = Thread.fromUnsafe(comments.value.filterNot(_ == comment))
}

object Thread {
  def fromUnsafe(comments: Seq[Comment]): Either[String, Thread] = {
    refineV[NonEmpty](comments).map(Thread(_))
  }

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.refined._

  implicit val decodeJson: Decoder[Thread] = deriveDecoder
  implicit val encodeJson: Encoder[Thread] = deriveEncoder
}