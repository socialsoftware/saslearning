package pt.ulisboa.tecnico.saslearning.engine;


public class TagAux{

	private String id;
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return tag;
	}

}
