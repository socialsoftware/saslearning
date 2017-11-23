package pt.ulisboa.tecnico.socialsoftware.saslearning.api

import io.finch._
import io.finch.circe.dropNullValues._
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.{Database, Document, User}
import shapeless.{:+:, CNil}

case class DocumentApi() extends Api[Document :+: Long :+: Seq[Document] :+: CNil] {

  private val prefix = "document"

  private def getDocument: Endpoint[Document] = get(prefix :: path[Long] :: authorize) { (id: Long, user: User) =>
    Database.getDocument(id).map(Ok)
  }

  private def postDocument: Endpoint[Long] =
    post(prefix :: jsonBody[User => Document] :: authorize ) { (createDocument: (User => Document), user: User) =>
      Database.addDocument(createDocument(user)).map(Ok)
    }

  private def getDocuments: Endpoint[Seq[Document]] = get(prefix :: authorize) { user: User =>
    Database.getDocuments.map(Ok)
  }

  override def endpoints: Endpoint[Document :+: Long :+: Seq[Document] :+: CNil] = {
    getDocument :+: postDocument :+: getDocuments
  }

}
