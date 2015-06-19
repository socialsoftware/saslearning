package pt.ulisboa.tecnico.saslearning.domain;

public class Annotation extends Annotation_Base {

	public Annotation() {
		super();
	}

	public boolean hasScenarioElement() {
		return getSrcOfStimulus() != null || getStimulus() != null
				|| getArtifact() != null || getEnvironment() != null
				|| getResponse() != null || getResponseMeasure() != null;
	}

	@Override
	public String toString() {
		return getAnnotation();
	}

	public void delete() {
		setDocument(null);
		setOwner(null);
		getFragment().delete();
		deleteDomainObject();
		
	}
}
