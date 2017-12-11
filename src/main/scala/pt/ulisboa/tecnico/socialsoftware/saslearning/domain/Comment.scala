package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

case class Comment(content: NonEmptyString, author: User)

object Comment {

  def fromUnsafe(content: String, author: User): Either[String, Comment] = fromNonEmptyString(content) { str =>
    Comment(str, author)
  }
}