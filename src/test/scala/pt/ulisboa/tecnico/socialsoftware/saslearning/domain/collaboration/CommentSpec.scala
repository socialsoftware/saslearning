package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import javax.mail.internet.InternetAddress

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import io.circe.syntax._
import org.scalatest.{EitherValues, Matchers, WordSpec}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.User

class CommentSpec extends WordSpec
  with Matchers
  with EitherValues {

  private val user = User(Some(0), username = "jdoe", email = new InternetAddress("john.doe@example.org"), displayName = "John Doe")

  private val question = "Can you explain this?"
  private val answer = "This is a pie"
  private val definition = "The number π is a mathematical constant"
  private val needMoreInformation = "Clarify why 80 is the default port for HTTP"

  private def commentToJsonString(comment: Comment, `type`: String): String =
    s"""{"content":"${comment.content}","author":${user.asJson.noSpaces},"type":"${`type`}"}""".stripMargin

  private def assertLeft(actual: Either[String, Comment]) = {
    actual should be('left)
  }

  private def assertRight(expected: String, actual: Either[String, Comment]) = {
    actual should be('right)
    actual.right.value.content.value should be(expected)
    actual.right.value.author should be(user)
  }

  "A question" should {
    "not be empty" in {
      val comment = Question.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      assertRight(expected = question, actual = Question.fromUnsafe(question, user))
    }
  }

  "An answer" should {
    "not be empty" in {
      val comment = Answer.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      assertRight(expected = answer, actual = Answer.fromUnsafe(answer, user))
    }
  }

  "A definition" should {
    "not be empty" in {
      val comment = Definition.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      assertRight(expected = definition, actual = Definition.fromUnsafe(definition, user))
    }
  }

  "Need for more information" should {
    "not be empty" in {
      val comment = NeedMoreInformation.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      assertRight(expected = needMoreInformation, actual = NeedMoreInformation.fromUnsafe(needMoreInformation, user))
    }
  }

  "Exporting a comment to JSON" should {
    "contain the type information" when {
      "is a question" in {
        val comment: Comment = Question(Refined.unsafeApply(question), user)
        assert(comment.asJson.noSpaces == commentToJsonString(comment, "question"))
      }
      "is an answer" in {
        val comment: Comment = Answer(Refined.unsafeApply(answer), user)
        assert(comment.asJson.noSpaces == commentToJsonString(comment, "answer"))

      }
      "is a definition" in {
        val comment: Comment = Definition(Refined.unsafeApply(definition), user)
        assert(comment.asJson.noSpaces == commentToJsonString(comment, "definition"))

      }
      "is a request for more information" in {
        val comment: Comment = NeedMoreInformation(Refined.unsafeApply(needMoreInformation), user)
        assert(comment.asJson.noSpaces == commentToJsonString(comment, "need_more_information"))
      }
    }
  }
}
