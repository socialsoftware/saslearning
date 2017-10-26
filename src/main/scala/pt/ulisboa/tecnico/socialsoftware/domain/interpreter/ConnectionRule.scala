package pt.ulisboa.tecnico.socialsoftware.domain.interpreter

/**
  *
  * @param name
  * @param parent
  * @param child
  * @param parentMultiplicity
  * @param childMultiplicity
  * @param required
  */
case class ConnectionRule(name: String,
                          parent: EntityType, child: EntityType,
                          parentMultiplicity: Multiplicity, childMultiplicity: Multiplicity,
                          required: Boolean)

object ConnectionRule {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[ConnectionRule] = deriveDecoder
  implicit val encodeJson: Encoder[ConnectionRule] = deriveEncoder
}
