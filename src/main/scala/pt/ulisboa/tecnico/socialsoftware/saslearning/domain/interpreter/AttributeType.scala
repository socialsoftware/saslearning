package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.interpreter

/**
  * AttributeType is used as per Property methodology of Adaptive Object Model. As such,
  * the attributes of a class should be held as a collection of properties instead of
  * single instance variables.
  *
  * @param name the name of attribute type
  * @param typeOf the class name of the type of the attribute
  */
case class AttributeType(name: String, typeOf: String)

object AttributeType {

  import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
  import io.circe.{ Decoder, Encoder }

  implicit val decodeJson: Decoder[AttributeType] = deriveDecoder
  implicit val encodeJson: Encoder[AttributeType] = deriveEncoder
}
