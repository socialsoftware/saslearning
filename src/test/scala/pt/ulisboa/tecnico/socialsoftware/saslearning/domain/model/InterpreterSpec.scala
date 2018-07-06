package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model.Node.MetaModelNode

class InterpreterSpec extends UnitSpec {
  val entityType: EntityType = EntityType(
    NonEmptyString("Scenario"),
    NonEmptyString("Scenarios can be concrete or general")
  )

  val root: MetaModelNode = MetaModelNode(
    NodeDescription(entityType, new Multiplicity(NonNegInt(1), Some(NonNegInt(1)))),
    Set.empty
  )

  val metaModel: MetaModel =
    MetaModel(NonEmptyString.unsafeFrom("Software Architecture Scenarios"), root)

  "Evaluating a model" when {
    "has no entities" must {
      "result in a failure" in {
        val model = metaModel.toModel
        model.evaluate.left.value should contain theSameElementsAs Vector(
          InsufficientAmountOfEntities(entityType, 0, 1)
        )
      }
    }
    "has one entity" must {
      "return a failure" when {
        "has a different entity type from MetaModel's root" in {
          val differentEntityType =
            EntityType(NonEmptyString("Stimulus"), NonEmptyString("Stimulus description"))
          val entity = Entity(differentEntityType)

          val node = metaModel.root.toModelNode.copy(data = Set(entity))
          Model(metaModel, node).evaluate.left.value should contain theSameElementsAs Vector(
            InvalidEntityType(differentEntityType, entityType)
          )
        }
      }
      "result in success" when {
        "has the same entity type that MetaModel's root" in {
          val entity = Entity(entityType)

          val node = metaModel.root.toModelNode.copy(data = Set(entity))
          Model(metaModel, node).evaluate should be('right)
        }
      }
    }
  }
}
