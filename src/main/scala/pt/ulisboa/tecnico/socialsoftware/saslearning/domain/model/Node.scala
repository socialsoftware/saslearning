package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

trait Node[T] {
  def description: NodeDescription
  def children: Set[T]
}

object Node {
  case class ModelNode(data: Set[Entity], description: NodeDescription, children: Set[ModelNode])
      extends Node[ModelNode] {

    def evaluate: Either[List[ModelValidation], Unit] = {
      def nodeEvaluator(node: ModelNode): List[ModelValidation] = {
        val invalidEntityTypeList = node.data
          .filterNot(_.entityType == node.description.entityType)
          .map(elem => InvalidEntityType(elem.entityType, node.description.entityType))
          .toList

        val entitiesWithoutMinimum =
          if (node.data.size >= node.description.multiplicity.lowerBound.value) {
            List.empty[ModelValidation]
          } else {
            List(
              InsufficientAmountOfEntities(node.description.entityType,
                                           node.data.size,
                                           node.description.multiplicity.lowerBound.value)
            )
          }

        val entitiesWithMoreThanMaximum =
          node.description.multiplicity.upperBound.fold(List.empty[ModelValidation]) { bound =>
            if (node.data.size <= bound.value) {
              List.empty
            } else {
              List(
                MaximumAmountOfEntitiesExceeded(node.description.entityType,
                                                node.data.size,
                                                bound.value)
              )
            }
          }

        invalidEntityTypeList ++ entitiesWithoutMinimum ++ entitiesWithMoreThanMaximum ++ children
          .flatMap(nodeEvaluator)
      }

      val result = nodeEvaluator(this)

      Either.cond(result.isEmpty, (), result)
    }

  }

  case class MetaModelNode(description: NodeDescription, children: Set[MetaModelNode])
      extends Node[MetaModelNode] {
    def instantiate: ModelNode = ModelNode(Set.empty, description, children.map(_.instantiate))
  }

}
