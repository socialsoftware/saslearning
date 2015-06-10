package pt.ulisboa.tecnico.saslearning.domain;

import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

public class SrcOfStimulus extends SrcOfStimulus_Base {
    
	private List<AnnotationJ> annotations;
	
    public SrcOfStimulus() {
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
    		a.setSrcOfStimulus(null);;
    	}
		setScenario(null);
		deleteDomainObject();
	}
    
}
