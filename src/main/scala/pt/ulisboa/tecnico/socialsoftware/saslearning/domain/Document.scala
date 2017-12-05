package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import java.net.URI

case class Document(id: Option[Long],
                    title: String, source: URI, content: String,
                    creator: User) extends WithId

object Document {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  import pt.ulisboa.tecnico.socialsoftware.saslearning.utils.JsonUtils._

  lazy implicit val decodeJson: Decoder[Document] = deriveDecoder[Document]
  lazy implicit val encodeJson: Encoder[Document] = deriveEncoder[Document]
  lazy implicit val decodePartialJson: Decoder[User => Document] = deriveDecoder[User => Document]

}

