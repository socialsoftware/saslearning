package pt.ulisboa.tecnico.socialsoftware.saslearning.api

import io.finch.{Endpoint, Ok, get, path}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{Database, User}
import shapeless.{:+:, CNil}

case class UserApi() extends Api[User :+: Seq[User] :+: CNil] {

  private val prefix = "users"

  private def user: Endpoint[User] = get(prefix :: path[String] :: authorize) { (username: String, user: User) =>
    Database.getUser(username).map(Ok)
  }

  private def users: Endpoint[Seq[User]] = get(prefix :: authorize) { (user: User) =>
    Database.getUsers.map(Ok)
  }

  override def endpoints: Endpoint[User :+: Seq[User] :+: CNil] = user :+: users

}
