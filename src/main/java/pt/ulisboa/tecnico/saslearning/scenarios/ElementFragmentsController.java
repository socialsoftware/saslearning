package pt.ulisboa.tecnico.saslearning.scenarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Annotation;
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.ElementFragment;
import pt.ulisboa.tecnico.saslearning.utils.Utils;

@Controller
public class ElementFragmentsController {
	Utils utils = new Utils();
	
	@RequestMapping(value="/removeSyntax/{docId}/{frag}/{toUnlink}")
	public RedirectView removeFragmentSyntax(@PathVariable String docId, @PathVariable String frag, @PathVariable String toUnlink) {
		ElementFragment src = FenixFramework.getDomainObject(frag);
		ElementFragment unlink = FenixFramework.getDomainObject(toUnlink);
		unlinkDifferentFragments(src, unlink);
		RedirectView rv = new RedirectView("/linkManager/" + docId + "/" + frag);
		return rv;
	}
	
	@RequestMapping(value="/addFragmentSyntax/{docId}/{toLink}/{linked}")
	public RedirectView addFragmentSyntax(@PathVariable String docId, @PathVariable String toLink, @PathVariable String linked) {
		ElementFragment fragToLink = FenixFramework.getDomainObject(toLink);
		ElementFragment destFrag = FenixFramework.getDomainObject(linked);
		linkDifferentFragments(fragToLink, destFrag);
		RedirectView rv = new RedirectView("/linkManager/" + docId + "/" + toLink);
		return rv;
	}
	
	@RequestMapping(value="/linkManager/{docId}/{fragId}", method=RequestMethod.GET)
	public String linkManager(@PathVariable String docId, @PathVariable String fragId, Model m) {
		ElementFragment e = FenixFramework.getDomainObject(fragId);
		Document d = FenixFramework.getDomainObject(docId);
		Set<ElementFragment> heads = d.getFragmentSet();
		Map<String, List<ElementFragment>> possibleLinks = new HashMap<String, List<ElementFragment>>();
		List<String> possibilities = e.possibleConnections();
		m.addAttribute("chainToLink", e);
		for(String tag : possibilities) { //preencher o mapa com as possiveis ligaçoes
			for(ElementFragment elem : heads) {
				if(elem.getName().equals(tag) && !elem.hasConnections()) {
					if(possibleLinks.get(tag) != null) {
						possibleLinks.get(tag).add(elem);
					}else {
						List<ElementFragment> list = new ArrayList<ElementFragment>();
						list.add(elem);
						possibleLinks.put(tag, list);
					}
				}
			}
		}
		m.addAttribute("possibleLinks", possibleLinks);
		m.addAttribute("docId", docId);
		m.addAttribute("annId", e.getAnnotation().getExternalId());
		return "linksManager";
	}
	
	@RequestMapping(value = "/fragmentManager/{docId}/{annotationId}")
	public String fragmentManager(Model m,
			@PathVariable String docId, @PathVariable String annotationId) {
		Document d = FenixFramework.getDomainObject(docId);
		Annotation a = FenixFramework.getDomainObject(annotationId);
		ElementFragment e = a.getFragment();
		m.addAttribute("frags", d.getFragmentSet());
		m.addAttribute("current", e);
		m.addAttribute("docId", docId);
		return "fragmentManager";
	}

	@RequestMapping(value = "linkFragment/{docId}/{fragToLink}/{parentFrag}")
	public RedirectView linkAnnotationToFragment(@PathVariable String docId,
			@PathVariable String fragToLink, @PathVariable String parentFrag) {
		ElementFragment toLink = FenixFramework.getDomainObject(fragToLink);
		ElementFragment parent = FenixFramework.getDomainObject(parentFrag);
		linkFragments(toLink, parent);
		RedirectView rv = new RedirectView("/fragmentManager/" + docId+"/" + toLink.getAnnotation().getExternalId());
		return rv;
	}
	
	@RequestMapping(value = "unlinkFragment/{docId}/{fragToUnlink}/{chainHead}")
	public RedirectView unlinkAnnotationFromChain(@PathVariable String docId,
			@PathVariable String fragToUnlink, @PathVariable String chainHead) {
		ElementFragment toUnlink = FenixFramework.getDomainObject(fragToUnlink);
		ElementFragment head = FenixFramework.getDomainObject(chainHead);
		unlinkFragments(toUnlink, head);
		RedirectView rv = new RedirectView("/fragmentManager/" + docId+"/" + toUnlink.getAnnotation().getExternalId());
		return rv;
	}
	
	@Atomic(mode=TxMode.WRITE)
	private void unlinkDifferentFragments(ElementFragment src,
			ElementFragment unlink) {
		src.unlink(unlink);
	}

	@Atomic(mode=TxMode.WRITE)
	private void linkDifferentFragments(ElementFragment fragToLink, ElementFragment destFrag) {
		fragToLink.connect(destFrag);
	}

	@Atomic(mode=TxMode.WRITE)
	private void unlinkFragments(ElementFragment toUnlink, ElementFragment head) {
		if(toUnlink.getExternalId().equals(head.getExternalId())) { // remover a cabeça da lista
			ElementFragment child = toUnlink.getChild();
			child.setParent(null);
			child.setDocument(toUnlink.getDocument());
			if(child.getChild() == null) {
				child.setLinked(false);
			}
			toUnlink.setChild(null);
			toUnlink.setLinked(false);
		}else { //remover do meio ou do fim da lista
			ElementFragment e = head;
			while(!e.getChild().getExternalId().equals(toUnlink.getExternalId())) {
				e = e.getChild();
			} //e = nó antes do que queremos remover
			ElementFragment child = toUnlink.getChild();
			if(child != null) {
				child.setParent(e);
			}
			e.setChild(child);
			toUnlink.setParent(null);
			toUnlink.setChild(null);
			toUnlink.setLinked(false);
			if(head.getChild() == null) { //se por acaso só estavam 2 fragmentos ligados e removemos 1
				head.setLinked(false);
			}
			
		}
	}

	@Atomic(mode=TxMode.WRITE)
	private void linkFragments(ElementFragment toLink, ElementFragment parent) {
		ElementFragment e = parent;
		while(e.getChild() != null) {
			e = e.getChild();
		}
		toLink.setDocument(null);
		e.setChild(toLink);
		e.setLinked(true);
		toLink.setLinked(true);
	}
}
