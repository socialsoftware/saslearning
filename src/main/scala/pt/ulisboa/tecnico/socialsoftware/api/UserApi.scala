package pt.ulisboa.tecnico.socialsoftware.api

import io.finch._
import pt.ulisboa.tecnico.socialsoftware.domain.{Database, User}
import shapeless.{:+:, CNil}

case class UserApi() extends Api[User :+: Seq[User] :+: CNil] {

  private val path = "user"

  private def user: Endpoint[User] = get(path :: string :: authorize) { (username: String, user: User) =>
    Database.getUser(username).map(Ok)
  }

  private def users: Endpoint[Seq[User]] = get(path :: authorize) { (user: User) =>
    Database.getUsers.map(Ok)
  }

  override def endpoints: Endpoint[User :+: Seq[User] :+: CNil] = user :+: users

}
