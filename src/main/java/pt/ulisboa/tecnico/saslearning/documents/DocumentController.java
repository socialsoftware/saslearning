package pt.ulisboa.tecnico.saslearning.documents;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

@Controller
public class DocumentController {

	@RequestMapping(value = "/headerFrame")
	public String headerFrame() {
		return "headerFrame";
	}

	@RequestMapping(value = "/manageDocs")
	public String manageDocuments(Model m) {
		m.addAttribute("docs", FenixFramework.getDomainRoot().getDocumentSet());
		m.addAttribute("newDoc", new DocUrl());
		return "manageDocs";
	}

	@RequestMapping(value = "/removeDoc/{id}", method = RequestMethod.GET)
	public RedirectView removeDocument(@PathVariable String id) {
		removeDocumentById(id);
		RedirectView rv = new RedirectView("/manageDocs");
		return rv;
	}

	@RequestMapping(value = "/addDoc", method = RequestMethod.POST)
	public RedirectView addDocument(@ModelAttribute DocUrl doc)
			throws IOException {
		addNewDocument(doc.getUrl());
		RedirectView rv = new RedirectView("/manageDocs");
		return rv;
	}

	@RequestMapping(value = "seeDocs", method = RequestMethod.GET)
	public String listDocumentsAvailable(Model m) {
		m.addAttribute("docs", FenixFramework.getDomainRoot().getDocumentSet());
		m.addAttribute("docSelected", new DocUrl());
		return "seeDocuments";
	}

	@RequestMapping(value = "/selectDoc/{id}", method = RequestMethod.GET)
	public String showDocument(@PathVariable String id, Model m)
			throws IOException {
		m.addAttribute("docId", id);
		Document d = FenixFramework.getDomainObject(id);
		m.addAttribute("doc", d);
		return "docTemplate";
	}

	@RequestMapping("/viewStructuredRepresentation/{docId}")
	public String viewStructuredRepresentation(@PathVariable String docId,
			Model m) {
		Document d = FenixFramework.getDomainObject(docId);
		m.addAttribute("scenarios", d.getScenarioSet());
		m.addAttribute("docId", docId);
		m.addAttribute("title", d.getTitle());
		return "structuredRepresentation";
	}

	@RequestMapping("/viewTemplate/{docId}/{connectedId}/{annotationId}")
	public RedirectView viewTemplate(@PathVariable String docId,
			@PathVariable String connectedId, @PathVariable String annotationId) {
		Annotation a = FenixFramework.getDomainObject(annotationId);
		RedirectView rv = new RedirectView();
		if(a.isScenarioAnnotation()) {
			rv.setUrl("/viewScenario/"+docId+"/"+connectedId+"#"+annotationId);
		}else if( a.getTag().equals("Module")) {
			rv.setUrl("/viewModule/"+docId+"/"+connectedId+"#"+annotationId);
		}
		return rv;
	}

	@RequestMapping(value = "/addAnnotationToStructure/{docId}/{annotationId}/{tag}")
	public RedirectView addAnnotationModal(@PathVariable String docId,
			@PathVariable String annotationId, @PathVariable String tag) {
		RedirectView rv = null;
		if (Utils.allScenarioConcepts().contains(tag)) {
			System.out.println("Is scenario tag");
			rv= new RedirectView("/addAnnotationToScenarioStructure/" + docId + "/"
					+ annotationId);
		} else if (tag.equals("Module")) {
			System.out.println("Is module tag");
			rv = new RedirectView("/addAnnotationToModuleTemplate/" + docId + "/"
					+ annotationId);
		}
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeDocumentById(String id) {
		Document d = FenixFramework.getDomainObject(id);
		d.delete();
	}

	private void checkForAttributePath(Element e, String attr) {
		if (e.hasAttr(attr)) {
			if (e.attr(attr).charAt(0) != '#') {
				String path = e.absUrl(attr);
				e.removeAttr(attr);
				e.attr(attr, path);
			}
		}
	}

	@Atomic(mode = TxMode.WRITE)
	private void addNewDocument(String url) throws IOException {
		if (!documentExists(url)) {
			Document doc = new Document();
			org.jsoup.nodes.Document document = Jsoup.connect(url).get();
			for (Element e : document.getAllElements()) {
				checkForAttributePath(e, "src");
				checkForAttributePath(e, "href");
				checkForAttributePath(e, "data");
			}
			doc.setUrl(url);
			Elements titleSet = document.getElementsByTag("title");
			String title = "";
			if (!titleSet.isEmpty()) {
				title = titleSet.text();
			} else {
				title = url;
			}
			doc.setTitle(title);
			doc.setContent(document.children().toString());
			FenixFramework.getDomainRoot().addDocument(doc);
		}
	}

	@Atomic
	private boolean documentExists(String url) {
		DomainRoot d = FenixFramework.getDomainRoot();
		for (Document doc : d.getDocumentSet()) {
			if (doc.getUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}

}
