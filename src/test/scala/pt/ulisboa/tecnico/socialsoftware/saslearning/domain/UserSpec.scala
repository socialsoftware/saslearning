package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import javax.mail.internet.InternetAddress

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{EitherValues, Matchers, WordSpec}

class UserSpec extends WordSpec
  with Matchers
  with EitherValues {

  private val user = User(None, "jdoe", new InternetAddress("jdoe@example.org"), "John Doe")
  private val exampleTeam = "Example Team"

  private def assertCreateUserFromJson(expected: Option[User], actual: String) = {
    assert(User.fromJson(parse(actual).getOrElse(Json.Null)) == expected)
  }

  private def assertCreateTeam(expected: Team, actual: Either[String, Team]) = {
    actual should be ('right)
    actual.right.value should equal (expected)
    actual.right.value.size should equal (expected.size)
  }

  "Creating a user" should {
    "fail" when {
      "have an empty name" in {
        val user = User.fromUnsafe(None, "", "jdoe@example.org", "John Doe")
        user should be ('left)
      }
      "have an invalid e-mail" in {
        val user = User.fromUnsafe(None, "jdoe", "example.org", "John Doe")
        user should be ('left)
      }
      "have an empty display name" in {
        val user = User.fromUnsafe(None, "jdoe", "jdoe@example.org", "")
        user should be ('left)
      }
    }
    "succeed" in {
      val actual = User.fromUnsafe(None, "jdoe", "jdoe@example.org", "John Doe")

      actual should be ('right)
      actual.right.value should be (user)
    }
  }

  "A user" should {
    val otherUser = User(None, "jane", new InternetAddress("janedoe@example.org"), "Jane Doe")
    "be the team owner" when {
      "creating a new team" in {
        val team = Team(Refined.unsafeApply(exampleTeam), Refined.unsafeApply(Set(user)), Set.empty)
        assertCreateTeam(team, user.createTeam(exampleTeam))
      }
      "creating a team with other owners" in {
        val team = Team(Refined.unsafeApply(exampleTeam), Refined.unsafeApply(Set(user, otherUser)), Set.empty)
        assertCreateTeam(team, user.createTeam(exampleTeam, owners = Set(otherUser)))
      }
      "creating a team with other members" in {
        val team = Team(Refined.unsafeApply(exampleTeam), Refined.unsafeApply(Set(user)), Set(otherUser))
        assertCreateTeam(team, user.createTeam(exampleTeam, members = Set(otherUser)))
      }
    }
  }

  "Exporting a user to JSON" should {
    "should contain all its defined fields" in {
      val json =
        """{
          |  "id" : 0,
          |  "username" : "jdoe",
          |  "email" : "jdoe@example.org",
          |  "display_name" : "John Doe"
          |}""".stripMargin
      assert(user.copy(id = Some(0)).asJson.spaces2 == json)
    }
  }

  "Creating a user from JSON" should {
    "create a valid user" when {
      "the fields are what we expect" in {
        val rawJson =
          """{
            |  "username" : "jdoe",
            |  "email" : "jdoe@example.org",
            |  "display_name" : "John Doe"
            |}""".stripMargin
        assertCreateUserFromJson(expected = Some(user), actual = rawJson)
      }
      "extra fields are provided" in {
        val rawJson =
          """{
            |  "username" : "jdoe",
            |  "email" : "jdoe@example.org",
            |  "display_name" : "John Doe",
            |  "age" : 20
            |}""".stripMargin
        assertCreateUserFromJson(expected = Some(user), actual = rawJson)
      }
      "id field is provided, ignoring it" in {
        val rawJson =
          """{
            |  "id" : 0,
            |  "username" : "jdoe",
            |  "email" : "jdoe@example.org",
            |  "display_name" : "John Doe"
            |}""".stripMargin
        assertCreateUserFromJson(expected = Some(user), actual = rawJson)
      }
    }
    "return None" when {
      "username field is missing" in {
        val rawJson =
          """{
            |  "email" : "jdoe@example.org",
            |  "display_name" : "John Doe"
            |}""".stripMargin
        assertCreateUserFromJson(expected = None, actual = rawJson)
      }
      "email field is missing" in {
        val rawJson =
          """{
            |  "username" : "jdoe",
            |  "display_name" : "John Doe"
            |}""".stripMargin
        assertCreateUserFromJson(expected = None, actual = rawJson)
      }
      "displayName field is missing" in {
        val rawJson =
          """{
            |  "username" : "jdoe",
            |  "email" : "jdoe@example.org",
            |}""".stripMargin
        assertCreateUserFromJson(expected = None, actual = rawJson)
      }
    }
  }

}
