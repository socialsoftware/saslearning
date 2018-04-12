package pt.ulisboa.tecnico.socialsoftware.saslearning

import com.twitter.finagle.Service
import com.twitter.finagle.http.{ Request, Response }
import io.circe.Json
import io.circe.generic.auto._
import io.finch._
import io.finch.syntax._
import io.finch.circe._
import pt.ulisboa.tecnico.socialsoftware.saslearning.services.OAuthService

package object api {

  //  private val oauthEndpoints = {
  //    OAuthApi(OAuthService.forFenixEdu).endpoints /* :+: OAuthApi(OAuthService.forGitHub, jwtService).endpoints*/
  //  }

  implicit val settings: Settings = Settings.fromConfig()

  private def oauthEndpoints(implicit settings: Settings) =
    settings.oauthProviders
      .map { provider =>
        OAuthApi(OAuthService(provider)).endpoints
      }
      .reduceRight(_.coproduct(_))

  private val userEndpoints = UserApi()
  private val documentEndpoints = DocumentApi()
  private val annotationEndpoints = AnnotationApi()

  private val health: Endpoint[Json] = get("health") {
    Ok(Json.fromString("ok"))
  }

  private val unauthorizedEndpoints = oauthEndpoints :+: health

  private val protectedEndpoints = annotationEndpoints.endpoints :+: documentEndpoints.endpoints :+: userEndpoints.endpoints

  private val combined = (unauthorizedEndpoints :+: protectedEndpoints).handle {
    case e: IllegalArgumentException =>
      //      log.error("Bad request from client", e)
      BadRequest(e)
    case t: Throwable =>
      //      log.error("Unexpected exception", t)
      InternalServerError(new Exception(t.getCause))
  }

  val endpoints: Service[Request, Response] = combined.toServiceAs[Application.Json]

}
