package pt.ulisboa.tecnico.saslearning.viewtypes;

import java.util.Iterator;

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
import pt.ulisboa.tecnico.saslearning.domain.Module;
import pt.ulisboa.tecnico.saslearning.domain.ModuleViewType;
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
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId + "#" + annotationId);
		return rv;
	}

	@RequestMapping(value = "/addNewModuleViewtype/{docId}/{annotationId}/{viewtypeName}")
	public RedirectView addNewModuleViewType(@PathVariable String docId,
			@PathVariable String annotationId,
			@PathVariable String viewtypeName, @RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addModuleViewTypeToDocument(d, viewtypeName);
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
	public RedirectView unlinkAnnotationFromViewtype(
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
	public RedirectView setViewtypeText(@RequestParam String text,
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
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		addNewModule(mvt, name);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId);
		return rv;

	}

	@RequestMapping(value = "/linkToModule/{docId}/{viewtypeId}/{moduleId}/{annotationId}")
	public RedirectView linkAnnotationToModule(@PathVariable String docId,
			@PathVariable String viewtypeId, @PathVariable String moduleId,
			@PathVariable String annotationId) {
		Module m = FenixFramework.getDomainObject(moduleId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		addAnnotationToModule(m, a);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId+"#"+annotationId);
		return rv;
	}

	@RequestMapping(value = "/removeModule/{docId}/{viewtypeId}/{moduleId}")
	public RedirectView removeModule(@PathVariable String docId,
			@PathVariable String viewtypeId, @PathVariable String moduleId) {
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		Module m = FenixFramework.getDomainObject(moduleId);
		removeModule(mvt, m);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId);
		return rv;
	}

	@RequestMapping(value = "/unlinkFromModule/{docId}/{viewtypeId}/{moduleId}/{annotationId}")
	public RedirectView removeAnnotationFromModule(@PathVariable String docId,
			@PathVariable String viewtypeId, @PathVariable String moduleId,
			@PathVariable String annotationId) {
		ModuleViewType mvt = FenixFramework.getDomainObject(viewtypeId);
		Module m = FenixFramework.getDomainObject(moduleId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		unlinkFromModule(a, m, mvt);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId);
		return rv;
	}

	@RequestMapping(value = "/setModuleText/{docId}/{viewtypeId}/{moduleId}", method = RequestMethod.POST)
	public RedirectView setModuleText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String viewtypeId,
			@PathVariable String moduleId) {
		Module m = FenixFramework.getDomainObject(moduleId);
		setModuleText(m, text);
		RedirectView rv = new RedirectView("/viewModuleViewType/" + docId + "/"
				+ viewtypeId);
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void setModuleText(Module m, String text) {
		m.setText(text);
	}

	@Atomic(mode = TxMode.WRITE)
	private void unlinkFromModule(Annotation a, Module m, ModuleViewType mvt) {
		a.setModule(null);
		m.removeAnnotation(a);
		mvt.addAnnotation(a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeModule(ModuleViewType mvt, Module m) {
		Iterator<Annotation> it = m.getAnnotationSet().iterator();
		while (it.hasNext()) {
			Annotation a = it.next();
			a.setModule(null);
			m.removeAnnotation(a);
			mvt.addAnnotation(a);
		}
		m.setModuleViewtype(null);
		m.delete();
	}

	@Atomic(mode = TxMode.WRITE)
	private void addAnnotationToModule(Module m, Annotation a) {
		a.setModuleViewtype(null);
		m.addAnnotation(a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void addNewModule(ModuleViewType mvt, String name) {
		Module m = new Module();
		m.setName(name);
		mvt.addModule(m);
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

	@Atomic(mode=TxMode.WRITE)
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
