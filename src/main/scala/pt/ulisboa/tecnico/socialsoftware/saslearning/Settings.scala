package pt.ulisboa.tecnico.socialsoftware.saslearning

import com.typesafe.config.{Config, ConfigFactory}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.oauth.Provider
import pureconfig._


case class Settings(port: Long, oauthProviders: Seq[Provider])

object Settings {
  def fromConfig(config: Config = ConfigFactory.load()): Settings = loadConfigOrThrow[Settings](config, "saslearning")
}