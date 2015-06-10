package pt.ulisboa.tecnico.saslearning.domain;

import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

public class Scenario extends Scenario_Base {
    
	private List<AnnotationJ> annotations;
	
    public Scenario() {
        super();
    }

	public List<AnnotationJ> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationJ> annotations) {
		this.annotations = annotations;
	}
	
	public boolean containsAnnotation(String id){
		if(annotations != null){
			for(AnnotationJ ann : annotations){
				if(ann.getId().equals(id)){
					return true;
				}
			}
		}
		return false;
	}
    
    public void delete() {
    	Iterator<Annotation> i = getAnnotationSet().iterator();
    	while(i.hasNext()) {
    		Annotation a = i.next();
    		a.setScenario(null);
    	}
    	
    	if(getSrcOfStimulus() != null) {
    		getSrcOfStimulus().delete();
    	}
    	if(getArtifact() != null) {
    		getArtifact().delete();
    	}
    	
    	if(getEnvironment() != null) {
    		getEnvironment().delete();
    	}
    	
    	if (getResponse() != null) {
			getResponse().delete();
		}
    	
    	if (getResponseMeasure() != null) {
			getResponseMeasure().delete();
		}
    	if (getStimulus() != null) {
    		getStimulus().delete();
		}
    	setDocument(null);
    	deleteDomainObject();
    }
    
}
