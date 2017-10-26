package pt.ulisboa.tecnico.socialsoftware.services

import com.twitter.util.Future
import pdi.jwt.algorithms.JwtHmacAlgorithm
import pdi.jwt.{Jwt, JwtCirce, JwtClaim, JwtOptions}
import pt.ulisboa.tecnico.socialsoftware.domain.{Database, User}

import scala.util.{Failure, Success}

case class JwtService(secretKey: String, algorithm: JwtHmacAlgorithm) {

  def validate(token: String): Future[User] = {
    JwtCirce.decodeJson(token, secretKey, Seq(algorithm), JwtOptions(leeway = 30)) flatMap { json =>
      json.hcursor.downField("user").as[String].toTry
    } match {
      case Success(username) =>
        Database.getUser(username)
      case Failure(cause) =>
        Future.exception(cause)
    }
  }

  // TODO: add if it is admin
  def generateUserToken(username: String): String = {
    val header = JwtClaim(s"""{"user":"$username"}""").issuedNow.expiresIn(1800) // TODO set issuer
    val token = Jwt.encode(header, secretKey, algorithm)

    Database.usernameToJwtTokenMap.put(token, username)

    token
  }

}
