package pt.ulisboa.tecnico.socialsoftware.api

import com.twitter.util.Future
import io.finch._
import pdi.jwt.JwtAlgorithm
import pt.ulisboa.tecnico.socialsoftware.domain.User
import pt.ulisboa.tecnico.socialsoftware.services.JwtService

trait Api[A] {

  val jwtService: JwtService = JwtService("uma password", JwtAlgorithm.HS512)

  def authorize: Endpoint[User] = {
    headerOption("Authorization").mapOutputAsync {
      case Some(header) if header.startsWith("Bearer") =>
        val _ :: token :: Nil = header.split(" ").toList
        jwtService.validate(token).map(Ok)
      case _ =>
        Future.value(Forbidden(new Exception("Header Authorization is missing.")))
    }
  }

  def endpoints: Endpoint[A]
}
