package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class Module extends Module_Base {
    
    public Module() {
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
    
    public List<AnnotationJ> getAnnotationsByTag(String tag){
    	List<AnnotationJ> resp = new ArrayList<AnnotationJ>();
    	Gson g = new Gson();
    	for(Annotation ann : getAnnotationSet()) {
    		if(ann.getTag().equals(tag)) {
    			AnnotationJ a = g.fromJson(ann.getAnnotation(), AnnotationJ.class);
    			resp.add(a);
    		}
    	}
    	return resp;
    }
    
    public List<AnnotationJ> getModuleResponsibilities(){
    	List<AnnotationJ> resp = new ArrayList<AnnotationJ>();
    	Gson g = new Gson();
    	for(Annotation ann : getAnnotationSet()) {
    		if(ann.getTag().equals("Module - Responsibility")) {
    			AnnotationJ a = g.fromJson(ann.getAnnotation(), AnnotationJ.class);
    			resp.add(a);
    		}
    	}
    	return resp;
    }
    
    public List<AnnotationJ> getModuleInterfaces(){
    	List<AnnotationJ> resp = new ArrayList<AnnotationJ>();
    	Gson g = new Gson();
    	for(Annotation ann : getAnnotationSet()) {
    		if(ann.getTag().equals("Module - Interface")) {
    			AnnotationJ a = g.fromJson(ann.getAnnotation(), AnnotationJ.class);
    			resp.add(a);
    		}
    	}
    	return resp;
    }    
    
    public List<AnnotationJ> getModuleImplDetails(){
    	List<AnnotationJ> resp = new ArrayList<AnnotationJ>();
    	Gson g = new Gson();
    	for(Annotation ann : getAnnotationSet()) {
    		if(ann.getTag().equals("Module - Implementation Details")) {
    			AnnotationJ a = g.fromJson(ann.getAnnotation(), AnnotationJ.class);
    			resp.add(a);
    		}
    	}
    	
    	return resp;
    }  
    
    
	public void delete() {
		Iterator<Annotation> it = getAnnotationSet().iterator();
		while(it.hasNext()) {
			Annotation a = it.next();
			a.updateConnection(null);
			a.setModule(null);
			removeAnnotation(a);
		}
		setDocument(null);
		deleteDomainObject();
	}
    
}
