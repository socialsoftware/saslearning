package pt.ulisboa.tecnico.saslearning.scenarios;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Artifact;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.ElementFragment;
import pt.ulisboa.tecnico.saslearning.domain.Environment;
import pt.ulisboa.tecnico.saslearning.domain.Response;
import pt.ulisboa.tecnico.saslearning.domain.ResponseMeasure;
import pt.ulisboa.tecnico.saslearning.domain.Scenario;
import pt.ulisboa.tecnico.saslearning.domain.ScenarioElement;
import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulus;
import pt.ulisboa.tecnico.saslearning.domain.Stimulus;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

@Controller
public class ScenarioFragmentsController {
	Utils utils = new Utils();
	
	@RequestMapping(value="/getElementFragment")
	public String getElementFragment() {
		return "scenarioElementFragment";
	}
	
	@RequestMapping(value="/removeAnnotationFromScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView removeAnnotationFromScenario(@PathVariable String docId, @PathVariable String annotationId, @PathVariable String scenarioId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotation(s,a);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@RequestMapping(value="/removeAnnotationFromScenarioElement/{docId}/{annotationId}/{elementId}")
	public RedirectView removeAnnotationFromElement(@PathVariable String docId, @PathVariable String annotationId, @PathVariable String elementId) {
		ScenarioElement e = FenixFramework.getDomainObject(elementId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotation(e,a);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotation(Scenario s, Annotation a) {
		s.removeAnnotation(a);
		a.setScenario(null);
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotation(ScenarioElement e, Annotation a) {
		e.removeAnnotation(a);
		a.setScenarioElement(null);
	}

	@RequestMapping(value = "/templateEditor/{docId}/{annotationId}")
	public String templateEditor(Model m,
			@PathVariable String docId, @PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("scenarios", d.getScenarioSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		
		return "structuredRepresentation";
	}
	
	@RequestMapping(value="/addNewScenario/{docId}/{annotationId}")
	public RedirectView addNewScenario(@PathVariable String docId, @PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		addScenarioToDocument(d);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@RequestMapping(value="/removeScenario/{docId}/{scenarioId}/{annotationId}")
	public RedirectView removeScenario(@PathVariable String docId, @PathVariable String annotationId, @PathVariable String scenarioId) {
		Document d = FenixFramework.getDomainObject(docId);
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		removeScenarioFromDocument(d, s);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeScenarioFromDocument(Document d, Scenario s) {
		d.removeScenario(s);
		s.delete();
	}

	@RequestMapping(value="/linkAnnotationToScenario/{docId}/{annotationId}/{scenarioId}")
	public RedirectView linkAnnotationToScenario(@PathVariable String docId, @PathVariable String annotationId, @PathVariable String scenarioId) {
		linkAnnotationToScenario(scenarioId, annotationId);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@RequestMapping(value="/linkAnnotationToScenElement/{docId}/{annotationId}/{elemId}")
	public RedirectView linkAnnotationToScenarioElement(@PathVariable String docId, @PathVariable String annotationId, @PathVariable String elemId) {
		linkAnnotationToElement(elemId, annotationId);
		RedirectView rv = new RedirectView("/templateEditor/" + docId + "/" + annotationId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToElement(String elemId, String annotationId) {
		ScenarioElement elem = FenixFramework.getDomainObject(elemId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		elem.addAnnotation(a);
	}

	@Atomic(mode=TxMode.WRITE)
	private void linkAnnotationToScenario(String scenarioId, String annotationId) {
		Scenario s = FenixFramework.getDomainObject(scenarioId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		s.addAnnotation(a);
	}

	@Atomic(mode=TxMode.WRITE)
	private void addScenarioToDocument(Document d) {
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
		s.setSrcOfStimulus(src);
		s.setStimulus(stim);
		s.setArtifact(art);
		s.setEnvironment(env);
		s.setResponse(resp);
		s.setResponseMeasure(rm);
		d.addScenario(s);
	}

	@RequestMapping(value = "/fragmentManager/{docId}/{annotationId}")
	public String fragmentManager(Model m,
			@PathVariable String docId, @PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		ElementFragment e = a.getFragment();
		m.addAttribute("frags", d.getFragmentSet());
		m.addAttribute("current", e);
		m.addAttribute("docId", docId);
		return "fragmentManager";
	}

	@RequestMapping(value = "linkFragment/{docId}/{fragToLink}/{parentFrag}")
	public RedirectView linkAnnotationToFragment(@PathVariable String docId,
			@PathVariable String fragToLink, @PathVariable String parentFrag) {
		ElementFragment toLink = FenixFramework.getDomainObject(fragToLink);
		ElementFragment parent = FenixFramework.getDomainObject(parentFrag);
		linkFragments(toLink, parent);
		RedirectView rv = new RedirectView("/fragmentManager/" + docId+"/" + toLink.getAnnotation().getExternalId());
		return rv;
	}
	
	@RequestMapping(value = "unlinkFragment/{docId}/{fragToUnlink}/{chainHead}")
	public RedirectView unlinkAnnotationFromChain(@PathVariable String docId,
			@PathVariable String fragToUnlink, @PathVariable String chainHead) {
		ElementFragment toUnlink = FenixFramework.getDomainObject(fragToUnlink);
		ElementFragment head = FenixFramework.getDomainObject(chainHead);
		unlinkFragments(toUnlink, head);
		RedirectView rv = new RedirectView("/fragmentManager/" + docId+"/" + toUnlink.getAnnotation().getExternalId());
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void unlinkFragments(ElementFragment toUnlink, ElementFragment head) {
		if(toUnlink.getExternalId().equals(head.getExternalId())) { // remover a cabeça da lista
			ElementFragment child = toUnlink.getChild();
			if(toUnlink.hasConnections()) {
				toUnlink.passConnectionsToChild();
			}
			child.setParent(null);
			child.setDocument(toUnlink.getDocument());
			if(child.getChild() == null && !child.hasConnections()) {
				child.setLinked(false);
			}
			toUnlink.setChild(null);
			toUnlink.setLinked(false);
		}else { //remover do meio ou do fim da lista
			ElementFragment e = head;
			while(!e.getChild().getExternalId().equals(toUnlink.getExternalId())) {
				e = e.getChild();
			} //e = nó antes do que queremos remover
			ElementFragment child = toUnlink.getChild();
			if(child != null) {
				child.setParent(e);
			}
			e.setChild(child);
			toUnlink.setParent(null);
			toUnlink.setChild(null);
			toUnlink.setLinked(false);
			if(head.getChild() == null && !head.hasConnections()) { //se por acaso só estavam 2 fragmentos ligados e removemos 1
				head.setLinked(false);
			}
			
		}
	}

	@Atomic(mode=TxMode.WRITE)
	private void linkFragments(ElementFragment toLink, ElementFragment parent) {
		ElementFragment e = parent;
		while(e.getChild() != null) {
			e = e.getChild();
		}
		toLink.setDocument(null);
		e.setChild(toLink);
		e.setLinked(true);
		toLink.setLinked(true);
	}
}
