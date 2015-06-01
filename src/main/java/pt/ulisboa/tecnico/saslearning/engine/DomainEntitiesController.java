package pt.ulisboa.tecnico.saslearning.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.jsonsupport.AnnotationJ;

@Controller
public class DomainEntitiesController {

	@RequestMapping(value="/addSyntax/{docId}")
	public String syntaxManager(Model m, @PathVariable String docId){
		m.addAttribute("annotations", getAnnotations(docId));
		return "syntaxManager";
	}
	
	@Atomic(mode=TxMode.READ)
	public List<AnnotationJ> getAnnotations(String docId){
		Gson gson = new Gson();
		Document d = FenixFramework.getDomainObject(docId);
		Set<Annotation> anns = d.getAnnotationSet();
		List<AnnotationJ> annotations = new ArrayList<AnnotationJ>(); 
		for(Annotation a : anns){
			AnnotationJ ann = gson.fromJson(a.getAnnotation(), AnnotationJ.class);
			annotations.add(ann);
		}
		return annotations;
	}
}
