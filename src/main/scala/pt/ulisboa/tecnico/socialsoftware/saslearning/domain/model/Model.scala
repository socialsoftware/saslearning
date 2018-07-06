package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model.Node.ModelNode

case class Model(metaModel: MetaModel, root: ModelNode) {
  def evaluate: Either[List[ModelValidation], Unit] = root.evaluate
}
