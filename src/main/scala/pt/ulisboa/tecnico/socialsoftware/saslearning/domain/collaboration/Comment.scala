package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{ fromString, User }

sealed trait Comment {
  def content: NonEmptyString
  def author: User
}

object Comment {

  import io.circe.{ Decoder, Encoder }
  import io.circe.generic.extras._
  import io.circe.generic.extras.semiauto.{ deriveDecoder, deriveEncoder }
  import io.circe.refined._

  implicit val config: Configuration =
    Configuration.default.withSnakeCaseConstructorNames.withSnakeCaseMemberNames
      .withDiscriminator("type")

  implicit val decodeJson: Decoder[Comment] = deriveDecoder[Comment]
  implicit val encodeJson: Encoder[Comment] = deriveEncoder[Comment]

}

final case class Question(content: NonEmptyString, author: User) extends Comment
final case class Answer(content: NonEmptyString, author: User) extends Comment
final case class Definition(content: NonEmptyString, author: User) extends Comment
final case class NeedMoreInformation(content: NonEmptyString, author: User) extends Comment

object Question {
  def fromUnsafe(content: String, author: User): Either[String, Question] =
    fromString(content)(Question(_, author))
}

object Answer {
  def fromUnsafe(content: String, author: User): Either[String, Answer] =
    fromString(content)(Answer(_, author))
}

object Definition {
  def fromUnsafe(content: String, author: User): Either[String, Definition] =
    fromString(content)(Definition(_, author))
}

object NeedMoreInformation {
  def fromUnsafe(content: String, author: User): Either[String, NeedMoreInformation] =
    fromString(content)(NeedMoreInformation(_, author))
}
