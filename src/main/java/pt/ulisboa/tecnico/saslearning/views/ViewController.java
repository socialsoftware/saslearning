package pt.ulisboa.tecnico.saslearning.views;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Component;
import pt.ulisboa.tecnico.saslearning.domain.Connector;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.Module;
import pt.ulisboa.tecnico.saslearning.domain.View;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

@Controller
public class ViewController {

	@RequestMapping(value = "/viewFragments")
	public String viewFragments() {
		return "viewFragments";
	}
	

	@RequestMapping(value = "/addAnnotationToViewTemplate/{docId}/{annotationId}")
	public String addAnnotationModal(@PathVariable String docId,
			@PathVariable String annotationId, Model m) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		Set<View> views = d.getViewSet();
		m.addAttribute("views", new HashSet<View>(views));
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "addAnnotationViewModal";
	}

	@RequestMapping(value = "/linkToView/{docId}/{annotationId}/{viewId}")
	public RedirectView addAnnotationToView(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewId) {
		View view = FenixFramework.getDomainObject(viewId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		addAnnotationToView(view, a);
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId
				+ "#" + annotationId);
		return rv;
	}

	@RequestMapping(value = "/addNewView/{docId}/{annotationId}/Module Viewtype/{viewName}")
	public RedirectView addNewModuleViewTypeView(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewName,
			@RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addModuleViewtypeToDocument(d, viewName, "Module Viewtype");
		RedirectView rv = new RedirectView();
		if (move.equals("yes")) {
			rv.setUrl("/moveAnnotationView/" + docId + "/" + annotationId);
		} else {
			rv.setUrl("/addAnnotationToViewTemplate/" + docId + "/"
					+ annotationId);
		}
		return rv;
	}
	
	@RequestMapping(value = "/addNewView/{docId}/{annotationId}/Component & Connector Viewtype/{viewName}")
	public RedirectView addNewCCViewTypeView(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewName,
			@RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addCCViewtypeToDocument(d, viewName, "Component & Connector Viewtype");
		RedirectView rv = new RedirectView();
		if (move.equals("yes")) {
			rv.setUrl("/moveAnnotationView/" + docId + "/" + annotationId);
		} else {
			rv.setUrl("/addAnnotationToViewTemplate/" + docId + "/"
					+ annotationId);
		}
		return rv;
	}

	@RequestMapping(value = "/viewView/{docId}/{viewId}")
	public String viewViewTemplate(Model m, @PathVariable String docId,
			@PathVariable String viewId) {
		m.addAttribute("docId", docId);
		View v = FenixFramework.getDomainObject(viewId);
		Document d = FenixFramework.getDomainObject(docId);
		if(v.getViewtype().equals("Module Viewtype")) {
			m.addAttribute("view", v);
			m.addAttribute("views", d.getViewSet());
			m.addAttribute("modules", d.getModuleSet());
			m.addAttribute("used", new UsedIds());
			return "viewMVTTemplate";
		}else if(v.getViewtype().equals("Component & Connector Viewtype")) {
			m.addAttribute("view", v);
			m.addAttribute("views", d.getViewSet());
			m.addAttribute("components", d.getComponentSet());
			m.addAttribute("connectors", d.getConnectorSet());
			m.addAttribute("used", new UsedIds());
			
			return "viewCCTemplate";
		}
		return null;
	}

	@RequestMapping(value = "/removeView/{docId}/{viewId}")
	public RedirectView removeView(@PathVariable String docId,
			@PathVariable String viewId) {
		Document d = FenixFramework.getDomainObject(docId);
		View mod = FenixFramework.getDomainObject(viewId);
		removeViewFromDocument(d, mod);
		RedirectView rv = new RedirectView("/selectDoc/" + docId);
		return rv;
	}

	@RequestMapping(value = "/unlinkFromView/{docId}/{annotationId}/{viewId}")
	public RedirectView unlinkAnnotationFromView(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewId) {
		View mod = FenixFramework.getDomainObject(viewId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromView(mod, a);
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}

	@RequestMapping(value = "/moveAnnotationView/{docId}/{annotationId}")
	public String moveAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("views", d.getViewSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "moveAnnotationViewModal";
	}

	@RequestMapping(value = "/moveToView/{docId}/{annotationId}/{viewId}")
	public RedirectView moveAnnotation(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String viewId) {
		View nv = FenixFramework.getDomainObject(viewId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToView(a, nv);
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId
				+ "#" + annotationId);
		return rv;
	}

	@RequestMapping(value = "/setViewText/{docId}/{viewId}", method = RequestMethod.POST)
	public RedirectView setViewText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String viewId) {
		View v = FenixFramework.getDomainObject(viewId);
		updateText(v, text);
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}

	@RequestMapping(value = "/addModulesToView/{docId}/{viewId}", method = RequestMethod.POST)
	public RedirectView addModulesToView(@PathVariable String docId,
			@PathVariable String viewId, @ModelAttribute UsedIds modules) {
		View v = FenixFramework.getDomainObject(viewId);
		if(v.getViewtype().equals("Module Viewtype")) {
			addModulesToView(v, modules);
		}
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}
	
	@RequestMapping(value = "/addComponentsToView/{docId}/{viewId}", method = RequestMethod.POST)
	public RedirectView addComponentsToView(@PathVariable String docId,
			@PathVariable String viewId, @ModelAttribute UsedIds components) {
		View v = FenixFramework.getDomainObject(viewId);
		if(v.getViewtype().equals("Component & Connector Viewtype")) {
			addComponentsToView(v, components);
		}
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}
	
	@RequestMapping(value = "/addConnectorsToView/{docId}/{viewId}", method = RequestMethod.POST)
	public RedirectView addConnectorsToView(@PathVariable String docId,
			@PathVariable String viewId, @ModelAttribute UsedIds connectors) {
		View v = FenixFramework.getDomainObject(viewId);
		if(v.getViewtype().equals("Component & Connector Viewtype")) {
			addConnectorsToView(v, connectors);
		}
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}

	@RequestMapping(value = "/removeModuleFromView/{docId}/{viewId}/{moduleId}")
	public RedirectView removeModule(@PathVariable String docId,
			@PathVariable String viewId, @PathVariable String moduleId) {
		View v = FenixFramework.getDomainObject(viewId);
		Module m = FenixFramework.getDomainObject(moduleId);
		if(v.getViewtype().equals("Module Viewtype")) {
			removeModuleFromView(v,m);
		}
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}
	
	@RequestMapping(value = "/removeComponentFromView/{docId}/{viewId}/{componentId}")
	public RedirectView removeComponent(@PathVariable String docId,
			@PathVariable String viewId, @PathVariable String componentId) {
		View v = FenixFramework.getDomainObject(viewId);
		Component comp = FenixFramework.getDomainObject(componentId);
		if(v.getViewtype().equals("Module Viewtype")) {
			removeComponentFromView(v,comp);
		}
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}
	
	@RequestMapping(value = "/removeConnectorFromView/{docId}/{viewId}/{connectorId}")
	public RedirectView removeConnector(@PathVariable String docId,
			@PathVariable String viewId, @PathVariable String connectorId) {
		View v = FenixFramework.getDomainObject(viewId);
		Connector conn = FenixFramework.getDomainObject(connectorId);
		if(v.getViewtype().equals("Module Viewtype")) {
			removeConnectorFromView(v,conn);
		}
		RedirectView rv = new RedirectView("/viewView/" + docId + "/" + viewId);
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void removeModuleFromView(View v, Module m) {
		v.removeModule(m);
		m.removeView(v);
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeComponentFromView(View v, Component m) {
		v.removeComponent(m);
		m.removeView(v);
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeConnectorFromView(View v, Connector m) {
		v.removeConnector(m);
		m.removeView(v);
	}

	@Atomic(mode = TxMode.WRITE)
	private void addModulesToView(View v, UsedIds modules) {
		for (String id : modules.getUsed()) {
			Module m = FenixFramework.getDomainObject(id);
			v.addModule(m);
		}
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void addComponentsToView(View v, UsedIds components) {
		for (String id : components.getUsed()) {
			Component component = FenixFramework.getDomainObject(id);
			v.addComponent(component);
		}
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void addConnectorsToView(View v, UsedIds connectors) {
		for (String id : connectors.getUsed()) {
			Connector conn = FenixFramework.getDomainObject(id);
			v.addConnector(conn);
		}
	}

	@Atomic(mode = TxMode.WRITE)
	private void addAnnotationToView(View view, Annotation a) {
		view.addAnnotation(a);
		a.updateConnection(view.getExternalId());
	}

	@Atomic(mode = TxMode.WRITE)
	private void addModuleViewtypeToDocument(Document d, String viewName,
			String viewtype) {
		View v = new View();
		v.setName(viewName);
		v.setViewtype(viewtype);
		d.addView(v);
	}
	
	@Atomic(mode = TxMode.WRITE)
	private void addCCViewtypeToDocument(Document d, String viewName,
			String viewtype) {
		View v = new View();
		v.setName(viewName);
		v.setViewtype(viewtype);
		d.addView(v);
	}

	@Atomic(mode = TxMode.WRITE)
	private void setViewText(View v, String text) {
		v.setText(text);
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateText(View v, String text) {
		v.setText(text);
	}

	@Atomic(mode = TxMode.WRITE)
	private void moveAnnotationToView(Annotation a, View v) {
		View old = a.getView();
		removeAnnotationFromView(old, a);
		addAnnotationToView(v, a);
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeAnnotationFromView(View v, Annotation a) {
		a.updateConnection(null);
		v.removeAnnotation(a);
		a.setView(null);
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeViewFromDocument(Document d, View view) {
		d.removeView(view);
		view.delete();
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
