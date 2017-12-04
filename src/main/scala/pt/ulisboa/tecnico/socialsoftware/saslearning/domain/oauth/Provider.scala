package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.oauth

import java.net.URI

case class Provider(name: String, site: URI,
                    id: String, secret: String,
                    authorizePath: String = "/oauth/authorize",
                    tokenPath: String = "/oauth/token",
                    userProfileEndpoint: URI,
                    fields: OAuthUserFields) {

  val authorizationUrl: URI = URI.create(s"${site.toString}$authorizePath")
  val accessTokenUrl: URI = URI.create(s"${site.toString}$tokenPath")
}

case class OAuthUserFields(username: String, email: String, displayName: String)