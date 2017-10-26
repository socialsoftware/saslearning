package pt.ulisboa.tecnico.socialsoftware.domain.interpreter

/**
  * EntityType represents a type of an [[Entity]], specified at runtime.
  *
  * @param name       the name of the type of the entity
  * @param attributes allowed attributes of this entity type
  */
case class EntityType(name: String, attributes: Seq[AttributeType])

object EntityType {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[EntityType] = deriveDecoder
  implicit val encodeJson: Encoder[EntityType] = deriveEncoder

}
