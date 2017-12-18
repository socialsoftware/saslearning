package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import javax.mail.internet.InternetAddress

import eu.timepit.refined.auto._
import org.scalatest.{EitherValues, Matchers, WordSpec}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.User

import scala.collection.immutable.Seq

class ThreadSpec extends WordSpec
  with Matchers
  with EitherValues {

  private val user = User(None, username = "jdoe", email = new InternetAddress("john.doe@example.org"), displayName = "John Doe")
  private val question = Question("What's the meaning of life?", user)
  private val comments = Seq(question)

  private def assertLeft(actual: Either[String, Thread]) = {
    actual should be('left)
  }

  private def assertRight(expected: Seq[Comment], actual: Either[String, Thread]) = {
    actual should be('right)
    actual.right.value.comments.value should be(expected)
  }

  "A thread" should {
    "not be empty" in {
      val thread = Thread.fromUnsafe(Seq.empty)
      assertLeft(thread)
    }
    "have at least one comment" in {
      val thread = Thread.fromUnsafe(comments)
      assertRight(expected = comments, actual = thread)
    }
  }

  it should {
    "update it's comments" when {
      val answer = Answer("42", user)
      "adding a new comment" in {
        val comments = Seq(question, answer)
        val thread = Thread.fromUnsafe(Seq(question)).flatMap(_.add(answer))
        assertRight(expected = comments, actual = thread)
      }
      "delete a comment by position" in {
        val thread = Thread.fromUnsafe(Seq(question, answer)).flatMap(_.delete(1))
        assertRight(expected = comments, actual = thread)
      }
      "delete a comment" in {
        val thread = Thread.fromUnsafe(Seq(question, answer)).flatMap(_.delete(answer))
        assertRight(expected = comments, actual = thread)
      }
    }
  }

}
