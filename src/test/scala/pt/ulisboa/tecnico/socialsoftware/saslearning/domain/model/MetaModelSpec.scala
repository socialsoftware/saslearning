package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class MetaModelSpec extends UnitSpec {
  val title: String = "Software Archictures Scenarios"
  val description: String = "Scenarios can be concrete or general"
  val entityType: EntityType = EntityType(NonEmptyString.unsafeFrom("Tactic"))

  "A meta model" must {
    "have a title, a description and at least one entity type" in {
      val model = MetaModel(title, description, entityType)

      model should be('right)
      model.right.get.title.value shouldEqual title
      model.right.get.description.value shouldEqual description
    }
  }
  it can {
    "not have a title" in {
      MetaModel("", description, entityType) should be('left)
    }
    "not have a description" in {
      MetaModel(title, "", entityType) should be('left)
    }
  }
}
