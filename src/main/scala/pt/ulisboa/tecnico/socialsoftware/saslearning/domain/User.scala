package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import javax.mail.internet.InternetAddress

import eu.timepit.refined._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.string.NonEmptyString

import scala.collection.immutable.Seq

case class User(id: Option[Long] = None,
                username: NonEmptyString, email: InternetAddress, displayName: NonEmptyString) extends WithId {

  def createTeam(name: String, owners: Set[User] = Set.empty, members: Set[User] = Set.empty): Either[String, Team] =
    Team.fromUnsafe(name, Seq.empty, owners + this, members)

}

object User {

  def fromUnsafe(id: Option[Long] = None, username: String, email: String, displayName: String): Either[String, User] =
    for {
      username <- refineV[NonEmpty](username)
      email <- EmailAddress(email)
      displayName <- refineV[NonEmpty](displayName)
    } yield new User(id, username, email, displayName)

  import io.circe.{Decoder, Encoder, Json}
  import io.circe.generic.extras._
  import io.circe.generic.extras.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.refined._
  import pt.ulisboa.tecnico.socialsoftware.saslearning.utils.JsonUtils.{internetAddressDecoder,internetAddressEncoder}

  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val decodeJson: Decoder[User] = deriveDecoder
  implicit val encodeJson: Encoder[User] = deriveEncoder


  def fromJson(json: Json,
               usernameField: String = "username",
               emailField: String = "email",
               displayNameField: String = "display_name"): Option[User] = {
    val cursor = json.hcursor

    (for {
      username <- cursor.downField(usernameField).as[String]
      email <- cursor.downField(emailField).as[String]
      name <- cursor.downField(displayNameField).as[String]
    } yield User.fromUnsafe(username = username, email = email, displayName = name).toOption).toOption.flatten
  }
}
