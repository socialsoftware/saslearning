package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

public class ElementFragment extends ElementFragment_Base {
    
    public ElementFragment() {
        super();
    }
    
    public AnnotationJ getAnnotationJson() {
    	Gson gson = new Gson();
    	AnnotationJ ann = gson.fromJson(getAnnotation().getAnnotation(), AnnotationJ.class);
    	return ann;
    }
    
    public List<ElementFragment> getChildren(){
    	if(getChild() == null) {
    		return null;
    	}
    	List<ElementFragment> children = new ArrayList<ElementFragment>();
    	ElementFragment e = this;
    	while(e.getChild() != null) {
    		children.add(e.getChild());
    		e = e.getChild();
    	}
    	return children;
    	
    }
    
    public ElementFragment getChainHead() {
    	if(getLinked()) {
    		ElementFragment e = this;
    		while(e.getParent() != null) {
    			e = e.getParent();
    		}
    		return e;
    	}
    	return null;
    }
    
    public void delete() {
    	if(getParent() != null) {
    		if(getChild() != null) {
    			getParent().setChild(getChild());
    			setChild(null);
    		}else {
    			setParent(null);
    		}
    	}
    	setDocument(null);
    	setAnnotation(null);
    	deleteDomainObject();
    }
}
