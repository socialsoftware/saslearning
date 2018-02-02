package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import java.net.URI
import javax.mail.internet.InternetAddress

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalatest.{Assertion, EitherValues, Matchers, WordSpec}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.{Annotation, Document}

abstract class UnitSpec extends WordSpec
  with Matchers
  with EitherValues {

  val user: User = User(None, "test", new InternetAddress("john.doe@example.org"), "John Doe")

  val defaultUri: URI = URI.create("http://example.org")
  val defaultTitle: NonEmptyString = Refined.unsafeApply("The history of Art")
  val defaultContent: NonEmptyString = Refined.unsafeApply("Content goes here")
  val defaultDocument: Document = Document(None, defaultTitle, defaultUri, defaultContent, user)

  val defaultAnnotation = Annotation(None, 0l, 1l, "This is an annotation", user)

  def assertLeft[T](actual: Either[String, T]): Assertion = actual should be ('left)

  def assertRight[T](expected: T, actual: Either[String, T]): Assertion = {
    actual should be('right)
    actual.right.value should be(expected)
  }
}
