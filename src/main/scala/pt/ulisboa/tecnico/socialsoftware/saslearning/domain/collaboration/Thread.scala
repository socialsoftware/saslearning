package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.refineV

import scala.collection.immutable.Seq

case class Thread(comments: Seq[Comment] Refined NonEmpty) {

  def add(comment: Comment): Either[String, Thread] = refineV[NonEmpty](comments.value :+ comment).map(Thread(_))
  def delete(position: Int): Either[String, Thread] =
    Thread.fromUnsafe(comments.value.take(position) ++ comments.value.drop(position + 1))
}

object Thread {
  def fromUnsafe(comments: Seq[Comment]): Either[String, Thread] = {
    refineV[NonEmpty](comments).map(Thread(_))
  }
}