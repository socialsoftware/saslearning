package pt.ulisboa.tecnico.socialsoftware.api

import io.finch._
import io.finch.circe.dropNullKeys._
import pt.ulisboa.tecnico.socialsoftware.domain.{Database, Document, User}
import shapeless.{:+:, CNil}

case class DocumentApi() extends Api[Document :+: Long :+: Seq[Document] :+: CNil] {

  private val path = "document"

  private def getDocument: Endpoint[Document] = get(path :: long :: authorize) { (id: Long, user: User) =>
    Database.getDocument(id).map(Ok)
  }

  private def postDocument: Endpoint[Long] =
    post(path :: jsonBody[User => Document] :: authorize ) { (createDocument: (User => Document), user: User) =>
      Database.addDocument(createDocument(user)).map(Ok)
    }

  private def getDocuments: Endpoint[Seq[Document]] = get(path :: authorize) { user: User =>
    Database.getDocuments.map(Ok)
  }

  override def endpoints: Endpoint[Document :+: Long :+: Seq[Document] :+: CNil] = {
    getDocument :+: postDocument :+: getDocuments
  }

}
