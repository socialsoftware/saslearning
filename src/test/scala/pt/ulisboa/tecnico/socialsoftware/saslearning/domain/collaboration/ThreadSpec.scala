package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

import scala.collection.immutable.Seq

class ThreadSpec extends UnitSpec {

  private val question = Question("What's the meaning of life?", user)
  private val defaultThread = Thread(Refined.unsafeApply(Seq(question)))

  "A thread" should {
    "not be empty" in {
      val thread = Thread.fromUnsafe(Seq.empty)
      assertLeft(thread)
    }
    "have at least one comment" in {
      val thread = Thread.fromUnsafe(Seq(question))
      assertRight(expected = defaultThread, actual = thread)
    }
  }

  it should {
    "update it's comments" when {
      val answer = Answer("42", user)
      "adding a new comment" in {
        val expected = Thread(Refined.unsafeApply(Seq(question, answer)))
        val thread = Thread.fromUnsafe(Seq(question)).flatMap(_.add(answer))
        assertRight(expected, actual = thread)
      }
      "delete a comment by position" in {
        val thread = Thread.fromUnsafe(Seq(question, answer)).flatMap(_.delete(1))
        assertRight(expected = defaultThread, actual = thread)
      }
      "delete a comment" in {
        val thread = Thread.fromUnsafe(Seq(question, answer)).flatMap(_.delete(answer))
        assertRight(expected = defaultThread, actual = thread)
      }
    }
  }

}
