package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.auto._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class DocumentSpec extends UnitSpec {

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