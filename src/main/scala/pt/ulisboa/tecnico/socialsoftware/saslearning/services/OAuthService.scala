package pt.ulisboa.tecnico.socialsoftware.saslearning.services

import java.net.URI

import io.circe.{Decoder, HCursor, Json}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.User
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.oauth.{Provider, Token}

case class OAuthService(client: Provider) {

  val redirectUri: URI = URI.create(
    s"http://localhost:8081/${client.name.toLowerCase.trim}/callback"
  )

  def userFromOAuth(authInfo: Token): Option[User] = {
    import io.circe.parser.parse

    import scalaj.http.Http

    val response = Http(client.userProfileEndpoint.toString)
      .header("Authorization", s"Bearer ${authInfo.accessToken}")
      .asString
      .body

    val doc = parse(response).getOrElse(Json.Null)

    client.getUser(doc)
  }

  def requestAccessToken(code: String): Option[Token] = {
    import io.circe.parser._

    import scalaj.http.Http

    val response = Http(client.accessTokenUrl.toString)
      .postForm
      .param("client_id", client.id)
      .param("client_secret", client.secret)
      .param("redirect_uri", redirectUri.toString)
      .param("code", code)
      //      .param("state", "TODO")
      .param("grant_type", "authorization_code")
      .asString
      .body

    // FIXME authorization_code and scope
    // FIXME handle unauthorization

    parse(response)
      .toOption
      .flatMap(_.as[Token].toOption)
  }

  // TODO: private def refreshAccessToken(): Unit = ???

  private implicit val decodeOAuthInfo: Decoder[Token] = (c: HCursor) => for {
    accessToken <- c.downField("access_token").as[String]
    refreshToken <- c.downField("refresh_token").as[Option[String]]
    expiresIn <- c.downField("expires_in").as[Int]
  } yield {
    Token(accessToken, refreshToken, expiresIn, client) //FIXME now + expiresIn
  }

}
