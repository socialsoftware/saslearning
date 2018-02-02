package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import eu.timepit.refined.auto._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.Annotation

import scala.collection.immutable.Seq

class WorkspaceSpec extends UnitSpec {

  private val prestineWorkspace = Workspace(defaultDocument)
  private val workspaceWithDefaultAnnotation = Workspace(defaultDocument, Seq.empty, Seq(defaultAnnotation))
  private val anotherAnnotation = Annotation(None, 0l, 1l, "This is an annotation", user)

  "Posting an annotation" should {
    "add it to the annotation list" when {
      "there are no annotations" in {
        val actual = prestineWorkspace.post(defaultAnnotation)

        assert(workspaceWithDefaultAnnotation == actual)
      }
      "there is at least one annotation" in {

        val expected = Workspace(defaultDocument, Seq.empty, Seq(defaultAnnotation, anotherAnnotation))
        val actual = Workspace(defaultDocument, Seq.empty, Seq(defaultAnnotation)).post(anotherAnnotation)

        assert(expected == actual)
      }
    }
  }

  "Deleting an annotation" should {
    "remove it from the annotation list" in {
      val actual = workspaceWithDefaultAnnotation.delete(defaultAnnotation)
      assert(prestineWorkspace == actual)
    }
    "not change the workspace" when {
      "the list is empty" in {
        val actual = prestineWorkspace.delete(defaultAnnotation)
        assert(prestineWorkspace == actual)
      }
      "the annotation isn't present" in {
        val actual = Workspace(defaultDocument, Seq.empty, Seq(defaultAnnotation)).delete(anotherAnnotation)
        assert(prestineWorkspace == actual)
      }
    }
  }
}
