package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TacticAvailabilityFragment extends TacticAvailabilityFragment_Base {
    
    public TacticAvailabilityFragment() {
        super();
    }
    @Override
    public List<String> possibleConnections() {
    	List<String> conns = new ArrayList<String>(1);
    	//...
    	return conns;
    }
    
    @Override
    public Map<String, ElementFragment> connectedFragments() {
    	Map<String, ElementFragment> m = new HashMap<String, ElementFragment>(1); 
    	//...
    	return m;
    }
    
    @Override
    public boolean hasConnections() {
    	//...
    	return false;
    }
    
    @Override
    public void passConnectionsToChild() {
    	//...
    }
    
    @Override
    public void removeConnections() {
    	//...
    }
    
    @Override
    public void connect(ElementFragment e) {
    	//...
    }
}
