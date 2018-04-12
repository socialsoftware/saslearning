package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.types.numeric.NonNegInt

import scala.collection.immutable.Seq

trait Thread[T] {

  protected def comments: Seq[Comment]

  protected def updated(items: Seq[Comment]): T

  def post(comment: Comment): T = updated(comments :+ comment)

  /**
    * Deletes the comment.
    *
    * @param comment the comment to delete
    *
    * @return the updated T, without the comment.
    */
  def delete(comment: Comment): T = updated(comments.filterNot(_ == comment))

  /**
    * Deletes a comment by position.
    *
    * @param position the position of the comment to be deleted. Must be >= 0.
    *
    * @return Right(T) if position >= 0, where T is updated without the comment.
    */
  def delete(position: Int): Either[String, T] =
    for {
      position <- NonNegInt.from(position)
    } yield updated(comments.take(position.value) ++ comments.drop(position.value + 1))

}
