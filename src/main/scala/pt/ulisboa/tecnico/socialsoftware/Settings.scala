package pt.ulisboa.tecnico.socialsoftware

import com.typesafe.config.{Config, ConfigFactory}
import pt.ulisboa.tecnico.socialsoftware.domain.oauth.Provider
import pureconfig._


case class Settings(port: Long, oauthProviders: Seq[Provider])

object Settings {
  def fromConfig(config: Config = ConfigFactory.load()): Settings = loadConfigOrThrow[Settings](config, "saslearning")
}