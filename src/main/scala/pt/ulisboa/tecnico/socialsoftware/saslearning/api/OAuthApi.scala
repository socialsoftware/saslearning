package pt.ulisboa.tecnico.socialsoftware.saslearning.api

import com.twitter.finagle.http.Status
import io.finch._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.Database
import pt.ulisboa.tecnico.socialsoftware.saslearning.services.OAuthService
import shapeless.{:+:, CNil}

case class OAuthApi(oauthService: OAuthService) extends Api[Unit :+: String :+: CNil] {
  private val client = oauthService.client
  private val prefix = client.name.toLowerCase.trim

  def register: Endpoint[Unit] = get(prefix :: "register") {
    val destination =
      s"${client.authorizationUrl}?client_id=${client.id}&redirect_uri=${oauthService.redirectUri.toString}"

    Output
      .unit(Status.SeeOther)
      .withHeader("Location" -> destination)
  }

  def callback: Endpoint[String] = get(prefix :: "callback" :: param("code")) { code: String =>
    (for {
      token <- oauthService.requestAccessToken(code)
      user <- oauthService.userFromOAuth(token)
    } yield {
      // Se o user não existe, guarda-o na BD mais o token
      // Else, actualiza o access token e gera um novo JWT

      Database.accessTokenToOAuthInfoMap.put(token.accessToken, token)
      Database.addUser(user)
      Database.accessTokenToUserMap.put(token.accessToken, user)

      //      Output.unit(Status.SeeOther)
      //        .withHeader("Location" -> "/")
      //        .withHeader("Token" -> jwtService.generateUserToken(user.username))
      //    }).getOrElse(Unauthorized(new Exception(s"Could not get a valid token")))
      Ok(jwtService.generateUserToken(user.username))
    }).getOrElse(Unauthorized(new Exception(s"Could not get a valid token")))
  }

  override def endpoints: Endpoint[Unit :+: String :+: CNil] = register :+: callback
}
