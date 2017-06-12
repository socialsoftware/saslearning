package pt.ulisboa.tecnico.socialsoftware.domain.auth

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future

/**
  * Created by david on 16/06/2017.
  */
abstract class OAuth2Provider {

  def doAuth()


}

class OAuth2Filter[Req, Rep] extends SimpleFilter[Req, Rep] {
  override def apply(request: Req, service: Service[Req, Rep]): Future[Rep] = ???
}