package pt.ulisboa.tecnico.socialsoftware.saslearning.api

import io.finch._
import io.finch.syntax._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{ Database, User }
import shapeless.{ :+:, CNil }

case class UserApi() extends Api[User :+: Seq[User] :+: CNil] {

  private val prefix = "users"

  private def user: Endpoint[User] = get(prefix :: path[String] :: authorize) {
    (username: String, _: User) =>
      Database.getUser(username).map(Ok)
  }

  private def users: Endpoint[Seq[User]] = get(prefix :: authorize) { _: User =>
    Database.getUsers.map(Ok)
  }

  override def endpoints: Endpoint[User :+: Seq[User] :+: CNil] = user :+: users

}
