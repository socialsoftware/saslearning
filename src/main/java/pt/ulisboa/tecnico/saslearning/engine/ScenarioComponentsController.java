package pt.ulisboa.tecnico.saslearning.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Artifact;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.Environment;
import pt.ulisboa.tecnico.saslearning.domain.Response;
import pt.ulisboa.tecnico.saslearning.domain.ResponseMeasure;
import pt.ulisboa.tecnico.saslearning.domain.Scenario;
import pt.ulisboa.tecnico.saslearning.domain.ScenarioElement;
import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulus;
import pt.ulisboa.tecnico.saslearning.domain.Stimulus;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

@Controller
public class ScenarioComponentsController {

	@RequestMapping(value="/scenarioElementFragment")
	public String getElementFragment() {
		return "scenarioElementFragment";
	}
	
	@RequestMapping(value = "/add/Source Of Stimulus/{docId}/{scenarioId}")
	public RedirectView addSourceOfStimulus(@PathVariable String scenarioId,
			@PathVariable String docId) {
		addSrcOfStimulus(scenarioId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addSrcOfStimulus(String scenarioId){
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		if(s.getSrcOfStimulus() == null) {
			SrcOfStimulus elem = new SrcOfStimulus();
			elem.setName("Source Of Stimulus");
			s.setSrcOfStimulus(elem);
		}
	}

	@RequestMapping(value = "/add/Stimulus/{docId}/{scenarioId}")
	public RedirectView addStimulus(@PathVariable String scenarioId,
			@PathVariable String docId) {
		addStimulus(scenarioId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addStimulus(String scenarioId){
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		if(s.getStimulus() == null) {
			Stimulus elem = new Stimulus();
			elem.setName("Stimulus");
			s.setStimulus(elem);
		}
	}
	
	@RequestMapping(value = "/add/Artifact/{docId}/{scenarioId}")
	public RedirectView addArtifact(@PathVariable String scenarioId,
			@PathVariable String docId) {
		addArtifact(scenarioId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addArtifact(String scenarioId){
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		if(s.getArtifact() == null) {
			Artifact elem = new Artifact();
			elem.setName("Artifact");
			s.setArtifact(elem);
		}
	}
	
	@RequestMapping(value = "/add/Environment/{docId}/{scenarioId}")
	public RedirectView addEnvironment(@PathVariable String scenarioId,
			@PathVariable String docId) {
		addEnvironment(scenarioId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addEnvironment(String scenarioId){
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		if(s.getEnvironment() == null) {
			Environment elem = new Environment();
			elem.setName("Environment");
			s.setEnvironment(elem);
		}
	}
	
	
	@RequestMapping(value = "/add/Response/{docId}/{scenarioId}")
	public RedirectView addResponse(@PathVariable String scenarioId,
			@PathVariable String docId) {
		addResponse(scenarioId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addResponse(String scenarioId){
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		if(s.getResponse() == null) {
			Response elem = new Response();
			elem.setName("Response");
			s.setResponse(elem);
		}
	}
	
	@RequestMapping(value = "/add/Response Measure/{docId}/{scenarioId}")
	public RedirectView addResponseMeasure(@PathVariable String scenarioId,
			@PathVariable String docId) {
		addResponseMeasure(scenarioId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addResponseMeasure(String scenarioId){
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		if(s.getResponseMeasure() == null) {
			ResponseMeasure elem = new ResponseMeasure();
			elem.setName("Response Measure");
			s.setResponseMeasure(elem);
		}
	}
	
	@Atomic(mode=TxMode.READ)
	private List<AnnotationJ> getAnnotationsByTag(String tag, String docId){
		List<AnnotationJ> annotations = new ArrayList<AnnotationJ>();
		Document d = FenixFramework.getDomainObject(docId);
		Gson gson = new Gson();
		for(Annotation a : d.getAnnotationSet()){
			if(a.getTag().equals(tag) && !a.hasScenarioElement()){
				AnnotationJ ann = gson.fromJson(a.getAnnotation(), AnnotationJ.class);
				annotations.add(ann);
			}
		}
		return annotations;
	}
	
	//---------------------------------------------------------------------
	@RequestMapping(value="/elementManager/{docId}/{elemId}")
	public String scenarioElementsManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		ScenarioElement elem = FenixFramework.getDomainObject(elemId);
		m.addAttribute("elem", elem);
		m.addAttribute("annotations", getAnnotationsByTag(elem.getName(), docId));
		return "scenarioElementsManager";
	}

	@RequestMapping(value="/removeScenarioElement/{docId}/{elemId}")
	public RedirectView removeScenarioElement(@PathVariable String docId, @PathVariable String elemId) {
		removeScenarioElement(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}

	@RequestMapping(value="/unlinkAnnotation/{docId}/{elemId}/{annId}")
	public RedirectView unlinkAnnotationFromElement(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		removeAnnotationFromElement(elemId, annId);
		RedirectView rv = new RedirectView("/elementManager/"+docId+"/" + elemId);
		return rv;
	}

	@RequestMapping(value="/linkAnnotationToElement/{docId}/{elemId}/{annId}")
	public RedirectView linkAnnotation(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToElement(elemId, annId);
		RedirectView rv = new RedirectView("/elementManager/" + docId + "/" + elemId);
		return rv;
	}

	@Atomic(mode=TxMode.READ)
	private List<AnnotationJ> getAnnotationsFromSet(Set<Annotation> anns){
		Gson gson = new Gson();
		List<AnnotationJ> annotations = new ArrayList<AnnotationJ>(); 
		for(Annotation a : anns){
			AnnotationJ ann = gson.fromJson(a.getAnnotation(), AnnotationJ.class);
			annotations.add(ann);
		}
		return annotations;
	}

	@Atomic(mode=TxMode.WRITE)
	private void removeScenarioElement(String elemId) {
		ScenarioElement e = FenixFramework.getDomainObject(elemId);
		e.delete();
	}

	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromElement(String elemId, String annotationId) {
		ScenarioElement s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setSrcOfStimulus(null);
	}
		
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToElement(String elemId, String annId){
		ScenarioElement s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
}
