package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString

case class MetaModel(title: NonEmptyString, description: NonEmptyString, root: EntityType)

object MetaModel {
  def apply(title: String, description: String, root: EntityType): Either[String, MetaModel] =
    for {
      nonEmptyTitle <- NonEmptyString.from(title)
      nonEmptyDescription <- NonEmptyString.from(description)
    } yield new MetaModel(nonEmptyTitle, nonEmptyDescription, root)
}
