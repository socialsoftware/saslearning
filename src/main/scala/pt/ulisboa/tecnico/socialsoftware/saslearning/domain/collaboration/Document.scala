package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import java.net.URI

import eu.timepit.refined._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{User, WithId}

case class Document(id: Option[Long],
                    title: NonEmptyString, source: URI, content: NonEmptyString,
                    creator: User) extends WithId

object Document {

  def fromUnsafe(id: Option[Long],
                 title: String, source: URI, content: String,
                 creator: User): Either[String, Document] = for {
    title <- refineV[NonEmpty](title)
    content <- refineV[NonEmpty](content)
  } yield new Document(id, title, source, content, creator)

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}
  import io.circe.refined._
  import pt.ulisboa.tecnico.socialsoftware.saslearning.utils.JsonUtils.{uriEncoder, uriDecoder}

  lazy implicit val decodeJson: Decoder[Document] = deriveDecoder
  lazy implicit val encodeJson: Encoder[Document] = deriveEncoder[Document]
  lazy implicit val decodePartialJson: Decoder[User => Document] = deriveDecoder[User => Document]

}
