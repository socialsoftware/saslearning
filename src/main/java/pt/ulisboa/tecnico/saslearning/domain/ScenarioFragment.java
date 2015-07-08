package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioFragment extends ScenarioFragment_Base {
    
    public ScenarioFragment() {
        super();
    }
    @Override
    public List<String> possibleConnections() {
    	List<String> conns = new ArrayList<String>(1);
    	if(getStimulus() == null) {
    		conns.add("Stimulus");
    	}
    	return conns;
    }
    
    @Override
    public Map<String, ElementFragment> connectedFragments() {
    	Map<String, ElementFragment> m = new HashMap<String, ElementFragment>(1); 
    	if(getStimulus() != null) {
    		m.put("Stimulus", getStimulus());
    	}
    	return m;
    }
    
    @Override
    public boolean hasConnections() {
    	return getStimulus() != null;
    }
    
    @Override
    public void passConnectionsToChild() {
    	if(getChild() != null && getChild() instanceof ScenarioFragment) {
    		ScenarioFragment child = (ScenarioFragment) getChild();
    		if(getStimulus() != null) {
    			child.setStimulus(getStimulus());
    			setStimulus(null);
    		}
    	}
    }
    
    @Override
    public void removeConnections() {
    	setStimulus(null);
    }
    
    @Override
    public void connect(ElementFragment e) {
    	if(e instanceof StimulusFragment) {
    		StimulusFragment s = (StimulusFragment) e;
    		setStimulus(s);
    		s.setScenario(this);
    		setLinked(true);
    		s.setLinked(true);
    	}
    }
    
    @Override
    public void unlink(ElementFragment e) {
    	if(e instanceof StimulusFragment) {
    		StimulusFragment s = (StimulusFragment) e;
    		s.unlink(this);
    	}
    }
}
