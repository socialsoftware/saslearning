package pt.ulisboa.tecnico.saslearning.domain;

public class Response extends Response_Base {
    
    public Response() {
        super();
    }
    
    @Override
    public void removeScenario() {
    	setScenario(null);
    }
    @Override
    public Scenario getEnclosingScenario() {
    	return getScenario();
    }
}
