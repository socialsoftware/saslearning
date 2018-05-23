package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.numeric.NonNegInt

import scala.util.Try

case class Multiplicity(lowerBound: NonNegInt, upperBound: Option[NonNegInt]) {
  upperBound.foreach(upper => require(upper.value >= lowerBound.value))

  def isInfinite: Boolean = upperBound.isEmpty
}

object Multiplicity {
  private def apply(lowerBound: NonNegInt,
                    upperBound: Option[NonNegInt]): Either[String, Multiplicity] =
    Try(new Multiplicity(lowerBound, upperBound)).toEither.left
      .map(_ => "Predicate upper bound >= lower bound failed.")

  def apply(lowerBound: Int, upperBound: Int): Either[String, Multiplicity] =
    NonNegInt
      .from(lowerBound)
      .flatMap(
        lower => NonNegInt.from(upperBound).flatMap(upper => Multiplicity(lower, Some(upper)))
      )

  def apply(lowerBound: Int): Either[String, Multiplicity] =
    NonNegInt
      .from(lowerBound)
      .flatMap(lower => Multiplicity(lower, None))
}
