package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.fromString
import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model.Node.MetaModelNode

case class MetaModel(title: NonEmptyString, root: MetaModelNode) {
  def instantiate: Model = Model(this, root.instantiate)
}

object MetaModel {
  def apply(title: String, root: MetaModelNode): Either[String, MetaModel] =
    fromString(title)(MetaModel(_, root))
}
