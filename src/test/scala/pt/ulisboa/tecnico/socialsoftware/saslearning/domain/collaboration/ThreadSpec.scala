package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.auto._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

import scala.collection.immutable.Seq

class ThreadSpec extends UnitSpec {

  private val question = Question("What's the meaning of life?", user)
  private val defaultThread = Thread(Seq(question))

  "A thread" should {
    "update it's comments" when {
      val answer = Answer("42", user)
      "adding a new comment" in {
        val expected = Thread(Seq(question, answer))
        val thread = defaultThread.add(answer)
        assert(expected == thread)
      }
      "delete a comment by position" in {
        val thread = Thread(Seq(question, answer)).delete(1)
        assertRight(expected = defaultThread, actual = thread)
      }
      "delete a comment" in {
        val thread = Thread(Seq(question, answer)).delete(answer)
        assert(defaultThread == thread)
      }
    }
  }

  it should {
    "failed to delete by position with a negative number" in {
      val actual = defaultThread.delete(-1)
      actual should be ('left)
    }
  }

}
