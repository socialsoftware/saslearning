package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.{Assertion, Matchers, WordSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class TeamSpec extends WordSpec with GeneratorDrivenPropertyChecks with Matchers {

  private val owner = User(None, "test", "jdoe@example.org", "John Doe")
  private val member = User(None, "jane", "jane_doe@example.org", "Jane Doe")
  private val newMember = User(None, "jsmith", "jsmith@example.org", "JohnSmith")


  private val EXAMPLE_TEAM = "Example Team"
  private val exampleTeam = Team(EXAMPLE_TEAM, Set(owner), Set(member))

  private def compareTeams(expected: Team, actual: Team): Assertion =
    assert(expected == actual && expected.size == actual.size)

  implicit def userGen: Gen[User] = for {
    username <- arbitrary[String]
    email <- arbitrary[String]
    displayName <- arbitrary[String]
  } yield User(None, username, email, displayName)

  implicit def membersGen(implicit gen: Gen[User]): Gen[(List[User], List[User])] = for {
    owners <- Gen.nonEmptyListOf[User](gen)
    members <- Gen.nonEmptyListOf[User](gen)
  } yield (owners, members)

  "A team" should {
    "have a non empty name" in {
      assertThrows[IllegalArgumentException](Team("", Set(owner), Set.empty))
    }
    "have at least one owner" in {
      assertThrows[IllegalArgumentException](Team(EXAMPLE_TEAM, Set.empty, Set.empty))
    }
    "update its owners and members" when {
      val team = Team(EXAMPLE_TEAM, Set(owner, member), Set.empty)
      "promoting a member to owner" in {
        compareTeams(expected = team, actual = exampleTeam.promote(member))
      }
      "demoting a owner to member" in {
        compareTeams(expected = exampleTeam, actual = team.demote(member))
      }
      "adding a owner that is a member" in {
        compareTeams(expected = team, exampleTeam.addOwner(member))
      }
    }
    "update its owners" when {
      val updatedTeam = exampleTeam.copy(owners = exampleTeam.owners + newMember)
      "adding a new owner" in {
        compareTeams(expected = updatedTeam, actual = exampleTeam.addOwner(newMember))
      }
      "removing an existing owner" in {
        compareTeams(expected = exampleTeam, actual = updatedTeam.removeOwner(newMember))
      }
    }
    "update its members" when {
      "adding a new member" in {
        val expectedTeam = exampleTeam.copy(members = exampleTeam.members + newMember)
        compareTeams(expected = expectedTeam, actual = exampleTeam.addMember(newMember))
      }
      "removing an existing member" in {
        val expectedTeam = exampleTeam.copy(members = Set.empty)
        compareTeams(expected = expectedTeam, actual = exampleTeam.removeMember(member))
      }
    }
    "not be updated" when {
      "adding a member that is a owner" in {
        compareTeams(expected = exampleTeam, actual = exampleTeam.addMember(owner))
      }
    }
  }

  "The team size" should {
    "be the sum of its members and owners" in {
      forAll(membersGen) { case (owners, members) =>
        val team = Team(EXAMPLE_TEAM, owners.toSet, members.toSet)
        assert(team.size == owners.size + members.size)
      }
    }
  }

  "Checking if a user belongs to a team" should {
    "be true if the user is a owner" in {
      assert(exampleTeam.contains(owner))
    }
    "be true if the user is a member" in {
      assert(exampleTeam.contains(member))
    }
    "be false if the user is not a owner or a member" in {
      assert(!exampleTeam.contains(newMember))
    }
  }

}
