package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StimulusFragment extends StimulusFragment_Base {
    
    public StimulusFragment() {
        super();
    }
    
    @Override
    public List<String> possibleConnections() {
    	List<String> conns = new ArrayList<String>(1);
    	if(getScenario() == null) {
    		conns.add("Scenario");
    	}
    	return conns;
    }
    
    @Override
    public Map<String, ElementFragment> connectedFragments() {
    	Map<String, ElementFragment> m = new HashMap<String, ElementFragment>(1); 
    	if(getScenario() != null) {
    		m.put("Scenario", getScenario());
    	}
    	return m;
    }
    
    @Override
    public boolean hasConnections() {
    	return getScenario() != null;
    }
    
    @Override
    public void passConnectionsToChild() {
    	if(getChild() != null && getChild() instanceof StimulusFragment) {
    		StimulusFragment child = (StimulusFragment) getChild();
    		if(getScenario() != null) {
    			child.setScenario(getScenario());
    			setScenario(null);
    		}
    	}
    }
    
    @Override
    public void removeConnections() {
    	setScenario(null);
    }
}
