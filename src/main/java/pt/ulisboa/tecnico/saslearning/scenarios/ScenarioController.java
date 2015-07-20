package pt.ulisboa.tecnico.saslearning.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Artifact;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.Environment;
import pt.ulisboa.tecnico.saslearning.domain.QualityAttribute;
import pt.ulisboa.tecnico.saslearning.domain.Response;
import pt.ulisboa.tecnico.saslearning.domain.ResponseMeasure;
import pt.ulisboa.tecnico.saslearning.domain.Scenario;
import pt.ulisboa.tecnico.saslearning.domain.ScenarioElement;
import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulus;
import pt.ulisboa.tecnico.saslearning.domain.Stimulus;
import pt.ulisboa.tecnico.saslearning.domain.Tactic;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

import com.google.gson.Gson;

@Controller
public class ScenarioController {

	@RequestMapping(value = "/templateEditor/{docId}/{annotationId}")
	public String templateEditor(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("scenarios", d.getScenarioSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		m.addAttribute("qualityAttributes", getQualityAttributes());
		m.addAttribute("tactics", Utils.getTactics());
		return "structuredRepresentation";
	}

	@RequestMapping(value = "/addNewScenario/{docId}/{annotationId}/{qualityAttribute}")
	public RedirectView addNewScenario(@PathVariable String docId,
			@PathVariable String annotationId,
			@PathVariable String qualityAttribute) {
		Document d = FenixFramework.getDomainObject(docId);
		addScenarioToDocument(d, qualityAttribute);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/"
				+ annotationId);
		return rv;
	}

	@RequestMapping(value="/addTactic/{docId}/{annotationId}/{scenarioId}/{tactic}")
	public RedirectView addTactic(@PathVariable String docId, 
			@PathVariable String annotationId, 
			@PathVariable String scenarioId, @PathVariable String tactic) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		addTacticToScenario(s,tactic);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void addTacticToScenario(Scenario s, String tactic) {
		Tactic t = new Tactic();
		t.setName(tactic);
		s.getQualityAttribute().addTactic(t);	
	}

	@RequestMapping(value = "/removeScenario/{docId}/{scenarioId}/{annotationId}")
	public RedirectView removeScenario(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String scenarioId) {
		Document d = FenixFramework.getDomainObject(docId);
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		removeScenarioFromDocument(d, s);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/"
				+ annotationId);
		return rv;
	}

	@RequestMapping(value = "/linkAnnotationToScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView linkAnnotationToScenario(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String scenarioId) {
		linkAnnotationToScenario(scenarioId, annotationId);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/"
				+ annotationId);
		return rv;
	}

	@RequestMapping(value = "/removeAnnotationFromScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView removeAnnotationFromScenario(
			@PathVariable String docId, @PathVariable String annotationId,
			@PathVariable String scenarioId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotation(s, a);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/"
				+ annotationId);
		return rv;
	}

	@RequestMapping(value = "/linkAnnotationToScenElement/{docId}/{annotationId}/{elemId}")
	public RedirectView linkAnnotationToScenarioElement(
			@PathVariable String docId, @PathVariable String annotationId,
			@PathVariable String elemId) {
		linkAnnotationToElement(elemId, annotationId);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/"
				+ annotationId);
		return rv;
	}

	@RequestMapping(value = "/removeAnnotationFromScenarioElement/{docId}/{annotationId}/{elementId}")
	public RedirectView removeAnnotationFromElement(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String elementId) {
		ScenarioElement e = FenixFramework.getDomainObject(elementId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotation(e, a);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/"
				+ annotationId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeAnnotation(Scenario s, Annotation a) {
		s.removeAnnotation(a);
		a.setScenario(null);
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeAnnotation(ScenarioElement e, Annotation a) {
		e.removeAnnotation(a);
		a.setScenarioElement(null);
	}

	private List<String> getQualityAttributes() {
		List<String> qatts = new ArrayList<String>();
		qatts.add("Availability");
		qatts.add("Interoperability");
		qatts.add("Modifiability");
		qatts.add("Performance");
		qatts.add("Security");
		qatts.add("Testability");
		qatts.add("Usability");
		return qatts;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeScenarioFromDocument(Document d, Scenario s) {
		d.removeScenario(s);
		s.delete();
	}

	@Atomic(mode = TxMode.WRITE)
	private void linkAnnotationToElement(String elemId, String annotationId) {
		System.out.println("linkning annotation to tactic");
		ScenarioElement elem = FenixFramework.getDomainObject(elemId);
		System.out.println(elem);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		elem.addAnnotation(a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void linkAnnotationToScenario(String scenarioId, String annotationId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.addAnnotation(a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void addScenarioToDocument(Document d, String qualityAttribute) {
		Scenario s = new Scenario();
		s.setName("Scenario");
		SrcOfStimulus src = new SrcOfStimulus();
		src.setName("Source Of Stimulus");
		Stimulus stim = new Stimulus();
		stim.setName("Stimulus");
		Artifact art = new Artifact();
		art.setName("Artifact");
		Environment env = new Environment();
		env.setName("Environment");
		Response resp = new Response();
		resp.setName("Response");
		ResponseMeasure rm = new ResponseMeasure();
		rm.setName("Response Measure");
		QualityAttribute qa = new QualityAttribute();
		qa.setName(qualityAttribute);
		s.setQualityAttribute(qa);
		s.setSrcOfStimulus(src);
		s.setStimulus(stim);
		s.setArtifact(art);
		s.setEnvironment(env);
		s.setResponse(resp);
		s.setResponseMeasure(rm);
		d.addScenario(s);
	}
}
