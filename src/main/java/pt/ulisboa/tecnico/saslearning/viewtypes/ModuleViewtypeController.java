package pt.ulisboa.tecnico.saslearning.viewtypes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.ModuleViewType;
import pt.ulisboa.tecnico.saslearning.domain.Scenario;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

@Controller
public class ModuleViewtypeController {

	@RequestMapping(value = "/addAnnotationToModuleVTStructure/{docId}/{annotationId}")
	public String addAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("modulevts", d.getModuleViewtypeSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "addAnnotationModuleModal";
	}

	@RequestMapping(value = "/linkToModuleViewtype/{docId}/{annotationId}/{viewtypeId}")
	public RedirectView addAnnotationToScenario(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewtypeId) {
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		addAnnotationToModuleViewType(mvt, a);
		RedirectView rv = new RedirectView("/viewModuleViewtype/" + docId + "/"
				+ viewtypeId + "#" + annotationId);
		return rv;
	}

	@RequestMapping(value = "/addNewModuleViewtype/{docId}/{annotationId}/{viewtypeName}")
	public RedirectView addNewModuleViewType(@PathVariable String docId,
			@PathVariable String annotationId,
			@PathVariable String scenarioName, @RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addModuleViewTypeToDocument(d, scenarioName);
		RedirectView rv = new RedirectView();
		if (move.equals("yes")) {
			rv.setUrl("/moveAnnotationMVT/" + docId + "/" + annotationId);
		} else {
			rv.setUrl("/addAnnotationToModuleVTStructure/" + docId + "/"
					+ annotationId);
		}
		return rv;
	}

	@RequestMapping("/viewModuleViewType/{docId}/{viewtypeId}")
	public String viewModuleViewtypeTemplate(Model m,
			@PathVariable String docId, @PathVariable String viewtypeId) {
		m.addAttribute("docId", docId);
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		m.addAttribute("viewtype", mvt);
		return "moduleViewtypeTemplate";
	}

	@RequestMapping(value = "/removeModuleViewtype/{docId}/{viewtypeId}")
	public RedirectView removeModuleViewtype(@PathVariable String docId,
			@PathVariable String viewtypeId) {
		Document d = FenixFramework.getDomainObject(docId);
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		removeModuleViewtypeFromDocument(d, mvt);
		RedirectView rv = new RedirectView("/selectDoc/" + docId);
		return rv;
	}

	@RequestMapping(value = "/unlinkFromModuleViewtype/{docId}/{annotationId}/{viewtypeId}")
	public RedirectView unlinkAnnotationFromScenario(
			@PathVariable String docId, @PathVariable String annotationId,
			@PathVariable String viewtypeId) {
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromModuleViewtype(mvt, a);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId);
		return rv;
	}

	@RequestMapping(value = "/moveAnnotationMVT/{docId}/{annotationId}")
	public String moveAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("viewtypes", d.getModuleViewtypeSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "moveAnnotationModuleVTModal";
	}

	@RequestMapping(value = "/moveToModuleViewtype/{docId}/{annotationId}/{viewtypeId}")
	public RedirectView moveAnnotation(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewtypeId) {
		ModuleViewType nvt = FenixFramework.getDomainObject(viewtypeId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToModuleViewtype(a, nvt);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId + "#" + annotationId);
		return rv;
	}

	@RequestMapping(value = "/setModuleViewtypeText/{docId}/{viewtypeId}", method = RequestMethod.POST)
	public RedirectView setScenarioText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String viewtypeId) {
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		updateText(mvt, text);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId + "#" + mvt.getName());
		return rv;
	}

	@RequestMapping(value = "/addNewModule/{docId}/{viewtypeId}/{name}")
	public RedirectView addModuleToViewtype(@PathVariable String docId,
			@PathVariable String viewtypeId, @PathVariable String name) {
		return null; //TO BE CONTINUED...
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateText(ModuleViewType mvt, String text) {
		mvt.setText(text);
	}

	@Atomic(mode = TxMode.WRITE)
	private void moveAnnotationToModuleViewtype(Annotation a, ModuleViewType nvt) {
		ModuleViewType old = a.getEnclosingModuleViewtype();
		removeAnnotationFromModuleViewtype(old, a);
		addAnnotationToModuleViewType(nvt, a);
	}

	private void removeAnnotationFromModuleViewtype(ModuleViewType mvt,
			Annotation a) {
		a.updateConnection(null);
		mvt.removeAnnotation(a);
		a.setModuleViewtype(null);
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeModuleViewtypeFromDocument(Document d, ModuleViewType mvt) {
		d.removeModuleViewtype(mvt);
		mvt.delete();

	}

	@Atomic(mode = TxMode.WRITE)
	private void addModuleViewTypeToDocument(Document d, String vtName) {
		ModuleViewType mvt = new ModuleViewType();
		mvt.setName("Module ViewType");
		mvt.setIdentifier(vtName);
		d.addModuleViewtype(mvt);
	}

	@Atomic(mode = TxMode.WRITE)
	private void addAnnotationToModuleViewType(ModuleViewType mvt, Annotation a) {
		mvt.addAnnotation(a);
		updateAnnotation(mvt.getExternalId(), a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateAnnotation(String connectedId, Annotation a) {
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		ann.setConnectedId(connectedId);
		String json = g.toJson(ann);
		a.setAnnotation(json);
	}
}
