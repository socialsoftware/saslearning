package pt.ulisboa.tecnico.saslearning.engine;

import java.util.List;

public class TagAux implements Comparable<TagAux>{

	private String id;
	private String tag;
	private List<TagAux> subtags;
	private String parentId;

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

	public List<TagAux> getSubtags() {
		return subtags;
	}

	public void setSubtags(List<TagAux> subtags) {
		this.subtags = subtags;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return tag;
	}

	@Override
	public int compareTo(TagAux o) {
		return tag.compareTo(o.getTag());
	}


}
