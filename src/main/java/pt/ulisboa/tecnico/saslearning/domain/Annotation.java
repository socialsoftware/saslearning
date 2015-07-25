package pt.ulisboa.tecnico.saslearning.domain;

import pt.ulisboa.tecnico.saslearning.utils.Utils;

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
		deleteDomainObject();
		
	}
	
	public boolean isConnected() {
		return getScenario() != null || getScenarioElement() != null;
	}
	
	public boolean isScenarioAnnotation() {
		return Utils.allScenarioConcepts().contains(getTag());
	}
	
	public boolean canBeAddedToScenario(Scenario s) {
		String tag = getTag();
		System.out.println("can " + tag + " be added to scenario?");
		if(isScenarioAnnotation()) {
			System.out.println(tag  + " is a scenario annotation");
			String[] split = getTag().split(" ");
			if(getTag().contains("Tactic")) {
				System.out.println(tag + "is a tactic");
				if(split[2].equals(s.getQualityAttribute().getName())) {
					System.out.println("it's THE tactic");
					return true;
				}
				System.out.println("its not the tactic.");
				return false;
				
			}
			else if(Utils.qualityAttributes().contains(tag)) {
				if(tag.equals(s.getQualityAttribute().getName())) {
					System.out.println("is the quality attribute");
					return true;
				}
				System.out.println("its nt the quality attribute");
				return false;
			}else {
				System.out.println("just a simple scenario annotation");
				return true;				
			}
		}
		System.out.println(tag + " does not belong to scenarios");
		return false;
	}
	
	public boolean belongsToScenario(Scenario s) {
		if(getScenario() != null) {
			return s.getExternalId().equals(getScenario().getExternalId());
		}else if(getScenarioElement() != null) {
			ScenarioElement elem = getScenarioElement();
			return elem.getExternalId().equals(s.getExternalId());
		}else {
			return false;
		}
	}
}
