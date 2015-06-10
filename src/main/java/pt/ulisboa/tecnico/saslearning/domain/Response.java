package pt.ulisboa.tecnico.saslearning.domain;

public class Response extends Response_Base {
    
    public Response() {
        super();
    }

	public void delete() {
		setScenario(null);
		deleteDomainObject();
	}
    
}
