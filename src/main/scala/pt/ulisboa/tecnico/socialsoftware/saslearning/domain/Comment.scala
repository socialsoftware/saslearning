package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection._

case class Comment(content: String Refined NonEmpty, author: User)

object Comment {

  def apply(content: String, author: User): Either[String, Comment] =
    refineV[NonEmpty](content).map(txt => Comment(txt, author))

}