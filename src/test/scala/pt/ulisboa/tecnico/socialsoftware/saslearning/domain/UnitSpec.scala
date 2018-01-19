package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import javax.mail.internet.InternetAddress

import eu.timepit.refined.auto._
import org.scalatest.{Assertion, EitherValues, Matchers, WordSpec}

abstract class UnitSpec extends WordSpec
  with Matchers
  with EitherValues {

  val user = User(None, "test", new InternetAddress("john.doe@example.org"), "John Doe")

  def assertLeft[T](actual: Either[String, T]): Assertion = actual should be ('left)

  def assertRight[T](expected: T, actual: Either[String, T]): Assertion = {
    actual should be('right)
    actual.right.value should be(expected)
  }
}
