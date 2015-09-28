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
import pt.ulisboa.tecnico.saslearning.domain.Connector;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.Role;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

import com.google.gson.Gson;

@Controller
public class ConnectorController {

	@RequestMapping(value = "/addAnnotationToConnectorTemplate/{docId}/{annotationId}")
	public String addAnnotationModal(@PathVariable String docId,
			@PathVariable String annotationId, Model m) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		Set<Connector> connectors = d.getConnectorSet();
		m.addAttribute("connectors", new HashSet<Connector>(connectors));
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "addAnnotationConnectorModal";
	}

	@RequestMapping(value = "/linkToConnector/{docId}/{annotationId}/{connectorId}")
	public RedirectView addAnnotationToConnector(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String connectorId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		addAnnotationToConnector(conn, a);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + annotationId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addAnnotationToConnector(Connector conn, Annotation a) {
		conn.addAnnotation(a);
		a.updateConnection(conn.getExternalId());
	}

	@RequestMapping(value = "/addNewConnector/{docId}/{annotationId}/{connectorName}")
	public RedirectView addNewConnector(@PathVariable String docId,
			@PathVariable String annotationId,
			@PathVariable String connectorName, @RequestParam String move) {
		Document d = FenixFramework.getDomainObject(docId);
		addConnectorToDocument(d, connectorName);
		RedirectView rv = new RedirectView();
		if (move.equals("yes")) {
			rv.setUrl("/moveAnnotationConnector/" + docId + "/" + annotationId);
		} else {
			rv.setUrl("/addAnnotationToConnectorTemplate/" + docId + "/"
					+ annotationId);
		}
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addConnectorToDocument(Document d, String connectorName) {
		Connector c = new Connector();
		c.setName(connectorName);
		d.addConnector(c);
	}

	@RequestMapping(value = "/viewConnector/{docId}/{connectorId}")
	public String viewConnectorTemplate(Model m, @PathVariable String docId,
			@PathVariable String connectorId) {
		m.addAttribute("docId", docId);
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Document d = FenixFramework.getDomainObject(docId);
		m.addAttribute("connector", conn);
		m.addAttribute("connectors", d.getConnectorSet());
		return "connectorTemplate";
	}

	@RequestMapping(value = "/removeConnector/{docId}/{connectorId}")
	public RedirectView removeConnector(@PathVariable String docId,
			@PathVariable String connectorId) {
		Document d = FenixFramework.getDomainObject(docId);
		Connector conn = FenixFramework.getDomainObject(connectorId);
		removeConnectorFromDocument(d, conn);
		RedirectView rv = new RedirectView("/selectDoc/" + docId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeConnectorFromDocument(Document d, Connector conn) {
		d.removeConnector(conn);
		conn.delete();
	}

	@RequestMapping(value = "/unlinkFromConnector/{docId}/{annotationId}/{connectorId}")
	public RedirectView unlinkAnnotationFromConnector(
			@PathVariable String docId, @PathVariable String annotationId,
			@PathVariable String connectorId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromConnector(conn, a);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeAnnotationFromConnector(Connector conn, Annotation a) {
		a.updateConnection(null);
		conn.removeAnnotation(a);
		a.setConnector(null);
	}

	@RequestMapping(value = "/moveAnnotationConnector/{docId}/{annotationId}")
	public String moveAnnotationModal(Model m, @PathVariable String docId,
			@PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		Gson g = new Gson();
		AnnotationJ ann = g.fromJson(a.getAnnotation(), AnnotationJ.class);
		m.addAttribute("connectors", d.getConnectorSet());
		m.addAttribute("annotation", ann);
		m.addAttribute("annotData", a);
		m.addAttribute("docId", docId);
		return "moveAnnotationConnectorModal";
	}

	@RequestMapping(value = "/moveToConnector/{docId}/{annotationId}/{connectorId}")
	public RedirectView moveAnnotation(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String connectorId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToConnector(a, conn);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + annotationId);
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void moveAnnotationToConnector(Annotation a, Connector conn) {
		Connector old = a.getConnector();
		removeAnnotationFromConnector(old, a);
		addAnnotationToConnector(conn, a);
	}

	@RequestMapping(value = "/setConnectorText/{docId}/{connectorId}", method = RequestMethod.POST)
	public RedirectView setConnectorText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String connectorId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		updateText(conn, text);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + conn.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateText(Connector conn, String text) {
		conn.setText(text);
	}

	@RequestMapping(value = "/addRoleToConnector/{docId}/{connectorId}/{roleName}")
	public RedirectView addRoleToConnector(@PathVariable String docId,
			@PathVariable String connectorId, @PathVariable String roleName) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		addRoleToConnector(conn, roleName);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + conn.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addRoleToConnector(Connector conn, String roleName) {
		Role r = new Role();
		r.setName(roleName);
		conn.addRole(r);
	}

	@RequestMapping(value = "/removeRoleFromConnector/{docId}/{connectorId}/{roleId}")
	public RedirectView removeRoleFromConnector(@PathVariable String docId,
			@PathVariable String connectorId, @PathVariable String roleId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Role r = FenixFramework.getDomainObject(roleId);
		removeRoleFromConnector(conn, r);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + conn.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeRoleFromConnector(Connector conn, Role r) {
		Iterator<Annotation> anns = r.getAnnotationSet().iterator();
		while (anns.hasNext()) {
			Annotation a = anns.next();
			r.removeAnnotation(a);
			conn.addAnnotation(a);
		}
		r.setConnector(null);
		r.delete();
	}

	@RequestMapping(value = "/moveAnnotationToRole/{docId}/{connectorId}/{roleId}/{annotationId}")
	public RedirectView moveAnnotationToRole(@PathVariable String docId,
			@PathVariable String connectorId, @PathVariable String roleId,
			@PathVariable String annotationId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Role r = FenixFramework.getDomainObject(roleId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		moveAnnotationToRole(conn, r, a);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + conn.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void moveAnnotationToRole(Connector conn, Role r, Annotation a) {
		conn.removeAnnotation(a);
		r.addAnnotation(a);

	}

	@RequestMapping(value = "/removeAnnotationFromRole/{docId}/{connectorId}/{roleId}/{annotationId}")
	public RedirectView removeAnnotationFromRole(@PathVariable String docId,
			@PathVariable String connectorId, @PathVariable String roleId,
			@PathVariable String annotationId) {
		Connector conn = FenixFramework.getDomainObject(connectorId);
		Role r = FenixFramework.getDomainObject(roleId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		removeAnnotationFromPort(conn, r, a);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ connectorId + "#" + conn.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeAnnotationFromPort(Connector conn, Role r, Annotation a) {
		r.removeAnnotation(a);
		conn.addAnnotation(a);
	}

	@RequestMapping(value = "/setRoleText/{docId}/{roleId}", method = RequestMethod.POST)
	public RedirectView setPortText(@RequestParam String text,
			@PathVariable String docId, @PathVariable String roleId) {
		Role r = FenixFramework.getDomainObject(roleId);
		updateText(r, text);
		RedirectView rv = new RedirectView("/viewConnector/" + docId + "/"
				+ r.getConnector().getExternalId() + "#" + r.getName());
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateText(Role r, String text) {
		r.setText(text);
	}

}
