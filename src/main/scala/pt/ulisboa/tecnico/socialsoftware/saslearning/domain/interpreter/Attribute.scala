package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.interpreter

/**
  * Represents an instance of the attribute type.
  *
  * @param name the name of the instance
  * @param specification the type of the attribute
  * @param value the concrete value of the attribute
  */
case class Attribute(name: String, specification: AttributeType, value: String)

object Attribute {

  import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
  import io.circe.{ Decoder, Encoder }

  implicit val decodeJson: Decoder[Attribute] = deriveDecoder
  implicit val encodeJson: Encoder[Attribute] = deriveEncoder

}
