package pt.ulisboa.tecnico.saslearning.scenarios;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
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
import pt.ulisboa.tecnico.saslearning.domain.Tactic;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

import com.google.gson.Gson;

@Controller
public class ScenarioController {

	@RequestMapping(value = "/addAnnotationToStructure/{docId}/{annotationId}")
	public String addAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("scenarios", d.getScenarioSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "addAnnotationModal";
	}

	@RequestMapping(value = "/moveAnnotation/{docId}/{annotationId}")
	public String moveAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("scenarios", d.getScenarioSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "moveAnnotationModal";
	}

	@RequestMapping(value = "/addNewScenario/{docId}/{annotationId}/{scenarioName}")
	public RedirectView addNewScenario(@PathVariable String docId,
			@PathVariable String annotationId,
			@PathVariable String scenarioName, @RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addScenarioToDocument(d, scenarioName);
		RedirectView rv = new RedirectView();
		if (move.equals("yes")) {
			rv.setUrl("/moveAnnotation/" + docId + "/" + annotationId);
		} else {
			rv.setUrl("/addAnnotationToStructure/" + docId + "/" + annotationId);
		}
		return rv;
	}

	@RequestMapping(value = "/removeScenario/{docId}/{scenarioId}")
	public RedirectView removeScenario(@PathVariable String docId,
			@PathVariable String scenarioId) {
		Document d = FenixFramework.getDomainObject(docId);
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		removeScenarioFromDocument(d, s);
		RedirectView rv = new RedirectView("/selectDoc/" + docId);
		return rv;
	}

	@RequestMapping(value = "/linkToScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView addAnnotationToScenario(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String scenarioId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		addAnnotationToScenario(s, a);
		RedirectView rv = new RedirectView("/viewScenario/" + docId + "/"
				+ scenarioId + "#" + annotationId);
		return rv;
	}

	@RequestMapping(value = "/moveToScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView moveAnnotation(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String scenarioId) {
		Scenario ns = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToScenario(a, ns);
		RedirectView rv = new RedirectView("/viewScenario/" + docId + "/"
				+ scenarioId + "#" + annotationId);
		return rv;
	}

	@RequestMapping("/viewScenario/{docId}/{scenarioId}")
	public String viewScenarioTemplate(Model m, @PathVariable String docId,
			@PathVariable String scenarioId) {
		m.addAttribute("docId", docId);
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		m.addAttribute("scenario", s);
		m.addAttribute("tactics", Utils.getTactics());
		return "scenarioTemplate";
	}

	@RequestMapping(value = "/unlinkFromScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView unlinkAnnotationFromScenario(
			@PathVariable String docId, @PathVariable String annotationId,
			@PathVariable String scenarioId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromScenario(s, a);
		RedirectView rv = new RedirectView("/viewScenario/" + docId + "/"
				+ scenarioId);
		return rv;
	}

	@RequestMapping(value = "/addTactic/{docId}/{scenarioId}/{tactic}")
	public RedirectView addTacticToScenario(@PathVariable String docId,
			@PathVariable String scenarioId, @PathVariable String tactic) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		addTactic(s, tactic);
		RedirectView rv = new RedirectView("/viewScenario/" + docId + "/"
				+ scenarioId);
		return rv;
	}

	@RequestMapping(value = "/linkToTactic/{docId}/{scenarioId}/{tacticId}/{annotationId}")
	public RedirectView addAnnotationToTactic(@PathVariable String docId,
			@PathVariable String scenarioId, @PathVariable String tacticId, @PathVariable String annotationId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Tactic t = FenixFramework.getDomainObject(tacticId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		linkAnnotationToTactic(s,t,a);
		RedirectView rv = new RedirectView("/viewScenario/" + docId + "/"
				+ scenarioId+"#"+annotationId);
		return rv;
	}
	
	@RequestMapping(value = "/unlinkFromTactic/{docId}/{scenarioId}/{tacticId}/{annotationId}")
	public RedirectView unlinkFromTactic(@PathVariable String docId,
			@PathVariable String scenarioId, @PathVariable String tacticId, @PathVariable String annotationId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Tactic t = FenixFramework.getDomainObject(tacticId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		unlinkAnnotationFromTactic(s,t,a);
		RedirectView rv = new RedirectView("/viewScenario/" + docId + "/"
				+ scenarioId+"#"+annotationId);
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void unlinkAnnotationFromTactic(Scenario s, Tactic t, Annotation a) {
		t.removeAnnotation(a);
		s.addAnnotation(a);
	}

	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToTactic(Scenario s, Tactic t, Annotation a) {
		s.removeAnnotation(a);
		t.addAnnotation(a);
	}
	
	

	@Atomic(mode = TxMode.WRITE)
	private void addTactic(Scenario s, String tactic) {
		Tactic t = new Tactic();
		t.setName(tactic);
		s.addTactic(t);
	}

	@Atomic(mode = TxMode.WRITE)
	private void addAnnotationToScenario(Scenario s, Annotation a) {
		String tag = a.getTag();

		if (tag.equals("Scenario Description") || tag.equals("Tactic")) {
			updateAnnotation(s.getExternalId(), a);
			s.addAnnotation(a);

		}
		if (s.getElements().get(tag) != null) {
			ScenarioElement em = s.getElements().get(tag);
			updateAnnotation(s.getExternalId(), a);
			em.addAnnotation(a);
		}

	}

	private void updateAnnotation(String connectedId, Annotation a) {
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		ann.setConnectedId(connectedId);
		String json = g.toJson(ann);
		a.setAnnotation(json);
	}

	@Atomic(mode = TxMode.WRITE)
	private void moveAnnotationToScenario(Annotation a, Scenario ns) {
		Scenario old = a.getEnclosingScenario();
		removeAnnotationFromScenario(old, a);
		addAnnotationToScenario(ns, a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeAnnotationFromScenario(Scenario s, Annotation a) {
		String tag = a.getTag();
		if (tag.equals("Scenario Description")) {
			s.removeAnnotation(a);
			a.setScenario(null);
			updateAnnotation(null, a);
		}
		else if(tag.equals("Tactic")) {
			if(a.getScenarioElement() != null) {
				ScenarioElement elem = a.getScenarioElement();
				elem.removeAnnotation(a);
				a.setScenarioElement(null);
				updateAnnotation(null, a);
			}else {
				s.removeAnnotation(a);
				a.setScenario(null);
				updateAnnotation(null, a);
			}
		}
		else if (s.getElements().get(tag) != null) {
			s.getElements().get(tag).removeAnnotation(a);
			a.setScenarioElement(null);
			updateAnnotation(null, a);
		}
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeScenarioFromDocument(Document d, Scenario s) {
		d.removeScenario(s);
		s.delete();
	}

	@Atomic(mode = TxMode.WRITE)
	private void addScenarioToDocument(Document d, String scenarioName) {
		Scenario s = new Scenario();
		s.setName("Scenario");
		s.setIdentifier(scenarioName);
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
		s.setSrcOfStimulus(src);
		s.setStimulus(stim);
		s.setArtifact(art);
		s.setEnvironment(env);
		s.setResponse(resp);
		s.setResponseMeasure(rm);
		d.addScenario(s);
	}
}
