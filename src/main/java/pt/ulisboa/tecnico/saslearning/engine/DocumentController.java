package pt.ulisboa.tecnico.saslearning.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

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

	@RequestMapping(value = "/selectDoc/{docId}/store/annotations", method = RequestMethod.POST)
	public RedirectView saveAnnotation(@PathVariable String docId,
			@RequestBody String annot, HttpServletResponse resp)
			throws IOException {
		String annId = writeAnnotation(annot, docId);
		return new RedirectView("/selectDoc/" + docId + "/store/annotations/" + annId);
	}

	// INDEX
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations", method = RequestMethod.GET)
	@ResponseBody
	public String getAnnotations(@PathVariable String docId) {
		return getDocAnnotations(docId);
		
	}
	
	// READ
	@RequestMapping(value = "/selectDoc/{docId}/store/annotations/{annId}", method = RequestMethod.GET)
	@ResponseBody
	public String readAnnotation(@PathVariable String annId) {
		return getSingleAnnotation(annId);
	}
	
	@Atomic
	public String getSingleAnnotation(String annId){
		Annotation ann = FenixFramework.getDomainObject(annId);
		return ann.getAnnotation();
	}
	

	@RequestMapping(value = "/selectDoc/{id}", method = RequestMethod.GET)
	public String showDocument(@PathVariable String id, Model m)
			throws IOException {
		String docUrl = getUrlById(id);
		org.jsoup.nodes.Document document = Jsoup.connect(docUrl).get();
		for (Element e : document.getAllElements()) {
			if (e.hasAttr("src")) {
				String path = e.absUrl("src");
				e.removeAttr("src");
				e.attr("src", path);
			}
			if (e.hasAttr("href")) {
				String path = e.absUrl("href");
				e.removeAttr("href");
				e.attr("href", path);
			}
			if (e.hasAttr("data")) {
				String path = e.absUrl("data");
				e.removeAttr("data");
				e.attr("data", path);
			}
		}
		m.addAttribute("link", document.children());
		return "docTemplate";
	}

	@Atomic
	public String getUrlById(String id) {
		Document d = FenixFramework.getDomainObject(id);
		return d.getUrl();
	}

	@Atomic
	private String getDocAnnotations(String docId) {
		String resp = "[";
		Document d = FenixFramework.getDomainObject(docId);
		Set<Annotation> annotations = d.getAnnotationSet();
		int n = annotations.size();
		if (n > 0) {
			for (Annotation an : annotations) {
				resp += an;
				resp += ",";
			}
			resp = resp.substring(0, resp.length() - 1);
			resp += "]";
			return resp;

		} else {
			return "[]";
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public String writeAnnotation(String annotation, String docId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation ann = new Annotation();
		String annId = ann.getExternalId();
		Gson gson = new Gson();
		AnnotationJ jsonObject = gson.fromJson(annotation, AnnotationJ.class);
		jsonObject.setId(annId);
		ann.setAnnotation(gson.toJson(jsonObject));
		d.addAnnotation(ann);
		return annId;
	}

	@Atomic(mode = TxMode.WRITE)
	public void addNewDocument(String url) {
		Document doc = new Document();
		doc.setUrl(url);
		FenixFramework.getDomainRoot().addDocument(doc);
	}

	@Atomic
	public List<DocUrl> getUrls() {
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
