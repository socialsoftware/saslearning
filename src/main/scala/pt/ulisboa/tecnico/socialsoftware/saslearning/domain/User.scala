package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

case class User(id: Option[Long], username: String, email: String, displayName: String) extends WithId {
  def createTeam(name: String, owners: Set[User] = Set.empty, members: Set[User] = Set.empty): Team =
    Team(name, owners + this, members)
}

object User {

  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.{Decoder, Encoder}

  implicit val decodeJson: Decoder[User] = deriveDecoder[User]
  implicit val encodeJson: Encoder[User] = deriveEncoder[User]
}
