package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class MultiplicitySpec extends UnitSpec {

  "Lower bound" must {
    "be >= 0" in {
      Multiplicity(0) should be('right)
    }
    "not be < 0" in {
      Multiplicity(-1) should be('left)
    }
    "not be > upper bound" in {
      val lower = 0
      Multiplicity(lower + 1, lower) should be('left)
    }
  }

  "Upper bound" must {
    "be >= 0" in {
      Multiplicity(0, 0) should be('right)
    }
    "not be < 0" in {
      Multiplicity(-1, -1) should be('left)
    }
    "not be < lower bound" in {
      val lower = 1
      Multiplicity(lower, lower - 1) should be('left)
    }
  }

  it can {
    "be infinite" in {
      val multiplicity = Multiplicity(0)

      multiplicity should be('right)
      multiplicity.right.value.isInfinite shouldBe true
    }
    "be finite" in {
      val multiplicity = Multiplicity(0, 10)

      multiplicity should be('right)
      multiplicity.right.value.isInfinite shouldBe false
    }
  }
}
