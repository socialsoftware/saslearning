package pt.ulisboa.tecnico.saslearning.domain;

public class SrcOfStimulus extends SrcOfStimulus_Base {
    
    public SrcOfStimulus() {
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
