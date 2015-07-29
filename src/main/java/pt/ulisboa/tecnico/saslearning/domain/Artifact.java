package pt.ulisboa.tecnico.saslearning.domain;

public class Artifact extends Artifact_Base {
    
    public Artifact() {
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
