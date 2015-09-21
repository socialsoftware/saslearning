package pt.ulisboa.tecnico.saslearning.domain;

public class QualityRequirement extends QualityRequirement_Base {
    
    public QualityRequirement() {
        super();
    }

	public void delete() {
		deleteDomainObject();
	}
    
}
