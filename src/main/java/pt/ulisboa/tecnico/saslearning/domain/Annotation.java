package pt.ulisboa.tecnico.saslearning.domain;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

import com.google.gson.Gson;

public class Annotation extends Annotation_Base {

	public Annotation() {
		super();
	}

	@Override
	public String toString() {
		return getAnnotation();
	}

	public void delete() {
		setDocument(null);
		setOwner(null);
		setScenario(null);
		setScenarioElement(null);
		deleteDomainObject();
		
	}
	
	public AnnotationJ getJsonVersion() {
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(getAnnotation(), AnnotationJ.class);
		return ann;
	}
	
	public boolean isConnected() {
		return getScenario() != null || getScenarioElement() != null;
	}
	
	public boolean isScenarioAnnotation() {
		return Utils.allScenarioConcepts().contains(getTag());
	}
	
	public Scenario getEnclosingScenario() {
		if(isScenarioAnnotation()) {
			if(getScenario() != null) {
				return getScenario();
			}
			return getScenarioElement().getEnclosingScenario();
		}
		return null;
	}
	
	public void updateConnection(String connectedId) {
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(getAnnotation(), AnnotationJ.class);
		ann.setConnectedId(connectedId);
		setAnnotation(g.toJson(ann));
	}
}
