package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.interpreter

/**
  *
  * @param name
  * @param rules
  */
case class AccountabilityType(name: String, rules: Seq[ConnectionRule])

object AccountabilityType {

  import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
  import io.circe.{ Decoder, Encoder }

  implicit val decodeJson: Decoder[AccountabilityType] = deriveDecoder
  implicit val encodeJson: Encoder[AccountabilityType] = deriveEncoder
}
