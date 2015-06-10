package pt.ulisboa.tecnico.saslearning.domain;

public class Tag extends Tag_Base {

	public Tag() {
		super();
	}

	public void delete() {
		setRoot(null);
		deleteDomainObject();
	}

}
