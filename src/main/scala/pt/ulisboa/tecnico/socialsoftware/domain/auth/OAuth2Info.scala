package pt.ulisboa.tecnico.socialsoftware.domain.auth

/**
  * Details for OAuth2.
  *
  * @param accessToken the access token
  * @param refreshToken the refresh token, if provided
  * @param expiresIn the number of seconds before the token expires, if provided
  *
  * Created by david on 16/06/2017.
  */
case class OAuth2Info(accessToken: String, refreshToken: Option[String] = None, expiresIn: Option[Int] = None)

// TODO convert expiresIn to seconds