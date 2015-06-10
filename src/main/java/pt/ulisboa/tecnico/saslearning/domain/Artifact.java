package pt.ulisboa.tecnico.saslearning.domain;

public class Artifact extends Artifact_Base {
    
    public Artifact() {
        super();
    }

	public void delete() {
		setScenario(null);
		deleteDomainObject();
	}
    
}
