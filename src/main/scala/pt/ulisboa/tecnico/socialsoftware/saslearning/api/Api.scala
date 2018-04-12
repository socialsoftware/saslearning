package pt.ulisboa.tecnico.socialsoftware.saslearning.api

import com.twitter.util.Future
import io.finch.{ headerOption, Endpoint, Forbidden, Ok }
import pdi.jwt.JwtAlgorithm
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.User
import pt.ulisboa.tecnico.socialsoftware.saslearning.services.JwtService

trait Api[A] {

  def jwtService: JwtService = JwtService("uma password", JwtAlgorithm.HS512)

  def authorize: Endpoint[User] =
    headerOption("Authorization").mapOutputAsync {
      case Some(header) if header.startsWith("Bearer") =>
        val _ :: token :: Nil = header.split(" ").toList
        jwtService.validate(token).map(Ok)
      case _ =>
        Future.value(Forbidden(new Exception("Header Authorization is missing.")))
    }

  def endpoints: Endpoint[A]
}
