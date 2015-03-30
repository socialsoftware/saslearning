package pt.ulisboa.tecnico.saslearning.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Document;

@Controller
public class DocumentController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String goToHome() {
		return "home";
	}

	@RequestMapping(value = "/addDoc", method = RequestMethod.GET)
	public String goToAddDocument(Model model) {
		model.addAttribute("docUrl", new DocUrl());
		return "addDocument";
	}

	@RequestMapping(value = "/addDoc", method = RequestMethod.POST)
	public String addDocument(@ModelAttribute DocUrl doc) {
		addNewDocument(doc.getUrl());
		return "addDocument";
	}

	@RequestMapping(value = "seeDocs", method = RequestMethod.GET)
	public String seeDocuments(Model m) {
		List<DocUrl> docs = getUrls();
		m.addAttribute("docs", docs);
		m.addAttribute("docSelected", new DocUrl());
		return "seeDocuments";
	}

	@RequestMapping(value = "/selectDoc/{id}", method = RequestMethod.GET)
	public String showDocument(@PathVariable String id, Model m)
			throws IOException {
		String docUrl = getUrlById(id);
		org.jsoup.nodes.Document document = Jsoup.connect(docUrl).get();
		for (Element e : document.getAllElements()) {
			checkForAttributePath(e, "src");
			checkForAttributePath(e, "href");
			checkForAttributePath(e, "data");
		}
		m.addAttribute("link", document.children());
		return "docTemplate";
	}

	private void checkForAttributePath(Element e, String attr) {
		if (e.hasAttr(attr)) {
			String path = e.absUrl(attr);
			e.removeAttr(attr);
			e.attr(attr, path);
		}
	}

	@Atomic
	private String getUrlById(String id) {
		Document d = FenixFramework.getDomainObject(id);
		return d.getUrl();
	}

	@Atomic(mode = TxMode.WRITE)
	private void addNewDocument(String url) {
		Document doc = new Document();
		doc.setUrl(url);
		FenixFramework.getDomainRoot().addDocument(doc);
	}

	@Atomic
	private List<DocUrl> getUrls() {
		List<DocUrl> docs = new ArrayList<DocUrl>();
		DomainRoot d = FenixFramework.getDomainRoot();
		for (Document doc : d.getDocumentSet()) {
			DocUrl url = new DocUrl();
			url.setId(doc.getExternalId());
			url.setUrl(doc.getUrl());
			docs.add(url);
		}
		return docs;
	}

}
