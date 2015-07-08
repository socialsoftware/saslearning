package pt.ulisboa.tecnico.saslearning.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

public class ElementFragment extends ElementFragment_Base {

	public ElementFragment() {
		super();
	}

	public AnnotationJ getAnnotationJson() {
		Gson gson = new Gson();
		AnnotationJ ann = gson.fromJson(getAnnotation().getAnnotation(),
				AnnotationJ.class);
		return ann;
	}

	public List<ElementFragment> getChildren() {
		if (getChild() == null) {
			return null;
		}
		List<ElementFragment> children = new ArrayList<ElementFragment>();
		ElementFragment e = this;
		while (e.getChild() != null) {
			children.add(e.getChild());
			e = e.getChild();
		}
		return children;

	}

	public ElementFragment getChainHead() {
		if (getLinked()) {
			ElementFragment e = this;
			while (e.getParent() != null) {
				e = e.getParent();
			}
			return e;
		}
		return null;
	}
	
	public void delete() {
		if (getParent() != null) { //tem um pai (nao é cabeça)
			if (getChild() != null) { //tem filhos (esta no meio da chain)
				getParent().setChild(getChild());
				setChild(null);
			} else { //está no fim da chain
				setParent(null);
			}
		}else { //cabeça - é preciso gerir as ligações
			ElementFragment child = getChild();
			if(child != null) { // tem filhos
				//passar as coisas para os filhos
				child.setDocument(getDocument());
				passConnectionsToChild();
				if(child.getChild() == null && !child.hasConnections()) { //o filho fica sozinho na chain e nao está ligado a nada
					//se estiver ligado a outros tipos de fragmentos, tem de continuar a ser considerado uma cabeça
					child.setLinked(false);
				}
				setChild(null);
			}else { //cabeça sem filhos
				removeConnections();
			}
		}
		setDocument(null);
		setAnnotation(null);
		deleteDomainObject();
	}
	
	// ---------------------- TO OVERRIDE -------------------------------
	public void removeConnections() {
	}

	public void passConnectionsToChild() {}

	public List<String> possibleConnections() {
		return null;
	}

	public Map<String, ElementFragment> connectedFragments() {
		return null;
	}
	
	
	public boolean hasConnections() {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void connect(ElementFragment e) {}
	
	@SuppressWarnings("unused")
	public void unlink(ElementFragment unlink) {
	}
	
	//-------------------------------------------------------
	@Override
	public String toString() {
		return getName() + " " + getExternalId();
	}


}
