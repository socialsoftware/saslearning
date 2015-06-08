package pt.ulisboa.tecnico.saslearning.domain;

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
    
    
    
}
