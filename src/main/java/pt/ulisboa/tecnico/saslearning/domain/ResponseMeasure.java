package pt.ulisboa.tecnico.saslearning.domain;

public class ResponseMeasure extends ResponseMeasure_Base {
    
    public ResponseMeasure() {
        super();
    }

	public void delete() {
		setScenario(null);
		deleteDomainObject();
	}
    
}
