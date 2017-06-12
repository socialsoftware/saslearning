package pt.ulisboa.tecnico.socialsoftware.domain.auth

/**
  * The identification of an Identity.
  *
  * @param userId the user id on the provider (e.g. google, fenixedu, etc.)
  * @param providerId the provider used to sign in
  *
  * Created by david on 16/06/2017.
  */
case class IdentityId(userId: String, providerId: String)