package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.types.numeric.NonNegInt

import scala.collection.immutable.Seq

case class Thread(comments: Seq[Comment] = Seq.empty) {

  def add(comment: Comment): Thread = this.copy(comments :+ comment)

  def delete(position: Int): Either[String, Thread] = for {
    position <- NonNegInt.from(position)
  } yield this.copy(comments.take(position.value) ++ comments.drop(position.value + 1))

  def delete(comment: Comment): Thread = this.copy(comments = comments.filterNot(_ == comment))
}

object Thread {

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

  implicit val decodeJson: Decoder[Thread] = deriveDecoder
  implicit val encodeJson: Encoder[Thread] = deriveEncoder
}