package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import javax.mail.internet.InternetAddress

import org.scalatest.{Assertion, EitherValues, Matchers, WordSpec}

class TeamSpec extends WordSpec
  with Matchers
  with EitherValues {

  import eu.timepit.refined.auto._

  private val owner = User(None, "test", new InternetAddress("john.doe@example.org"), "John Doe")
  private val member = User(None, "jane", new InternetAddress("jane_doe@example.org"), "Jane Doe")
  private val newMember = User(None, "jsmith", new InternetAddress("jsmith@example.org"), "JohnSmith")

  private val exampleTeam = Team("Apple", Set(owner), Set(member))

  private def compareTeams(expected: Team, actual: Team): Assertion =
    assert(expected == actual && expected.size == actual.size)

  "A team" should {
    "have a non empty name" in {
      val team = Team.fromUnsafe("", Set(owner), Set.empty)
      team should be ('left)
    }
    "have at least one owner" in {
      assertThrows[IllegalArgumentException](Team("Potato", Set.empty, Set.empty))
    }
    "update its owners and members" when {
      val team = Team("Apple", Set(owner, member), Set.empty)
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
    "be the sum of its members and owners" when {
      "there at least one owner and no members" in {
        val team = Team("Banana", Set(owner), Set.empty)
        team.size should equal (1)
      }
      "there is at least one owner and one member" in {
        val team = Team("Banana", Set(owner), Set(member))
        team.size should equal (2)
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
