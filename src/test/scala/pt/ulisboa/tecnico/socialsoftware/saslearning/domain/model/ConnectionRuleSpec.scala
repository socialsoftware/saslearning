package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.numeric.NonNegInt
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class ConnectionRuleSpec extends UnitSpec {

  val multiplicity = new Multiplicity(NonNegInt.unsafeFrom(1), Some(NonNegInt.unsafeFrom(1)))

  "A connection rule" must {
    "have a name" in {
      val name = "Has Tactic"

      val connectionRule = ConnectionRule(name, multiplicity)

      connectionRule should be('right)
      connectionRule.right.get.name.value shouldEqual "Has Tactic"
      connectionRule.right.get.multiplicity shouldBe multiplicity
    }
  }

  it can {
    "not have a name" in {
      ConnectionRule("", multiplicity) should be('left)
    }
  }
}
