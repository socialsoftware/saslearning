package pt.ulisboa.tecnico.socialsoftware.api

import io.finch._
import io.finch.circe.dropNullKeys._
import pt.ulisboa.tecnico.socialsoftware.domain.{Annotation, Database, User}
import shapeless.{:+:, CNil}

case class AnnotationApi() extends Api[Annotation :+: Long :+: Seq[Annotation] :+: CNil] {

  private val path = "annotation"

  private def getAnnotation: Endpoint[Annotation] = get(path :: long :: authorize) { (id: Long, user: User) =>
    Database.getAnnotation(id).map(Ok)
  }

  private def postAnnotation: Endpoint[Long] = {
    post(path :: jsonBody[User => Annotation] :: authorize) { (createAnnotation: User => Annotation, user: User) =>
      Database.addAnnotation(createAnnotation(user)).map(Ok)
    }
  }

  private def getAnnotations: Endpoint[Seq[Annotation]] = get(path :: authorize) { (user: User) =>
    Database.getAnnotations.map(Ok)
  }

  override def endpoints: Endpoint[Annotation :+: Long :+: Seq[Annotation] :+: CNil] = {
    getAnnotation :+: postAnnotation :+: getAnnotations
  }

}
