package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model.Node.{ MetaModelNode, ModelNode }

class MetaModelSpec extends UnitSpec {
  val title: String = "Software Archictures Scenarios"
  val entityType: EntityType =
    EntityType(NonEmptyString("Scenario"), NonEmptyString("Scenarios can be concrete or general"))

  val root: MetaModelNode = MetaModelNode(
    NodeDescription(entityType, new Multiplicity(NonNegInt(1), Some(NonNegInt(1)))),
    Set.empty
  )

  "A meta model" must {
    "have a title" in {
      val model = MetaModel(title, root)

      model should be('right)
      model.right.value.title.value shouldEqual title
    }
  }

  it can {
    "not have a title" in {
      MetaModel("", root) should be('left)
    }
  }

  it should {
    "return an empty model" in {
      val metaModel = MetaModel(NonEmptyString("Software Archictures Scenarios"), root)
      val model = Model(metaModel, ModelNode(Set.empty, root.description, Set.empty))

      metaModel.instantiate should equal(model)

    }
  }
}
