package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.oauth

case class Token(accessToken: String, refreshToken: Option[String], expiresIn: Int, client: Provider)
