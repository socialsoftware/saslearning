package pt.ulisboa.tecnico.saslearning.domain;

public class ResponseMeasure extends ResponseMeasure_Base {
    
    public ResponseMeasure() {
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
