package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration
import scala.collection.immutable.Seq

trait Post[T] {
  def thread: Option[Thread]

  def updateThread(thread: Option[Thread]): T

  def postComment(comment: Comment): Either[String, T] = {
    val threadWithComment = thread match {
      case Some(t) => t.add(comment)
      case _ => Thread.fromUnsafe(Seq(comment))
    }

    threadWithComment.map(thread => updateThread(Some(thread)))
  }

  def deleteComment(comment: Comment): T = updateThread(thread.flatMap(_.delete(comment).toOption))
}
