package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString

case class EntityType(name: NonEmptyString, rules: Map[ConnectionRule, EntityType] = Map.empty)

object EntityType {
  def apply(name: String, rules: Map[ConnectionRule, EntityType]): Either[String, EntityType] =
    for {
      nonEmptyName <- NonEmptyString.from(name)
    } yield new EntityType(nonEmptyName, rules)

  def apply(name: String): Either[String, EntityType] = EntityType(name, Map.empty)
}
