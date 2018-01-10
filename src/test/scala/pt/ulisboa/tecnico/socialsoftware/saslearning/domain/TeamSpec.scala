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

  private val exampleTeam = Team.fromUnsafe("Apple", Set(owner), Set(member))

  private def compareTeams(expected: Either[String, Team], actual: Either[String, Team]): Assertion = {
    actual should be ('right)
    actual should equal (expected)
  }

  "A team" should {
    "have a non empty name" in {
      val team = Team.fromUnsafe("", Set(owner), Set.empty)
      team should be ('left)
    }
    "have at least one owner" in {
      val team = Team.fromUnsafe("Potato", Set.empty, Set.empty)
      team should be ('left)
    }
    "update its owners and members" when {
      val team = Team.fromUnsafe("Apple", Set(owner, member), Set.empty)
      "promoting a member to owner" in {
        compareTeams(expected = team, actual = exampleTeam.flatMap(_.promote(member)))
      }
      "demoting a owner to member" in {
        compareTeams(expected = exampleTeam, actual = team.flatMap(_.demote(member)))
      }
      "adding a owner that is a member" in {
        compareTeams(expected = team, exampleTeam.flatMap(_.addOwner(member)))
      }
    }
    "update its owners" when {
      val team = Team.fromUnsafe("Apple", Set(owner, newMember), Set(member))
      "adding a new owner" in {
        compareTeams(expected = team, actual = exampleTeam.flatMap(_.addOwner(newMember)))
      }
      "removing an existing owner" in {
        compareTeams(expected = exampleTeam, actual = team.flatMap(_.removeOwner(newMember)))
      }
    }
    "update its members" when {
      "adding a new member" in {
        val expectedTeam = Team.fromUnsafe("Apple", Set(owner), Set(member, newMember))
        compareTeams(expected = expectedTeam, actual = exampleTeam.map(_.addMember(newMember)))
      }
      "removing an existing member" in {
        val expectedTeam = Team.fromUnsafe("Apple", Set(owner))
        compareTeams(expected = expectedTeam, actual = exampleTeam.map(_.removeMember(member)))
      }
    }
    "not be updated" when {
      "adding a member that is a owner" in {
        compareTeams(expected = exampleTeam, actual = exampleTeam.map(_.addMember(owner)))
      }
    }
  }

  "The team size" should {
    "be the sum of its members and owners" when {
      "there at least one owner and no members" in {
        val team = Team.fromUnsafe("Banana", Set(owner), Set.empty)
        team should be ('right)
        team.right.value.size should equal (1)
      }
      "there is at least one owner and one member" in {
        val team = Team.fromUnsafe("Banana", Set(owner), Set(member))
        team should be ('right)
        team.right.value.size should equal (2)
      }
    }
  }

  "Checking if a user belongs to a team" should {
    "be true if the user is a owner" in {
      exampleTeam.right.value.contains(owner) should be (true)
    }
    "be true if the user is a member" in {
      exampleTeam.right.value.contains(member) should be (true)
    }
    "be false if the user is not a owner or a member" in {
      exampleTeam.right.value.contains(newMember) should be (false)
    }
  }

}
