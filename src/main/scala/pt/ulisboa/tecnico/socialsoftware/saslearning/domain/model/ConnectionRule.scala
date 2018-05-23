package pt.ulisboa.tecnico.socialsoftware.saslearning.domain.model

import eu.timepit.refined.types.string.NonEmptyString

case class ConnectionRule(name: NonEmptyString, multiplicity: Multiplicity)

object ConnectionRule {
  def apply(name: String, multiplicity: Multiplicity): Either[String, ConnectionRule] =
    for (nonEmptyName <- NonEmptyString.from(name)) yield ConnectionRule(nonEmptyName, multiplicity)
}
