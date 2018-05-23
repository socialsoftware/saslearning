package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import java.net.URI
import javax.mail.internet.InternetAddress

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalatest.{ Assertion, EitherValues, Matchers, WordSpec }
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.{ Annotation, Document }

abstract class UnitSpec extends WordSpec with Matchers with EitherValues {

  def notWord: AfterWord = afterWord("not")

  def user: User = User(None, "test", new InternetAddress("john.doe@example.org"), "John Doe")

  def defaultUri: URI = URI.create("http://example.org")
  def defaultTitle: NonEmptyString = Refined.unsafeApply("The history of Art")
  def defaultContent: NonEmptyString = Refined.unsafeApply("Content goes here")
  def defaultDocument: Document = Document(None, defaultTitle, defaultUri, defaultContent, user)

  def defaultAnnotation = Annotation(None, 0l, 1l, "This is an annotation", user)

  def assertLeft[T](actual: Either[String, T]): Assertion = actual should be('left)

  def assertRight[T](expected: T, actual: Either[String, T]): Assertion = {
    actual should be('right)
    actual.right.value should be(expected)
  }
}
