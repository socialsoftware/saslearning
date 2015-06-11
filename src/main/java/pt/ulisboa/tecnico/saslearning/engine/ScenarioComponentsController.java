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
import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulus;
import pt.ulisboa.tecnico.saslearning.domain.Stimulus;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

@Controller
public class ScenarioComponentsController {
	@RequestMapping(value = "/addSrcOfStimulus/{docId}/{scenarioId}")
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
			elem.setName("Source of Stimulus");
			s.setSrcOfStimulus(elem);
		}
	}

	@RequestMapping(value = "/addStimulus/{docId}/{scenarioId}")
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
	
	@RequestMapping(value = "/addArtifact/{docId}/{scenarioId}")
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
	
	@RequestMapping(value = "/addEnvironment/{docId}/{scenarioId}")
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
	
	
	@RequestMapping(value = "/addResponse/{docId}/{scenarioId}")
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
	
	@RequestMapping(value = "/addResponseMeasure/{docId}/{scenarioId}")
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
	
	
	//---------------------------------------------------------------------
	@RequestMapping(value="/linkAnnotation/{docId}/SrcOfStimulus/{elemId}")
	public String srcStimulusManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		m.addAttribute("elem", getSrcOfStimulusById(elemId));
		m.addAttribute("annotations", getAnnotationsByTag("Source Of Stimulus", docId));
		return "scenarioElementsManager";
	}
	
	@Atomic(mode=TxMode.READ)
	private SrcOfStimulus getSrcOfStimulusById(String elemId) {
		SrcOfStimulus elem = FenixFramework.getDomainObject(elemId);
		return elem;
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Stimulus/{elemId}")
	public String stimulusManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		m.addAttribute("elem", getStimulusById(elemId));
		m.addAttribute("annotations", getAnnotationsByTag("Stimulus", docId));
		return "scenarioElementsManager";
	}
	
	@Atomic(mode=TxMode.READ)
	private Stimulus getStimulusById(String elemId) {
		Stimulus elem = FenixFramework.getDomainObject(elemId);
		return elem;
	}
	
	
	@RequestMapping(value="/linkAnnotation/{docId}/Artifact/{elemId}")
	public String artifactManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		m.addAttribute("elem", getArtifactById(elemId));
		m.addAttribute("annotations", getAnnotationsByTag("Artifact", docId));
		return "scenarioElementsManager";
	}
	
	@Atomic(mode=TxMode.READ)
	private Artifact getArtifactById(String elemId) {
		Artifact elem = FenixFramework.getDomainObject(elemId);
		return elem;
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Environment/{elemId}")
	public String environmentManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		m.addAttribute("elem", getEnvironmentById(elemId));
		m.addAttribute("annotations", getAnnotationsByTag("Environment", docId));
		return "scenarioElementsManager";
	}
	
	@Atomic(mode=TxMode.READ)
	private Environment getEnvironmentById(String elemId) {
		Environment elem = FenixFramework.getDomainObject(elemId);
		return elem;
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Response/{elemId}")
	public String responseManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		m.addAttribute("elem", getResponseById(elemId));
		m.addAttribute("annotations", getAnnotationsByTag("Response", docId));
		return "scenarioElementsManager";
	}
	
	@Atomic(mode=TxMode.READ)
	private Response getResponseById(String elemId) {
		Response elem = FenixFramework.getDomainObject(elemId);
		return elem;
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/ResponseMeasure/{elemId}")
	public String responseMeasureManager(@PathVariable String docId, @PathVariable String elemId, Model m) {
		m.addAttribute("docId", docId);
		m.addAttribute("elem", getResponseMeasureById(elemId));
		m.addAttribute("annotations", getAnnotationsByTag("Response Measure", docId));
		return "scenarioElementsManager";
	}
	
	@Atomic(mode=TxMode.READ)
	private ResponseMeasure getResponseMeasureById(String elemId) {
		ResponseMeasure elem = FenixFramework.getDomainObject(elemId);
		return elem;
	}
	
	@Atomic(mode=TxMode.READ)
	private List<AnnotationJ> getAnnotationsByTag(String tag, String docId){
		List<AnnotationJ> annotations = new ArrayList<AnnotationJ>();
		Document d = FenixFramework.getDomainObject(docId);
		Gson gson = new Gson();
		for(Annotation a : d.getAnnotationSet()){
			if(a.getTag().equals(tag)){
				AnnotationJ ann = gson.fromJson(a.getAnnotation(), AnnotationJ.class);
				annotations.add(ann);
			}
		}
		return annotations;
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
	//----------------------------------------------------------------------
	@RequestMapping(value="/removeSrcOfStimulus/{docId}/{elemId}")
	public RedirectView removeSrcOfStimulus(@PathVariable String docId, @PathVariable String elemId) {
		removeSrcOfStimulus(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void removeSrcOfStimulus(String srcId) {
		SrcOfStimulus src = FenixFramework.getDomainObject(srcId);
		src.delete();
	}
	
	@RequestMapping(value="/removeStimulus/{docId}/{elemId}")
	public RedirectView removeStimulus (@PathVariable String docId, @PathVariable String elemId) {
		removeStimulus(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void removeStimulus(String elemId) {
		Stimulus s = FenixFramework.getDomainObject(elemId);
		s.delete();
	}

	@RequestMapping(value="/removeArtifact/{docId}/{elemId}")
	public RedirectView removeArtifact(@PathVariable String docId, @PathVariable String elemId) {
		removeArtifact(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void removeArtifact(String elemId) {
		Artifact a = FenixFramework.getDomainObject(elemId);
		a.delete();
	}

	@RequestMapping(value="/removeEnvironment/{docId}/{elemId}")
	public RedirectView removeEnvironment(@PathVariable String docId, @PathVariable String elemId) {
		removeEnvironment(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void removeEnvironment(String elemId) {
		Environment e = FenixFramework.getDomainObject(elemId);
		e.delete();
	}

	@RequestMapping(value="/removeResponse/{docId}/{elemId}")
	public RedirectView removeResponse(@PathVariable String docId, @PathVariable String elemId) {
		removeResponse(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void removeResponse(String elemId) {
		Response r = FenixFramework.getDomainObject(elemId);
		r.delete();
	}
	
	@RequestMapping(value="/removeResponseMeasure/{docId}/{elemId}")
	public RedirectView removeResponseMeasure(@PathVariable String docId, @PathVariable String elemId) {
		removeResponseMeasure(elemId);
		RedirectView rv = new RedirectView("/addSyntax/" + docId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeResponseMeasure(String elemId) {
		ResponseMeasure rm = FenixFramework.getDomainObject(elemId);
		rm.delete();
	}
//-------------------------------------------------------------------------------------------------
	@RequestMapping(value="/unlinkAnnotation/Source of Stimulus/{docId}/{srcId}/{annId}")
	public RedirectView unlinkAnnotationFromSrcStimulus(@PathVariable String docId, @PathVariable String srcId, @PathVariable String annId) {
		removeAnnotationFromSrcStimulus(srcId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/"+docId+"/SrcOfStimulus/" + srcId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromSrcStimulus(String srcId, String annotationId) {
		SrcOfStimulus s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setSrcOfStimulus(null);
	}
	
	@RequestMapping(value="/unlinkAnnotation/Stimulus/{docId}/{elemId}/{annId}")
	public RedirectView unlinkAnnotationFromStimulus(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		removeAnnotationFromStimulus(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/"+docId+"/Stimulus/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromStimulus(String elemId, String annotationId) {
		Stimulus s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setStimulus(null);
	}
	
	@RequestMapping(value="/unlinkAnnotation/Artifact/{docId}/{elemId}/{annId}")
	public RedirectView unlinkAnnotationFromArtifact(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		removeAnnotationFromArtifact(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/"+docId+"/Artifact/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromArtifact(String elemId, String annotationId) {
		Artifact s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setArtifact(null);
	}
	
	@RequestMapping(value="/unlinkAnnotation/Environment/{docId}/{elemId}/{annId}")
	public RedirectView unlinkAnnotationFromEnvironment(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		removeAnnotationFromEnvironment(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/"+docId+"/Environment/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromEnvironment(String elemId, String annotationId) {
		Environment s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setEnvironment(null);
	}
	
	@RequestMapping(value="/unlinkAnnotation/Response/{docId}/{elemId}/{annId}")
	public RedirectView unlinkAnnotationFromResponse(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		removeAnnotationFromResponse(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/"+docId+"/Response/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromResponse(String elemId, String annotationId) {
		Response s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setResponse(null);
	}
	
	@RequestMapping(value="/unlinkAnnotation/Response Measure/{docId}/{elemId}/{annId}")
	public RedirectView unlinkAnnotationFromResponseMeasure(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		removeAnnotationFromResponseMeasure(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/"+docId+"/ResponseMeasure/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromResponseMeasure(String elemId, String annotationId) {
		ResponseMeasure s = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.removeAnnotation(a);
		a.setResponseMeasure(null);
	}
	
	
	
//---------------------------------------------------------------------------------------------	
	@RequestMapping(value="/linkAnnotation/{docId}/Source of Stimulus/{elemId}/{annId}")
	public RedirectView linkToSrcStimulus(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToSrcStimulus(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/" + docId + "/SrcOfStimulus/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToSrcStimulus(String srcId, String annId){
		SrcOfStimulus s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Stimulus/{elemId}/{annId}")
	public RedirectView linkToStimulus(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToStimulus(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/" + docId + "/Stimulus/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToStimulus(String srcId, String annId){
		Stimulus s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Artifact/{elemId}/{annId}")
	public RedirectView linkToArtifact(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToArtifact(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/" + docId + "/Artifact/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToArtifact(String srcId, String annId){
		Artifact s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Environment/{elemId}/{annId}")
	public RedirectView linkToEnvironment(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToEnvironment(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/" + docId + "/Environment/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToEnvironment(String srcId, String annId){
		Environment s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Response/{elemId}/{annId}")
	public RedirectView linkToResponse(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToResponse(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/" + docId + "/Response/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToResponse(String srcId, String annId){
		Response s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
	
	@RequestMapping(value="/linkAnnotation/{docId}/Response Measure/{elemId}/{annId}")
	public RedirectView linkToResponseMeasure(@PathVariable String docId, @PathVariable String elemId, @PathVariable String annId) {
		linkAnnotationToResponseMeasure(elemId, annId);
		RedirectView rv = new RedirectView("/linkAnnotation/" + docId + "/ResponseMeasure/" + elemId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToResponseMeasure(String srcId, String annId){
		ResponseMeasure s = FenixFramework.getDomainObject(srcId);
		Annotation a = FenixFramework.getDomainObject(annId);
		s.addAnnotation(a);
	}
	
	
}
