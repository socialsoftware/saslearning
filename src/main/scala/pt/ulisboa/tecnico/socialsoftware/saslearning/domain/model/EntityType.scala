package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString

case class EntityType(name: NonEmptyString, hint: NonEmptyString)

object EntityType {
  def apply(name: String, hint: String): Either[String, EntityType] =
    for {
      nonEmptyName <- NonEmptyString.from(name)
      nonEmptyHint <- NonEmptyString.from(hint)
    } yield EntityType(nonEmptyName, nonEmptyHint)
}
