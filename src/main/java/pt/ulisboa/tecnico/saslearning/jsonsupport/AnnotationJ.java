package pt.ulisboa.tecnico.saslearning.jsonsupport;

public class AnnotationJ {

	private String id;
	private String annotator_schema_version;
	private String created;
	private String updated;
	private String text;
	private String quote;
	private String uri;
	private Range[] ranges;
	private String[] tags;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAnnotator_schema_version() {
		return annotator_schema_version;
	}
	public void setAnnotator_schema_version(String annotator_schema_version) {
		this.annotator_schema_version = annotator_schema_version;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getQuote() {
		return quote;
	}
	public void setQuote(String quote) {
		this.quote = quote;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Range[] getRanges() {
		return ranges;
	}
	public void setRanges(Range[] ranges) {
		this.ranges = ranges;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	
	
}
