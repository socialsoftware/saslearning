package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.interpreter

/**
  * Multiplicity represents an interval with a lower and upper bound.
  *
  * The following rules must be valid when specifying a multiplicity:
  *  - lower bound must be equal or greater than zero;
  *  - upper bound must be equal or greater than zero;
  *  - upper bound must be equal or greater than lower bound.
  *
  * @param lower the minimum value
  * @param upper the maximum value
  */
case class Multiplicity(lower: Int, upper: Int) {
  require(lower > -1, s"$lower should be >= 0")
  require(upper > -1, s"$upper should be >= 0")
  require(upper >= lower, s"$upper should be >= $lower")
}

object Multiplicity {

  import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
  import io.circe.{ Decoder, Encoder }

  implicit val decodeJson: Decoder[Multiplicity] = deriveDecoder
  implicit val encodeJson: Encoder[Multiplicity] = deriveEncoder
}
