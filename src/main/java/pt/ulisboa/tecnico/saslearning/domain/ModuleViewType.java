package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;


public class ModuleViewType extends ModuleViewType_Base {
    
    public ModuleViewType() {
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
		Iterator<Module> it = getModuleSet().iterator();
		while(it.hasNext()) {
			Module m = it.next();
			m.delete();
		}
		
		Iterator<Annotation> anns = getAnnotationSet().iterator();
		while(anns.hasNext()) {
			Annotation a = anns.next();
			a.updateConnection(null);
			a.setModuleViewtype(null);
			removeAnnotation(a);
		}
		setDocument(null);
		deleteDomainObject();
	}
    
}
