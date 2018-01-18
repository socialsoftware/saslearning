package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.{Annotation, Document}

import scala.concurrent.Future

case class Storage(userCount: Long = -1, users: Map[Long, User] = Map.empty,
                   documentCount: Long = -1, documents: Map[Long, Document] = Map.empty,
                   annotationCount: Long = -1, annotations: Map[Long, Annotation] = Map.empty) {
}

trait StorageOperations {
  def addUser(user: User): Future[User]
  def getUser(id: Long): Future[User]
  def getUsers: Future[Seq[User]]

  def addDocument(document: Document): Future[Document]
  def getDocument(id: Long): Future[Document]
  def getDocuments: Future[Seq[Document]]

  def addAnnotation(annotation: Annotation): Future[Annotation]
  def getAnnotation(id: Long): Future[Annotation]
  def getAnnotations: Future[Seq[Annotation]]
}
