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
    
    public List<AnnotationJ> getJsonAnnotations(){
    	Gson g = new Gson();
    	List<AnnotationJ> list = new ArrayList<AnnotationJ>(); 
    	for(Annotation a : getAnnotationSet()) {
    		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
    		list.add(ann);
    	}
    	return list;
    	
    }
    
    public Map<String, ScenarioElement> getElements() {
    	Map<String, ScenarioElement> map = new LinkedHashMap<String, ScenarioElement>();
    	map.put("Source Of Stimulus", getSrcOfStimulus());
    	map.put("Stimulus", getStimulus());
    	map.put("Artifact", getArtifact());
    	map.put("Environment", getEnvironment());
    	map.put("Response", getResponse());
    	map.put("Response Measure", getResponseMeasure());
    	return map;
    }

	public void delete() {
		getSrcOfStimulus().delete();
		getStimulus().delete();
		getArtifact().delete();
		getEnvironment().delete();
		getResponse().delete();
		getResponseMeasure().delete();
		Iterator<Tactic> it = getTacticSet().iterator();
		while(it.hasNext()) {
			Tactic t = it.next();
			t.delete();
		}
		Iterator<Annotation> i = getAnnotationSet().iterator();
    	while(i.hasNext()) {
    		Annotation a = i.next();
    		a.updateConnection(null);
    		a.setScenarioElement(null);
    		removeAnnotation(a);
    	}
    	deleteDomainObject();
	}
}
