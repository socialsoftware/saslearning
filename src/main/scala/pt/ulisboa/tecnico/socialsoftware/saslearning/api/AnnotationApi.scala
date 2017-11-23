package pt.ulisboa.tecnico.socialsoftware.saslearning.api

import io.finch._
import io.finch.circe.dropNullValues._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{Annotation, Database, User}
import shapeless.{:+:, CNil}

case class AnnotationApi() extends Api[Annotation :+: Long :+: Seq[Annotation] :+: CNil] {

  private val prefix = "annotation"

  private def getAnnotation: Endpoint[Annotation] = get(prefix :: path[Long] :: authorize) { (id: Long, user: User) =>
    Database.getAnnotation(id).map(Ok)
  }

  private def postAnnotation: Endpoint[Long] = {
    post(prefix :: jsonBody[User => Annotation] :: authorize) { (createAnnotation: User => Annotation, user: User) =>
      Database.addAnnotation(createAnnotation(user)).map(Ok)
    }
  }

  private def getAnnotations: Endpoint[Seq[Annotation]] = get(prefix :: authorize) { (user: User) =>
    Database.getAnnotations.map(Ok)
  }

  override def endpoints: Endpoint[Annotation :+: Long :+: Seq[Annotation] :+: CNil] = {
    getAnnotation :+: postAnnotation :+: getAnnotations
  }

}
