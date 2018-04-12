package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import java.net.URI
import javax.mail.internet.InternetAddress

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import org.scalatest.Assertion
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.Document

import scala.collection.immutable.Seq

class TeamSpec extends UnitSpec {

  private val member = User(None, "jane", new InternetAddress("jane_doe@example.org"), "Jane Doe")
  private val newMember =
    User(None, "jsmith", new InternetAddress("jsmith@example.org"), "JohnSmith")

  private val exampleTeam = Team("Apple", Seq.empty, Refined.unsafeApply(Set(user)), Set(member))

  def assertRight(expected: Team, actual: Either[String, Team]): Assertion = {
    super.assertRight(expected, actual)
    actual.right.value.size should equal(expected.size)
  }

  private def compareTeams(expected: Team, actual: Team): Assertion =
    assertRight(expected, Right(actual))

  "A team" should {
    "have a non empty name" in {
      val team = Team.fromUnsafe("", Seq.empty, Set(user), Set.empty)
      team should be('left)
    }
    "have at least one owner" in {
      val team = Team.fromUnsafe("Potato", Seq.empty, Set.empty, Set.empty)
      team should be('left)
    }
    "update its owners and members" when {
      val expectedTeam = Team("Apple", Seq.empty, Refined.unsafeApply(Set(user, member)), Set.empty)
      "promoting a member to owner" in {
        assertRight(expected = expectedTeam, actual = exampleTeam.promote(member))
      }
      "demoting a owner to member" in {
        assertRight(expected = exampleTeam, actual = expectedTeam.demote(member))
      }
      "adding a owner that is a member" in {
        assertRight(expected = expectedTeam, exampleTeam.addOwner(member))
      }
    }
    "update its owners" when {
      val expectedTeam =
        Team("Apple", Seq.empty, Refined.unsafeApply(Set(user, newMember)), Set(member))
      "adding a new owner" in {
        assertRight(expected = expectedTeam, actual = exampleTeam.addOwner(newMember))
      }
      "removing an existing owner" in {
        assertRight(expected = exampleTeam, actual = expectedTeam.removeOwner(newMember))
      }
    }
    "update its members" when {
      "adding a new member" in {
        val expectedTeam =
          Team("Apple", Seq.empty, Refined.unsafeApply(Set(user)), Set(member, newMember))
        compareTeams(expected = expectedTeam, actual = exampleTeam.addMember(newMember))
      }
      "removing an existing member" in {
        val expectedTeam = Team("Apple", Seq.empty, Refined.unsafeApply(Set(user)))
        compareTeams(expected = expectedTeam, actual = exampleTeam.removeMember(member))
      }
    }
    "not be updated" when {
      "adding a member that is a owner" in {
        compareTeams(expected = exampleTeam, actual = exampleTeam.addMember(user))
      }
    }
  }

  "The team size" should {
    "be the sum of its members and owners" when {
      "there at least one owner and no members" in {
        val team = Team.fromUnsafe("Banana", Seq.empty, Set(user), Set.empty)
        team should be('right)
        team.right.value.size should equal(1)
      }
      "there is at least one owner and one member" in {
        val team = Team.fromUnsafe("Banana", Seq.empty, Set(user), Set(member))
        team should be('right)
        team.right.value.size should equal(2)
      }
    }
  }

  "Checking if a user belongs to a team" should {
    "be true if the user is a owner" in {
      exampleTeam.contains(user) should be(true)
    }
    "be true if the user is a member" in {
      exampleTeam.contains(member) should be(true)
    }
    "be false if the user is not a owner or a member" in {
      exampleTeam.contains(newMember) should be(false)
    }
  }

  "Adding a document" should {
    "create a new workspace" in {
      val expected =
        Team("Apple", Seq(Workspace(defaultDocument)), Refined.unsafeApply(Set(user)), Set(member))
      val actual = exampleTeam.addDocument(defaultDocument)
      assert(expected == actual)
    }
  }

  "Removing a document" should {
    "remove the matching workspace from the list" in {
      val actual =
        Team("Apple", Seq(Workspace(defaultDocument)), Refined.unsafeApply(Set(user)), Set(member))
          .removeDocument(defaultDocument)

      assert(exampleTeam == actual)
    }
    "not change the workspace list" in {
      val expected =
        Team("Apple", Seq(Workspace(defaultDocument)), Refined.unsafeApply(Set(user)), Set(member))

      val anotherDocument = Document(None,
                                     Refined.unsafeApply("Art of War"),
                                     URI.create("www.exameple.org"),
                                     Refined.unsafeApply("Some content"),
                                     user)

      val actual = expected.removeDocument(anotherDocument)

      assert(expected == actual)
    }
  }

}
