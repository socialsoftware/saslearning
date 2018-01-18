package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import java.util.NoSuchElementException

import com.twitter.util.Future
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.collaboration.{Annotation, Document}
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.oauth.Token

import scala.collection.mutable

object Database {
  private val users: mutable.Map[Long, User] = mutable.Map.empty[Long, User]
  private val documents: mutable.Map[Long, Document] = mutable.Map.empty[Long, Document]
  private val annotations: mutable.Map[Long, Annotation] = mutable.Map.empty[Long, Annotation]

  val accessTokenToUserMap: mutable.Map[String, User] = mutable.Map.empty[String, User]
  val accessTokenToOAuthInfoMap: mutable.Map[String, Token] = mutable.Map.empty[String, Token]
  val usernameToJwtTokenMap: mutable.Map[String, String] = mutable.Map.empty[String, String]

  def getUser(username: String): Future[User] = users.synchronized {
    users.values.find(_.username.value == username) match {
      case Some(user) => Future.value(user)
      case None => Future.exception(new NoSuchElementException)
    }
  }

  def addUser(newUser: User): Future[User] = users.synchronized {
    if (users.values.exists(_.username == newUser.username)) {
      Future.exception(new IllegalArgumentException)
    } else {
      newUser.id match {
        case None =>
          val id = generateNewId(users)
          users(id) = newUser.copy(id = Some(id))
          Future(newUser)
        case _ => Future.exception(new IllegalArgumentException)
      }
    }
  }

  def getUsers: Future[Seq[User]] = Future.value(
    users.synchronized(users.toSeq.sortBy(_._1).map(_._2))
  )

  def getDocument(id: Long): Future[Document] = Future(
    documents.synchronized {
      documents.getOrElse(id, throw new NoSuchElementException)
    }
  )

  def addDocument(newDocument: Document): Future[Long] = documents.synchronized {
    newDocument.id match {
      case None =>
        val id = generateNewId(documents)
        documents(id) = newDocument.copy(id = Some(id))
        Future(id)
      case _ => Future.exception(new IllegalArgumentException)
    }
  }

  def getDocuments: Future[Seq[Document]] = Future(
    documents.synchronized(documents.toSeq.sortBy(_._1).map(_._2))
  )

  def getAnnotation(id: Long): Future[Annotation] = Future(
    annotations.synchronized {
      annotations.getOrElse(id, throw new NoSuchElementException)
    }
  )

  def addAnnotation(newAnnotation: Annotation): Future[Long] = annotations.synchronized {
    newAnnotation.id match {
      case None =>
        val id = generateNewId(annotations)
        annotations(id) = newAnnotation.copy(id = Some(id))
        Future(id)
      case _ => Future.exception(new IllegalArgumentException)
    }
  }

  def getAnnotations: Future[Seq[Annotation]] = Future(
    annotations.synchronized(annotations.toSeq.sortBy(_._1).map(_._2))
  )

  private def generateNewId(target: mutable.Map[Long, _]): Long = if (target.isEmpty) 0 else target.keys.max + 1

}
