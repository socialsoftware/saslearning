package pt.ulisboa.tecnico.saslearning.domain;

public class Environment extends Environment_Base {
    
    public Environment() {
        super();
    }

	public void delete() {
		setScenario(null);
		deleteDomainObject();
	}
    
}
