package pt.ulisboa.tecnico.saslearning.domain;

public class Stimulus extends Stimulus_Base {
    
    public Stimulus() {
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
