package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.{Annotation, Comment, Document, Thread}

import scala.collection.immutable.Seq

case class Workspace(document: Document,
                     comments: Seq[Comment] = Seq.empty,
                     annotations: Seq[Annotation] = Seq.empty) extends Thread[Workspace] {

  def post(annotation: Annotation): Workspace = this.copy(annotations = annotations :+ annotation)

  def delete(annotation: Annotation): Workspace = this.copy(annotations = annotations.filterNot(_ == annotation))

  override protected def updated(items: Seq[Comment]): Workspace = this.copy(comments = items)
}