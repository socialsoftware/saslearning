package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.auto._
import org.scalatest.{EitherValues, Matchers, WordSpec}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.User

class AnnotationSpec extends WordSpec
  with Matchers
  with EitherValues {

  private val user = User(None, username = "jdoe", email = "john.doe@example.org", displayName = "John Doe")
  private val defaultAnnotation = Annotation(None, 0l, 1l, "This is an annotation", user)

  private def assertLeft(actual: Either[String, Annotation]) = {
    actual should be('left)
  }

  private def assertRight(expected: Annotation, actual: Either[String, Annotation]) = {
    actual should be('right)
    actual.right.value should be(expected)
  }

  "An annotation" should {
    "have a content" when {
      "is empty" in {
        val annotation = Annotation.fromUnsafe(None, 0l, 10l, "", user)
        assertLeft(annotation)

      }
      "is not empty" in {
        val annotation = Annotation.fromUnsafe(None, 0l, 1l, "This is an annotation", user)
        assertRight(expected = defaultAnnotation, actual = annotation)
      }
    }
    "have a position >= 0" when {
      "it's smaller than 0" in {
        val annotation = Annotation.fromUnsafe(None, -1l, 1l, "This is an annotation", user)
        assertLeft(annotation)
      }
      "equal to 0" in {
        val annotation = Annotation.fromUnsafe(None, 0l, 1l, "This is an annotation", user)
        assertRight(expected = defaultAnnotation, actual = annotation)
      }
      "greater than 0" in {
        val annotation = Annotation.fromUnsafe(None, 1l, 10l, "This is an annotation", user)
        val expected = Annotation(None, 1l, 10l, "This is an annotation", user)
        assertRight(expected = expected, actual = annotation)
      }
    }
    "have a offset > 0" when {
      "it's smaller than 0" in {
        val annotation = Annotation.fromUnsafe(None, 0l, -1l, "This is an annotation", user)
        assertLeft(annotation)
      }
      "equal to 0" in {
        val annotation = Annotation.fromUnsafe(None, 0l, 0l, "This is an annotation", user)
        assertLeft(annotation)
      }
      "greater than 0" in {
        val annotation = Annotation.fromUnsafe(None, 0l, 1l, "This is an annotation", user)
        assertRight(expected = defaultAnnotation, actual = annotation)
      }
    }
  }

}
