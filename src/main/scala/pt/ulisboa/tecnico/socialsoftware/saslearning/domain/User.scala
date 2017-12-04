package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import io.circe.Json

case class User(id: Option[Long], username: String, email: String, displayName: String) extends WithId {
  def createTeam(name: String, owners: Set[User] = Set.empty, members: Set[User] = Set.empty): Team =
    Team(name, owners + this, members)
}

object User {

  import io.circe.{Decoder, Encoder}

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

  def apply(json: Json,
            usernameField: String = USERNAME,
            emailField: String = EMAIL,
            displayNameField: String = DISPLAY_NAME): Option[User] = {
    val cursor = json.hcursor

    (for {
      username <- cursor.downField(usernameField).as[String]
      email <- cursor.downField(emailField).as[String]
      name <- cursor.downField(displayNameField).as[String]
    } yield User(None, username, email, name)).toOption
  }
}
