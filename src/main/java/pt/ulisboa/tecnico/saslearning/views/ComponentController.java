package pt.ulisboa.tecnico.saslearning.views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import pt.ulisboa.tecnico.saslearning.domain.Component;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.Port;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

@Controller
public class ComponentController {

	@RequestMapping(value = "/addAnnotationToComponentTemplate/{docId}/{annotationId}")
	public String addAnnotationModal(@PathVariable String docId,
			@PathVariable String annotationId, Model m) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		Set<Component> components = d.getComponentSet();
		m.addAttribute("components", new HashSet<Component>(components));
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "addAnnotationComponentModal";
	}

	@RequestMapping(value = "/linkToComponent/{docId}/{annotationId}/{componentId}")
	public RedirectView addAnnotationToComponent(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String componentId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		addAnnotationToComponent(comp, a);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + annotationId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addAnnotationToComponent(Component comp, Annotation a) {
		comp.addAnnotation(a);
		a.updateConnection(comp.getExternalId());
	}

	@RequestMapping(value = "/addNewComponent/{docId}/{annotationId}/{componentName}")
	public RedirectView addNewComponent(@PathVariable String docId,
			@PathVariable String annotationId,
			@PathVariable String componentName, @RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addComponentToDocument(d, componentName);
		RedirectView rv = new RedirectView();
		if (move.equals("yes")) {
			rv.setUrl("/moveAnnotationComponent/" + docId + "/" + annotationId);
		} else {
			rv.setUrl("/addAnnotationToComponentTemplate/" + docId + "/"
					+ annotationId);
		}
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addComponentToDocument(Document d, String componentName) {
		Component c = new Component();
		c.setName(componentName);
		d.addComponent(c);
	}

	@RequestMapping(value = "/viewComponent/{docId}/{componentId}")
	public String viewComponentTemplate(Model m, @PathVariable String docId,
			@PathVariable String componentId) {
		m.addAttribute("docId", docId);
		Component comp = FenixFramework.getDomainObject(componentId);
		Document d = FenixFramework.getDomainObject(docId);
		m.addAttribute("component", comp);
		m.addAttribute("components", d.getComponentSet());
		return "componentTemplate";
	}

	@RequestMapping(value = "/removeComponent/{docId}/{componentId}")
	public RedirectView removeComponent(@PathVariable String docId,
			@PathVariable String componentId) {
		Document d = FenixFramework.getDomainObject(docId);
		Component comp = FenixFramework.getDomainObject(componentId);
		removeComponentFromDocument(d, comp);
		RedirectView rv = new RedirectView("/selectDoc/" + docId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeComponentFromDocument(Document d, Component comp) {
		d.removeComponent(comp);
		comp.delete();
	}

	@RequestMapping(value = "/unlinkFromComponent/{docId}/{annotationId}/{componentId}")
	public RedirectView unlinkAnnotationFromComponent(
			@PathVariable String docId, @PathVariable String annotationId,
			@PathVariable String componentId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromComponent(comp, a);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId);
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromComponent(Component comp, Annotation a) {
		a.updateConnection(null);
		comp.removeAnnotation(a);
		a.setComponent(null);
	}

	@RequestMapping(value = "/moveAnnotationComponent/{docId}/{annotationId}")
	public String moveAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("components", d.getComponentSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "moveAnnotationComponentModal";
	}

	@RequestMapping(value = "/moveToComponent/{docId}/{annotationId}/{componentId}")
	public RedirectView moveAnnotation(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String componentId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToComponent(a, comp);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + annotationId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void moveAnnotationToComponent(Annotation a, Component comp) {
		Component old = a.getComponent();
		removeAnnotationFromComponent(old, a);
		addAnnotationToComponent(comp, a);
	}

	@RequestMapping(value = "/setComponentText/{docId}/{componentId}", method = RequestMethod.POST)
	public RedirectView setComponentText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String componentId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		updateText(comp, text);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + comp.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateText(Component comp, String text) {
		comp.setText(text);
	}

	@RequestMapping(value = "/addPortToComponent/{docId}/{componentId}/{portName}")
	public RedirectView addPortToComponent(@PathVariable String docId,
			@PathVariable String componentId, @PathVariable String portName) {
		Component comp = FenixFramework.getDomainObject(componentId);
		addPortToComponent(comp, portName);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + comp.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addPortToComponent(Component comp, String portName) {
		Port p = new Port();
		p.setName(portName);
		comp.addPort(p);
	}

	@RequestMapping(value = "/removePortFromComponent/{docId}/{componentId}/{portId}")
	public RedirectView removePortFromComponent(@PathVariable String docId,
			@PathVariable String componentId, @PathVariable String portId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		Port p = FenixFramework.getDomainObject(portId);
		removePortFromComponent(comp, p);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + comp.getName());
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void removePortFromComponent(Component comp, Port p) {
		Iterator<Annotation> anns = p.getAnnotationSet().iterator();
		while(anns.hasNext()) {
			Annotation a = anns.next();
			p.removeAnnotation(a);
			comp.addAnnotation(a);
		}
		p.setComponent(null);
		p.delete();
	}
	
	@RequestMapping(value = "/moveAnnotationToPort/{docId}/{componentId}/{portId}/{annotationId}")
	public RedirectView moveAnnotationToPort(@PathVariable String docId,
			@PathVariable String componentId, @PathVariable String portId,@PathVariable String annotationId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		Port p = FenixFramework.getDomainObject(portId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToPort(comp,p,a);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + comp.getName());
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void moveAnnotationToPort(Component comp, Port p, Annotation a) {
		comp.removeAnnotation(a);
		p.addAnnotation(a);
		
	}
	
	@RequestMapping(value = "/removeAnnotationFromPort/{docId}/{componentId}/{portId}/{annotationId}")
	public RedirectView removeAnnotationFromPort(@PathVariable String docId,
			@PathVariable String componentId, @PathVariable String portId,@PathVariable String annotationId) {
		Component comp = FenixFramework.getDomainObject(componentId);
		Port p = FenixFramework.getDomainObject(portId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromPort(comp,p,a);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ componentId + "#" + comp.getName());
		return rv;
	}

	@Atomic(mode=TxMode.WRITE)
	private void removeAnnotationFromPort(Component comp, Port p, Annotation a) {
		p.removeAnnotation(a);
		comp.addAnnotation(a);
	}
	
	@RequestMapping(value = "/setPortText/{docId}/{portId}", method = RequestMethod.POST)
	public RedirectView setPortText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String portId) {
		Port p = FenixFramework.getDomainObject(portId);
		updateText(p, text);
		RedirectView rv = new RedirectView("/viewComponent/" + docId + "/"
				+ p.getComponent().getExternalId() + "#" + p.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateText(Port p, String text) {
		p.setText(text);
	}

}
