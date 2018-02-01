package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

trait Post[T] {
  def thread: Thread

  /**
    * A function to update T, given a new [[Thread]].
    *
    * @param thread the new [[Thread]] to replace in T.
    *
    * @return the new T.
    */
  def updated(thread: Thread): T

  def postComment(comment: Comment): T = updated(thread.add(comment))

  /**
    * Deletes a comment by position.
    *
    * @param position the position of the comment to be deleted. Must be >= 0.
    *
    * @return Right(T) if position >= 0, where T is updated without the comment.
    */
  def deleteComment(position: Int): Either[String, T] = thread.delete(position).map(t => updated(t))

  /**
    * Deletes the comment.
    *
    * @param comment the comment to delete
    *
    * @return the updated T, without the comment.
    */
  def deleteComment(comment: Comment): T = updated(thread.delete(comment))
}
