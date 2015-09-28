package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class Connector extends Connector_Base {
    
    public Connector() {
        super();
    }
    
    public List<AnnotationJ> getJsonAnnotations(){
    	Gson g = new Gson();
    	List<AnnotationJ> list = new ArrayList<AnnotationJ>(); 
    	for(Annotation a : getAnnotationSet()) {
    		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
    		list.add(ann);
    	}
    	return list;
    	
    }

	public void delete() {
		Iterator<Annotation> itA = getAnnotationSet().iterator();
		while(itA.hasNext()) {
			Annotation a = itA.next();
			a.updateConnection(null);
			removeAnnotation(a);
		}
		
		Iterator<Role> itR = getRoleSet().iterator();
		while(itR.hasNext()) {
			Role r = itR.next();
			removeRole(r);
			r.delete();
		}
		setDocument(null);
		deleteDomainObject();
	}
}
