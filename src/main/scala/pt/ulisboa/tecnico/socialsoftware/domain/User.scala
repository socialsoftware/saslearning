package pt.ulisboa.tecnico.socialsoftware.domain

import pt.ulisboa.tecnico.socialsoftware.domain.auth.{IdentityId, OAuth2Info}

/**
  * User information gathered from OAuth2 provider when the user signs up.
  *
  * @param firstName the user's first name
  * @param lastName the user' last name
  * @param email the user's email
  * @param identityId pair (userId, providerId) associated with the user
  * @param oAuth2Info the OAuth2 information associated with the user
  *
  * Created by david on 16/06/2017.
  */
case class User(firstName: String, lastName: String, email: Option[String], identityId: IdentityId, oAuth2Info: OAuth2Info)

// TODO allow linking multiple OAuth2 providers