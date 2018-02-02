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

object Workspace {
  import io.circe.{Decoder, Encoder}
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

  implicit val decodeJson: Decoder[Workspace] = deriveDecoder
  implicit val encodeJson: Encoder[Workspace] = deriveEncoder
}

trait Workable[T] {

  def workspaces: Seq[Workspace]

  protected def updated(workspaces: Seq[Workspace]): T

  def addDocument(document: Document): T = updated(workspaces :+ Workspace(document))

  def removeDocument(document: Document): T = updated(workspaces.filterNot(_.document == document))
}

