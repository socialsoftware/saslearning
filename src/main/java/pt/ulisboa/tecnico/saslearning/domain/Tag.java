package pt.ulisboa.tecnico.saslearning.domain;

import java.util.Iterator;

public class Tag extends Tag_Base {

	public Tag() {
		super();
	}

	public void delete() {
		setRoot(null);
		setSupertag(null);
		Iterator<Tag> it = getSubtagSet().iterator();
		while (it.hasNext()) {
			Tag child = it.next();
			child.delete();
		}
		deleteDomainObject();
	}

}
