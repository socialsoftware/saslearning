package pt.ulisboa.tecnico.socialsoftware.saslearning

import javax.mail.internet.{AddressException, InternetAddress}

import eu.timepit.refined.refineV
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty

import scala.util.control.Exception._

package object domain {

  type NonEmptySet[T] = Set[T] Refined NonEmpty

  def EmailAddress(string: String): Either[String, InternetAddress] = {
    val either = catching(classOf[AddressException]) either new InternetAddress(string, true)
    either.left.map(_.getLocalizedMessage)
  }

  def fromString[T](value: String)(f: NonEmptyString => T): Either[String, T] = {
    refineV[NonEmpty](value).map(str => f(str))
  }


}
