package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class View extends View_Base {
    
    public View() {
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
    	Iterator<Annotation> it = getAnnotationSet().iterator();
		while(it.hasNext()) {
			Annotation a = it.next();
			a.updateConnection(null);
			a.setView(null);
			removeAnnotation(a);
		}
		Iterator<Module> itm = getModuleSet().iterator();
		while(itm.hasNext()) {
			Module m = itm.next();
			m.removeView(this);
			removeModule(m);
		}
		setDocument(null);
		deleteDomainObject();
    }
}
