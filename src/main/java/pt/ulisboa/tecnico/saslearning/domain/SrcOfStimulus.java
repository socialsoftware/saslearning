package pt.ulisboa.tecnico.saslearning.domain;

import java.util.Iterator;

public class SrcOfStimulus extends SrcOfStimulus_Base {
    
	
    public SrcOfStimulus() {
        super();
    }

	@Override
	public void delete() {
		Iterator<Annotation> i = getAnnotationSet().iterator();
    	while(i.hasNext()) {
    		Annotation a = i.next();
    		a.setSrcOfStimulus(null);
    	}
		setScenario(null);
		deleteDomainObject();
	}
    
}
