package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import org.scalatest.{Matchers, WordSpec}

class UserSpec extends WordSpec with Matchers {

  val user = User(None, "jdoe", "jdoe@example.org", "John Doe")
  val EXAMPLE_TEAM = "Example Team"

  "A user" should {
    val otherUser = User(None, "jane", "janedoe@example.org", "Jane Doe")
    "be the team owner" when {
      "creating a new team" in {
        val team = Team(EXAMPLE_TEAM, Set(user), Set.empty)
        assert(team == user.createTeam(EXAMPLE_TEAM))
      }
      "creating a team with other owners" in {
        val team = Team(EXAMPLE_TEAM, Set(user, otherUser), Set.empty)
        assert(team == user.createTeam(EXAMPLE_TEAM, owners = Set(otherUser)))
      }
      "creating a team with other members" in {
        val team = Team(EXAMPLE_TEAM, Set(user), Set(otherUser))
        assert(team == user.createTeam(EXAMPLE_TEAM, members = Set(otherUser)))
      }
    }
  }
}
