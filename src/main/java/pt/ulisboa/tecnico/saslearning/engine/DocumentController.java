package pt.ulisboa.tecnico.saslearning.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import pt.ulisboa.tecnico.saslearning.domain.Document;

@Controller
public class DocumentController {

	@RequestMapping(value = "/manageDocs")
	public String manageDocuments(Model m) {
		List<DocUrl> docs = getUrls();
		m.addAttribute("docs", docs);
		m.addAttribute("newDoc", new DocUrl());
		return "manageDocs";
	}

	@RequestMapping(value = "/removeDoc/{id}", method = RequestMethod.GET)
	public RedirectView removeDocument(Model m, @PathVariable String id) {
		removeDocumentById(id);
		List<DocUrl> docs = getUrls();
		m.addAttribute("docs", docs);
		m.addAttribute("newDoc", new DocUrl());
		RedirectView rv = new RedirectView("/manageDocs");
		return rv;
	}


	@RequestMapping(value = "/addDoc", method = RequestMethod.POST)
	public RedirectView addDocument(@ModelAttribute DocUrl doc, Model m)
			throws IOException {
		addNewDocument(doc.getUrl());
		m.addAttribute("newDoc", new DocUrl());
		List<DocUrl> docs = getUrls();
		m.addAttribute("docs", docs);
		RedirectView rv = new RedirectView("/manageDocs");
		return rv;
	}

	@RequestMapping(value = "seeDocs", method = RequestMethod.GET)
	public String listDocumentsAvailable(Model m) {
		List<DocUrl> docs = getUrls();
		m.addAttribute("docs", docs);
		m.addAttribute("docSelected", new DocUrl());
		return "seeDocuments";
	}

	@RequestMapping(value = "/selectDoc/{id}", method = RequestMethod.GET)
	public String showDocument(@PathVariable String id, Model m)
			throws IOException {
//		DocUrl doc = getDocumentById(id);
//		m.addAttribute("article", doc.getContent());
//		m.addAttribute("source", doc.getUrl());
		m.addAttribute("docId", id);
		return "docTemplate";
	}
	
	@RequestMapping(value = "/getDoc/{id}")
	public String getDocument(@PathVariable String id, Model m){
		DocUrl doc = getDocumentById(id);
//		m.addAttribute("article", doc.getContent());
//		m.addAttribute("source", doc.getUrl());
		m.addAttribute("doc", doc);
		return "document";
	}
	
	@RequestMapping(value="/documentHeader")
	public String getDocumentHeader(){
		return "documentHeader";
	}
	
	@Atomic
	private void removeDocumentById(String id) {
		Document d = FenixFramework.getDomainObject(id);
		d.delete();
	}

	private void checkForAttributePath(Element e, String attr) {
		
		if (e.hasAttr(attr)) {
			if(e.attr(attr).charAt(0) != '#'){
				String path = e.absUrl(attr);
				e.removeAttr(attr);
				e.attr(attr, path);
			}
		}
	}

	@Atomic(mode = TxMode.READ)
	private DocUrl getDocumentById(String id) {
		Document d = FenixFramework.getDomainObject(id);
		DocUrl doc = new DocUrl();
		doc.setId(id);
		doc.setTitle(d.getTitle());
		doc.setUrl(d.getUrl());
		doc.setContent(d.getContent());
		return doc;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addNewDocument(String url) throws IOException {
		if (!documentExists(url)){
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

	@Atomic
	private List<DocUrl> getUrls() {
		List<DocUrl> docs = new ArrayList<DocUrl>();
		DomainRoot d = FenixFramework.getDomainRoot();
		for (Document doc : d.getDocumentSet()) {
			DocUrl url = new DocUrl();
			url.setId(doc.getExternalId());
			url.setUrl(doc.getUrl());
			url.setTitle(doc.getTitle());
			url.setContent(doc.getContent());
			docs.add(url);
		}
		return docs;
	}

}
