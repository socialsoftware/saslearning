package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.Annotation
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
    "have no entities" must {
      "result in a failure" in {
        val model = metaModel.instantiate
        model.evaluate.left.value should contain theSameElementsAs Vector(
          InsufficientAmountOfEntities(entityType, 0, 1)
        )
      }
    }
    "have one entity" must {
      "return a failure" when {
        "have a different entity type from MetaModel's root" in {
          val differentEntityType =
            EntityType(NonEmptyString("Stimulus"), NonEmptyString("Stimulus description"))
          val entity = Entity(differentEntityType, defaultAnnotation)

          val node = metaModel.root.instantiate.copy(data = Set(entity))
          Model(metaModel, node).evaluate.left.value should contain theSameElementsAs Vector(
            InvalidEntityType(differentEntityType, entityType)
          )
        }
      }
      "have more than upper bound of multiplicity" must {
        "return a failure" in {
          val anotherAnnotation = Annotation(None, 10l, 1l, "This is another annotation", user)

          val entity = Entity(entityType, defaultAnnotation)
          val anotherEntity = Entity(entityType, anotherAnnotation)

          val node = metaModel.root.instantiate.copy(data = Set(entity, anotherEntity))
          Model(metaModel, node).evaluate.left.value should contain theSameElementsAs Vector(
            MaximumAmountOfEntitiesExceeded(entityType, 2, 1)
          )
        }
      }
      "result in success" when {
        "have the same entity type that MetaModel's root" in {
          val entity = Entity(entityType, defaultAnnotation)

          val node = metaModel.root.instantiate.copy(data = Set(entity))
          Model(metaModel, node).evaluate should be('right)
        }
      }
    }
  }
}
