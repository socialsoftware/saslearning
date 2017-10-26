package pt.ulisboa.tecnico.socialsoftware

import com.twitter.finagle.Http
import com.twitter.finagle.param.Stats
import com.twitter.server.TwitterServer
import com.twitter.util.Await

/**
  * Created by david on 07/06/2017.
  */
object SASLearning extends TwitterServer {

  def main(): Unit = {
    val server = Http.server
      .configured(Stats(statsReceiver))
      .serve(":8081", api.endpoints)

    onExit {
      server.close()
    }

    Await.ready(adminHttpServer)
  }
}
