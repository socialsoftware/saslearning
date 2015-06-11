package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class Artifact extends Artifact_Base {
    
	
    public Artifact() {
        super();
    }

	public List<AnnotationJ> getAnnotations() {
		Gson gson = new Gson();
		List<AnnotationJ> annotations = new ArrayList<AnnotationJ>(); 
		for(Annotation a : getAnnotationSet()){
			AnnotationJ ann = gson.fromJson(a.getAnnotation(), AnnotationJ.class);
			annotations.add(ann);
		}
		return annotations;
	}
	
	public boolean containsAnnotation(String id){
		if(!getAnnotationSet().isEmpty()){
			for(Annotation ann : getAnnotationSet()){
				if(ann.getExternalId().equals(id)){
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
    		a.setArtifact(null);
    	}
		setScenario(null);
		deleteDomainObject();
	}
    
}
