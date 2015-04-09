package pt.ulisboa.tecnico.saslearning.domain;

public class Document extends Document_Base {
    
    public Document() {
        super();
        
    }
    
    @Override
    public String toString() {
    	return getUrl();
    }
    
    public void delete(){
    	for(Annotation a : getAnnotationSet()){
    		removeAnnotation(a);
    		a.delete();
    	}
    	setRoot(null);
    	deleteDomainObject();
    }
    
}
