package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{NonEmptyString, User, fromString}

sealed trait Comment {
  def content: NonEmptyString
  def author: User
}

object Comment {

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.refined._

  implicit val decodeJson: Decoder[Comment] = deriveDecoder
  implicit val encodeJson: Encoder[Comment] = deriveEncoder
}

final case class Question(content: NonEmptyString, author: User) extends Comment
final case class Answer(content: NonEmptyString, author: User) extends Comment
final case class Definition(content: NonEmptyString, author: User) extends Comment
final case class NeedMoreInformation(content: NonEmptyString, author: User) extends Comment

object Question {
  def fromUnsafe(content: String, author: User): Either[String, Comment] = fromString(content)(Question(_, author))
}

object Answer {
  def fromUnsafe(content: String, author: User): Either[String, Comment] = fromString(content)(Answer(_, author))
}

object Definition {
  def fromUnsafe(content: String, author: User): Either[String, Comment] = fromString(content)(Definition(_, author))
}

object NeedMoreInformation {
  def fromUnsafe(content: String, author: User): Either[String, Comment] = fromString(content)(NeedMoreInformation(_, author))
}