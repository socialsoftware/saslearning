package pt.ulisboa.tecnico.saslearning.domain;

public class Environment extends Environment_Base {
    
    public Environment() {
        super();
    }
    
    @Override
    public void removeScenario() {
    	setScenario(null);
    }
}
