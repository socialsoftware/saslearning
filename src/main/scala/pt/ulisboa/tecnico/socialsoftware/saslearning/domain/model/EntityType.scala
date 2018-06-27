package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString

case class EntityType(name: NonEmptyString,
                      connections: Map[ConnectionRule, EntityType] = Map.empty)

object EntityType {
  def apply(name: String,
            connections: Map[ConnectionRule, EntityType]): Either[String, EntityType] =
    for {
      nonEmptyName <- NonEmptyString.from(name)
    } yield new EntityType(nonEmptyName, connections)

  def apply(name: String): Either[String, EntityType] = EntityType(name, Map.empty)
}
