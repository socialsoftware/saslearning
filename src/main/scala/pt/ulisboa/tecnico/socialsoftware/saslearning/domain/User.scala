package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import eu.timepit.refined._
import eu.timepit.refined.collection.NonEmpty

case class User(id: Option[Long] = None,
                username: NonEmptyString, email: NonEmptyString, displayName: NonEmptyString) extends WithId {

  def createTeam(name: String, owners: Set[User] = Set.empty, members: Set[User] = Set.empty): Either[String, Team] =
    Team.fromUnsafe(name, owners + this, members)

}

object User {

  def fromUnsafe(id: Option[Long] = None, username: String, email: String, displayName: String): Either[String, User] =
    for {
      username <- refineV[NonEmpty](username)
      email <- refineV[NonEmpty](email)
      displayName <- refineV[NonEmpty](displayName)
    } yield new User(id, username, email, displayName)

  import io.circe.{Decoder, Encoder, Json}
  import io.circe.refined._

  private val ID = "id"
  private val USERNAME = "username"
  private val EMAIL = "email"
  private val DISPLAY_NAME = "display_name"

  implicit val decodeJson: Decoder[User] =
    Decoder.forProduct4(ID, USERNAME, EMAIL, DISPLAY_NAME)(User.apply)

  implicit val encodeJson: Encoder[User] =
    Encoder.forProduct4(ID, USERNAME, EMAIL, DISPLAY_NAME)(u =>
      (u.id, u.username, u.email, u.displayName)
    )

  def fromJson(json: Json,
               usernameField: String = USERNAME,
               emailField: String = EMAIL,
               displayNameField: String = DISPLAY_NAME): Option[User] = {
    val cursor = json.hcursor

    (for {
      username <- cursor.downField(usernameField).as[String]
      email <- cursor.downField(emailField).as[String]
      name <- cursor.downField(displayNameField).as[String]
    } yield User.fromUnsafe(username = username, email = email, displayName = name).toOption).toOption.flatten
  }
}
