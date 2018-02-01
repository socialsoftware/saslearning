package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import java.net.URI

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class DocumentSpec extends UnitSpec {

  private val defaultUri = URI.create("http://example.org")
  private val defaultTitle: NonEmptyString = Refined.unsafeApply("The history of Art")
  private val defaultContent: NonEmptyString = Refined.unsafeApply("Content goes here")

  private val defaultDocument = Document(None, defaultTitle, defaultUri, defaultContent, user)

  "A document" should {
    "have a title" when {
      "is empty" in {
        val document = Document.fromUnsafe(None, "", defaultUri, defaultContent, user)
        document should be ('left)
      }
      "is not empty" in {
        val document = Document.fromUnsafe(None, "The history of Art", defaultUri, defaultContent, user)
        assertRight(expected = defaultDocument, actual = document)
      }
    }
    "have content" when {
      "is empty" in {
        val document = Document.fromUnsafe(None, defaultTitle, defaultUri, "", user)
        document should be ('left)
      }
      "is not empty" in {
        val document = Document.fromUnsafe(None, defaultTitle, defaultUri, "Content goes here", user)
        assertRight(expected = defaultDocument, actual = document)
      }
    }
  }

}