package pt.ulisboa.tecnico.socialsoftware.domain.interpreter


/**
  *
  * @param name
  * @param definition
  * @param parents
  * @param children
  */
case class Accountability(name: String, definition: AccountabilityType,
                          parents: Seq[Entity], children: Seq[Entity])

object Accountability {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[Accountability] = deriveDecoder
  implicit val encodeJson: Encoder[Accountability] = deriveEncoder

}