package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class EntityTypeSpec extends UnitSpec {

  val name = "Scenario"

  "An entity type" must {
    "have a name" in {
      val entityType = EntityType(name)

      entityType should be('right)
      entityType.right.value.name.value shouldEqual name
    }
  }

  it can {
    "not have a name" in {
      EntityType("") should be('left)
    }
  }

}
