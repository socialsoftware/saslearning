package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class EntityTypeSpec extends UnitSpec {

  val name = "Scenario"

  "An entity type" must {
    "have a name" in {
      val entityType = EntityType(name)

      entityType should be('right)
      entityType.right.value.name.value shouldEqual name
      entityType.right.value.rules shouldBe empty
    }
  }

  it can {
    "not have a name" in {
      EntityType("") should be('left)
    }
    "have connections rules" in {
      val connectionRule =
        ConnectionRule(
          NonEmptyString.unsafeFrom("Has Tactic"),
          new Multiplicity(NonNegInt.unsafeFrom(1), Some(NonNegInt.unsafeFrom(1)))
        )

      val tactic = EntityType(NonEmptyString.unsafeFrom("Tactic"))

      val entityType =
        EntityType(name, Map(connectionRule -> tactic))

      entityType should be('right)
      entityType.right.value.name.value shouldEqual name
      entityType.right.value.rules.get(connectionRule) shouldBe Some(tactic)
    }
  }

}
