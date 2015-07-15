package pt.ulisboa.tecnico.saslearning.documents;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.User;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;
import pt.ulisboa.tecnico.saslearning.jsonsupport.Tactic;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

import com.google.gson.Gson;

@RestController
public class AnnotationController {
	Utils utils = new Utils();

	// INDEX
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations", method = RequestMethod.GET)
	public String getAnnotations(@PathVariable String docId) {
		String resp = getDocAnnotations(docId);
		return resp;
	}

	// READ
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations/{annId}", method = RequestMethod.GET)
	public String readAnnotation(@PathVariable String annId) {
		return getSingleAnnotation(annId);
	}

	// CREATE
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations", method = RequestMethod.POST)
	public RedirectView saveAnnotation(@PathVariable String docId,
			@RequestBody String annot, Principal user)
			throws IOException {
		String annId = writeAnnotation(user.getName(), annot, docId);
		RedirectView rv = new RedirectView("/selectDoc/" + docId
				+ "/store/annotations/" + annId);
//		RedirectView rv = new RedirectView("/scenarioManager");
		rv.setStatusCode(HttpStatus.SEE_OTHER);
		return rv;
	}

	// UPDATE
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations/{annId}", method = RequestMethod.PUT)
	public RedirectView updateAnnotation(@PathVariable String annId,
			@PathVariable String docId, @RequestBody String annot) throws IOException {
		updateAnnotationById(annId, annot);
		RedirectView rv = new RedirectView("/selectDoc/" + docId
				+ "/store/annotations/" + annId);
		rv.setStatusCode(HttpStatus.SEE_OTHER);
		return rv;
	}

	// DELETE
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations/{annId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAnnotation(@PathVariable String annId) {
		deleteDocumentAnnotation(annId);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	
	//TACTICS
	@RequestMapping(value = "/selectDoc/{docId}/store/addTactic/{annId}", method=RequestMethod.POST)
	public RedirectView addTactic(
			@PathVariable String docId, @PathVariable String annId,
			@RequestBody String body){
		Gson g = new Gson();
		Tactic t = g.fromJson(body, Tactic.class);
		Annotation a = FenixFramework.getDomainObject(annId);
		addTacticToAnnotation(a, t.getTactic());
		RedirectView rv = new RedirectView("/selectDoc/" + docId
				+ "/store/annotations/" + annId);
		rv.setStatusCode(HttpStatus.SEE_OTHER);
		return rv;
		
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addTacticToAnnotation(Annotation a, String tactic) {
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		ann.setTactic(tactic);
		String json = g.toJson(ann);
		a.setAnnotation(json);
	}

	@RequestMapping(value = "/annotator/getTags")
	public String getTags(){
		return Utils.getJsonTags();
	}
	
	@RequestMapping(value = "getDoc/getTactics/{type}")
	public String getTactics(@PathVariable String type) {
		return Utils.getTactics(type);
	}
	
	@Atomic
	private String getDocAnnotations(String docId) {
		Document d = FenixFramework.getDomainObject(docId);
		AnnotationJ[] annotations = getAnnotationArray(d);
		Gson gson = new Gson();
		
		return gson.toJson(annotations);
	}
	
	private AnnotationJ[] getAnnotationArray(Document d){
		Gson gson = new Gson();
		Set<Annotation> annotations = d.getAnnotationSet();
		int i = 0;
		int n = annotations.size();
		AnnotationJ[] annotationsJson = new AnnotationJ[n];
		for (Annotation an : annotations) {
			AnnotationJ a = gson.fromJson(an.getAnnotation(), AnnotationJ.class);
			annotationsJson[i] = a;
			i++;
		}
		return annotationsJson;
	}
	
	@Atomic
	private String getSingleAnnotation(String annId){
		Annotation ann = FenixFramework.getDomainObject(annId);
		return ann.getAnnotation();
	}
	
	
	@Atomic(mode = TxMode.WRITE)
	private String writeAnnotation(String username, String annotation, String docId) {
		Document d = FenixFramework.getDomainObject(docId);
		User u = getUserByUsername(username);
		Annotation ann = new Annotation();
		String annId = ann.getExternalId();
		Gson gson = new Gson();
		AnnotationJ jsonObject = gson.fromJson(annotation, AnnotationJ.class);
		jsonObject.setId(annId);
		jsonObject.setUser(username);
		ann.setAnnotation(gson.toJson(jsonObject));
		ann.setTag(jsonObject.getTag());
		d.addAnnotation(ann);
		u.addAnnotation(ann);
		return annId;
	}
	
	@Atomic
	private User getUserByUsername(String username){
		Set<User> users = FenixFramework.getDomainRoot().getUserSet();
		for(User u : users){
			if(u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	@Atomic
	private void updateAnnotationById(String annId, String annot) {
		Annotation a = FenixFramework.getDomainObject(annId);
		Gson gson = new Gson();
		AnnotationJ ann = gson.fromJson(annot, AnnotationJ.class);
		a.setTag(ann.getTag());
		a.setAnnotation(annot);
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void deleteDocumentAnnotation(String annId) {
		Annotation ann = FenixFramework.getDomainObject(annId);
		ann.delete();
	}
}
