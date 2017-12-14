package pt.ulisboa.tecnico.socialsoftware.saslearning

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.{NonNegative, Positive => RPositive}

package object domain {

  type Natural = Long Refined NonNegative
  type Positive = Long Refined RPositive
  type NonEmptyString = String Refined NonEmpty

  def Natural(a: Long): Either[String, Natural] = refineV[NonNegative](a)
  def Positive(a: Long): Either[String, Positive] = refineV[RPositive](a)
  def NonEmptyString(string: String): Either[String, NonEmptyString] = refineV[NonEmpty](string)

  def fromString[T](value: String)(f: NonEmptyString => T): Either[String, T] = {
    refineV[NonEmpty](value).map(str => f(str))
  }


}
