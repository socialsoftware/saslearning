package pt.ulisboa.tecnico.saslearning.domain;

public class Document extends Document_Base {
    
    public Document() {
        super();
        
    }
    
    @Override
    public String toString() {
    	return getUrl();
    }
    
}
