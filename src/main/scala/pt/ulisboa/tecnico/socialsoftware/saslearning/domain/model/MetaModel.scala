package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.fromString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model.Node.MetaModelNode

case class MetaModel(title: NonEmptyString, root: MetaModelNode) {
  def toModel: Model = Model(this, root.toModelNode)
}

object MetaModel {
  def apply(title: String, root: MetaModelNode): Either[String, MetaModel] =
    fromString(title)(MetaModel(_, root))
}
