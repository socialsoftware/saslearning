package pt.ulisboa.tecnico.socialsoftware.domain.oauth

import java.net.URI

import io.circe.Json
import pt.ulisboa.tecnico.socialsoftware.domain.User

case class Provider(name: String, site: URI,
                    id: String, secret: String,
                    authorizePath: String = "/oauth/authorize",
                    tokenPath: String = "/oauth/token",
                    userProfileEndpoint: URI,
                    fields: OAuthUserFields) {

  val authorizationUrl: URI = URI.create(s"${site.toString}$authorizePath")
  val accessTokenUrl: URI = URI.create(s"${site.toString}$tokenPath")

  def getUser(json: Json): Option[User] = {
    val cursor = json.hcursor

    (for {
      username <- cursor.downField(fields.username).as[String]
      email <- cursor.downField(fields.email).as[String]
      name <- cursor.downField(fields.displayName).as[String]
    } yield User(None, username, email, name)).toOption
  }

}

case class OAuthUserFields(username: String, email: String, displayName: String)