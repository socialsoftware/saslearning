package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class EntityTypeSpec extends UnitSpec {

  val name = "Scenario"
  val hint = "Scenarios can be concrete or general"

  "An entity type" must {
    "have a name and a hint" in {
      val entityType = EntityType(name, hint)

      entityType should be('right)
      entityType.right.value.name.value shouldEqual name
      entityType.right.value.hint.value shouldEqual hint
    }
  }

  it can {
    "not have a name" in {
      EntityType("", hint) should be('left)
    }
    "not have a hint" in {
      EntityType(name, "") should be('left)
    }
  }

}
