package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString

case class EntityType(name: NonEmptyString)

object EntityType {
  def apply(name: String): Either[String, EntityType] =
    for {
      checkedName <- NonEmptyString.from(name)
    } yield EntityType(checkedName)
}
