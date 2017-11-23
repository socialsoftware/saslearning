package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.interpreter

/**
  * Entity represents an instance of [[EntityType]].
  *
  * @param name       the name of the entity instance.
  * @param attributes the actual attributes of this entity instance.
  */
case class Entity(name: String, attributes: Seq[Attribute])

object Entity {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[Entity] = deriveDecoder
  implicit val encodeJson: Encoder[Entity] = deriveEncoder

}
