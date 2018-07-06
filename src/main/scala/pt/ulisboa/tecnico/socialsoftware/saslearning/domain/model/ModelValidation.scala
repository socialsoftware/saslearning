package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

sealed trait ModelValidation {
  def entityType: EntityType
  def errorMessage: String
}

case class InsufficientAmountOfEntities(entityType: EntityType, entities: Int, minimumEntites: Int)
    extends ModelValidation {
  override def errorMessage: String =
    s"The model must have at least $minimumEntites entities, but only $entities are defined."
}

case class MaximumAmountOfEntitiesExceeded(entityType: EntityType,
                                           entities: Int,
                                           maximumEntites: Int)
    extends ModelValidation {
  override def errorMessage: String =
    s"The model can have a maximum of $maximumEntites entities, but $entities are defined."
}

case class InvalidEntityType(entityType: EntityType, expectedEntityType: EntityType)
    extends ModelValidation {
  override def errorMessage: String =
    s"The model were expecting an entity of type ${expectedEntityType.name}, buy received ${entityType.name}."
}
