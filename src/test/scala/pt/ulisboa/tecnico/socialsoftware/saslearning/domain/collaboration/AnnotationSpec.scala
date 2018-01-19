package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.auto._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

import scala.collection.immutable.Seq

class AnnotationSpec extends UnitSpec {

  private val defaultAnnotation = Annotation(None, 0l, 1l, "This is an annotation", user)

  "An annotation" should {
    val annotation = Annotation.fromUnsafe(None, 0l, 1l, "This is an annotation", user)
    "have a content" when {
      "is empty" in {
        val annotation = Annotation.fromUnsafe(None, 0l, 10l, "", user)
        assertLeft(annotation)

      }
      "is not empty" in {
        assertRight(expected = defaultAnnotation, actual = annotation)
      }
    }
    "have a position >= 0" when {
      "it's smaller than 0" in {
        val annotation = Annotation.fromUnsafe(None, -1l, 1l, "This is an annotation", user)
        assertLeft(annotation)
      }
      "equal to 0" in {
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
        assertRight(expected = defaultAnnotation, actual = annotation)
      }
    }
  }

  "Posting a comment" should {
    val question = Question("What is an annotation?", user)
    "create a thread when it doesn't exist" in {
      val threadedAnnotation = defaultAnnotation.postComment(question)

      val expectedAnnotation = Thread
        .fromUnsafe(Seq(question))
        .map { thread =>
          Annotation(None, 0l, 1l, "This is an annotation", user, Some(thread))
        }

      expectedAnnotation.map { expected =>
        assertRight(expected = expected, actual = threadedAnnotation)
      }
    }
    "add it to the thread" in {
      val answer = Answer("An annotation is ...", user)
      val actualAnnotation = Thread.fromUnsafe(Seq(question)).flatMap { thread =>
        Annotation(None, 0l, 1l, "This is an annotation", user, Some(thread)).postComment(answer)
      }

      val expectedAnnotation = Thread
        .fromUnsafe(Seq(question, answer))
        .map { thread =>
          Annotation(None, 0l, 1l, "This is an annotation", user, Some(thread))
        }

      expectedAnnotation.map { expected =>
        assertRight(expected = expected, actual = actualAnnotation)
      }

    }
  }

  "Deleting a comment" should {
    val question = Question("What is an annotation?", user)
    "remove it from the thread" in {
      val answer = Answer("An annotation is ...", user)

      val expectedAnnotation = Thread.fromUnsafe(Seq(question)).map { thread =>
        Annotation(None, 0l, 1l, "This is an annotation", user, Some(thread))
      }

      val actualAnnotation = Thread.fromUnsafe(Seq(question, answer)).map { thread =>
        Annotation(None, 0l, 1l, "This is an annotation", user, Some(thread)).deleteComment(answer)
      }

      expectedAnnotation.map { expected =>
        assertRight(expected = expected, actual = actualAnnotation)
      }
    }
    "delete the thread" in {
      defaultAnnotation.postComment(question).map { actual =>
        assert(defaultAnnotation == actual.deleteComment(question))
      }
    }
  }

}
