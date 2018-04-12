package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration

import eu.timepit.refined.auto._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.UnitSpec

class DocumentSpec extends UnitSpec {

  val source = "http://example.org"

  "A document" should {
    "not be created" when {
      "the title is empty" in {
        val document = Document.fromUnsafe(None, "", source, defaultContent, user)
        document should be('left)
      }
      "have no content" in {
        val document = Document.fromUnsafe(None, defaultTitle, source, "", user)
        document should be('left)
      }
      "have an invalid URI" in {
        val document =
          Document.fromUnsafe(None, defaultTitle, "invalid source", defaultContent, user)
        document should be('left)
      }
    }
    "be created" in {
      val document = Document.fromUnsafe(None, defaultTitle, source, defaultContent, user)
      assertRight(expected = defaultDocument, actual = document)
    }
  }
}
