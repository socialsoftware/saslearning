package pt.ulisboa.tecnico.socialsoftware.domain.auth

/**
  * Created by david on 16/06/2017.
  */
case class OAuth2Settings(authorizationUrl: String, accessTokenUrl: String, refreshTokenUrl: Option[String] = None,
                          clientId: String, clientSecret: String, scope: Option[String])
