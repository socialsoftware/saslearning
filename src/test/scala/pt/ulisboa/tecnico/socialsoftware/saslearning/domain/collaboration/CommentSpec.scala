package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class CommentSpec extends UnitSpec {

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
      val content = "Can you explain this?"
      assertRight(expected = content, actual = Question.fromUnsafe(content, user))
    }
  }

  "An answer" should {
    "not be empty" in {
      val comment = Answer.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      val content = "Can you explain this?"
      assertRight(expected = content, actual = Answer.fromUnsafe(content, user))
    }
  }

  "A definition" should {
    "not be empty" in {
      val comment = Definition.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      val content = "The number π is a mathematical constant"
      assertRight(expected = content, actual = Definition.fromUnsafe(content, user))
    }
  }

  "Need for more information" should {
    "not be empty" in {
      val comment = NeedMoreInformation.fromUnsafe("", user)
      assertLeft(comment)
    }
    "have content" in {
      val content = "Clarify why 80 is the default port for HTTP"
      assertRight(expected = content, actual = NeedMoreInformation.fromUnsafe(content, user))
    }
  }
}
