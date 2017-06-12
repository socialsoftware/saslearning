package pt.ulisboa.tecnico.socialsoftware

import com.google.api.client.util.store.MemoryDataStoreFactory
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.param.Stats
import com.twitter.server.TwitterServer
import com.twitter.util.Await

/**
  * Created by david on 07/06/2017.
  */
object SASLearning extends TwitterServer {

  val api: Service[Request, Response] = ???

  def main(): Unit = {
    val server = Http.server
      .configured(Stats(statsReceiver))
      .serve(":8080", api)

    onExit(server.close())

    Await.ready(adminHttpServer)
  }
}
