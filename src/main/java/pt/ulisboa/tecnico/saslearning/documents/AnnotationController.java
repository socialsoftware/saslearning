package pt.ulisboa.tecnico.saslearning.documents;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
import pt.ulisboa.tecnico.saslearning.domain.Component;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.Module;
import pt.ulisboa.tecnico.saslearning.domain.Port;
import pt.ulisboa.tecnico.saslearning.domain.Scenario;
import pt.ulisboa.tecnico.saslearning.domain.ScenarioElement;
import pt.ulisboa.tecnico.saslearning.domain.User;
import pt.ulisboa.tecnico.saslearning.domain.View;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

import com.google.gson.Gson;

@RestController
public class AnnotationController {

	// INDEX
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations", method = RequestMethod.GET)
	public String getAnnotations(@PathVariable String docId) {
		Document d = FenixFramework.getDomainObject(docId);
		List<AnnotationJ> anns = new ArrayList<AnnotationJ>();
		Gson g = new Gson();
		for(Annotation a : d.getAnnotationSet()) {
			AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
			if(a.isViewAnnotation() && a.getView() != null) {
				View v = a.getView();
				ann.setText("View: " + v.getViewtype());
			} else if(a.isScenarioAnnotation() && a.getEnclosingScenario() != null) {
				Scenario s = a.getEnclosingScenario();
				if(a.getScenario() != null) {
					ann.setText("Scenario: " + s.getName());
				}else {
					ScenarioElement elem = a.getScenarioElement();
					ann.setText(elem.getIdentifier() + " of Scenario: " + s.getName());
				}
			} else if(a.isModuleViewtypeAnnotation() && a.getModule() != null) {
				Module m = a.getModule();
				ann.setText("Module: " + m.getName());
			} else if(a.getTag().contains("Component")) {
				if(a.getComponent() != null) {
					Component c = a.getComponent();
					ann.setText("Component: " + c.getName());
				}
				
				if(a.getPort() != null) {
					Port p = a.getPort();
					Component c = p.getComponent();
					ann.setText("Port " + p.getName() + " of Component " + c.getName());
				}
			}
			anns.add(ann);
			
		}
		String resp = g.toJson(anns);
		return resp;
	}

	// READ
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations/{annId}", method = RequestMethod.GET)
	public String readAnnotation(@PathVariable String annId) {
		Annotation a = FenixFramework.getDomainObject(annId);
		return a.getAnnotation();
	}

	// CREATE
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations", method = RequestMethod.POST)
	public RedirectView saveAnnotation(@PathVariable String docId,
			@RequestBody String annot, Principal user)
			throws IOException {
		String annId = writeAnnotation(user.getName(), annot, docId);
		RedirectView rv = new RedirectView("/selectDoc/" + docId
				+ "/store/annotations/" + annId);
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

	@RequestMapping(value = "/annotator/getTags")
	public String getTags(){
		return Utils.getJsonTags();
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
	
	@Atomic(mode=TxMode.WRITE)
	private void updateAnnotationById(String annId, String annot) {
		Annotation a = FenixFramework.getDomainObject(annId);
		Gson gson = new Gson();
		AnnotationJ ann = gson.fromJson(annot, AnnotationJ.class);
		if(!ann.getTag().equals(a.getTag())) {
			a.setTag(ann.getTag());
			a.setModule(null);
			a.setScenario(null);
			a.setScenarioElement(null);
			a.setView(null);
			a.setModule(null);
			a.setComponent(null);
			a.setPort(null);
			a.setConnector(null);
			a.setRole(null);
		}
		
		a.setAnnotation(annot);
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void deleteDocumentAnnotation(String annId) {
		Annotation ann = FenixFramework.getDomainObject(annId);
		ann.delete();
	}
}
