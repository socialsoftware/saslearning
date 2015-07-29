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
	
	public boolean canBeAddedToScenario(Scenario s) {
//		String tag = getTag();
//		if(isScenarioAnnotation()) {
//			String[] split = getTag().split(" ");
//			if(getTag().contains("Tactic")) {
//				if(split[2].equals(s.getQualityAttribute().getName())) {
//					return true;
//				}
//				return false;
//				
//			}
//			else if(Utils.qualityAttributes().contains(tag)) {
//				if(tag.equals(s.getQualityAttribute().getName())) {
//					return true;
//				}
//				return false;
//			}else {
//				return true;				
//			}
//		}
		return false;
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
	
	public boolean belongsToScenario(Scenario s) {
//		if(getScenario() != null) {
//			return s.getExternalId().equals(getScenario().getExternalId());
//		}else if(getScenarioElement() != null) {
//			ScenarioElement elem = getScenarioElement();
//			for(ScenarioElement e : s.getElements().values()) {
//				if(e.getExternalId().equals(elem.getExternalId())) {
//					return true;
//				}
//			}
//			if(elem instanceof QualityAttribute) {
//				return elem.getExternalId().equals(s.getQualityAttribute().getExternalId());
//			}else if(elem instanceof Tactic) {
//				for(Tactic t : s.getQualityAttribute().getTacticSet()) {
//					if(t.getExternalId().equals(elem.getExternalId())) {
//						return true;
//					}
//				}
//			}
//			return false;
//		}else {
//			return false;
//		}
		return false;
	}
}
