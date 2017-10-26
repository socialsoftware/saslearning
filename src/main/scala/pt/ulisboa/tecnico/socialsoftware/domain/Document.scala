package pt.ulisboa.tecnico.socialsoftware.domain

import java.net.URI

case class Document(id: Option[Long],
                    title: String, source: URI, content: String,
                    owner: User /*, groups: Seq[Group]*/) extends WithId

object Document {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}
  import pt.ulisboa.tecnico.socialsoftware.utils.JsonUtils._

  implicit val decodeJson: Decoder[Document] = deriveDecoder[Document]
  implicit val encodeJson: Encoder[Document] = deriveEncoder[Document]
  implicit val decodePartialJson: Decoder[User => Document] = deriveDecoder[User => Document]

}

