package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class Component extends Component_Base {
    
    public Component() {
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
		
		Iterator<Port> itP = getPortSet().iterator();
		while(itP.hasNext()) {
			Port p = itP.next();
			removePort(p);
			p.delete();
		}
		setDocument(null);
		deleteDomainObject();
	}
    
}
