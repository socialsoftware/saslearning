package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class Scenario extends Scenario_Base {
    
	
    public Scenario() {
        super();
    }
    
    public Map<String, ScenarioElement> getScenarioElements(){
    	Map<String, ScenarioElement> elements = new LinkedHashMap<String, ScenarioElement>();
    	elements.put("Source Of Stimulus", getSrcOfStimulus());
    	elements.put("Stimulus", getStimulus());
    	elements.put("Artifact", getArtifact());
    	elements.put("Environment", getEnvironment());
    	elements.put("Response", getResponse());
    	elements.put("Response Measure", getResponseMeasure());
    	return elements;
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
	
	private List<ScenarioElement> getElements(){
		List<ScenarioElement> elems = new ArrayList<ScenarioElement>(6);
		elems.add(getArtifact());
		elems.add(getEnvironment());
		elems.add(getResponse());
		elems.add(getResponseMeasure());
		elems.add(getSrcOfStimulus());
		elems.add(getStimulus());
		return elems;
	}
    
    public void delete() {
    	Iterator<Annotation> i = getAnnotationSet().iterator();
    	while(i.hasNext()) {
    		Annotation a = i.next();
    		a.setScenario(null);
    	}
    	
    	Iterator<ScenarioElement> s = getElements().iterator();
    	while(s.hasNext()) {
    		ScenarioElement e = s.next();
    		if(e != null) {
    			e.delete();
    		}
    	}
    	setDocument(null);
    	deleteDomainObject();
    }
    
}
