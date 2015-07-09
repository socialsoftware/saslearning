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
import pt.ulisboa.tecnico.saslearning.domain.Document;
import pt.ulisboa.tecnico.saslearning.domain.ElementFragment;
import pt.ulisboa.tecnico.saslearning.domain.StimulusFragment;

@Controller
public class ScenarioSyntaxController {

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
	
	@RequestMapping(value="/viewStructuredRepresentation/{docId}")
	public String viewStructuredRepresentation(@PathVariable String docId, Model m) {
		Document d = FenixFramework.getDomainObject(docId);
		List<StimulusFragment> scens = new ArrayList<StimulusFragment>();
		for(ElementFragment e : d.getFragmentSet()) {
			if(e instanceof StimulusFragment) {
				scens.add((StimulusFragment) e);
			}
		}
		m.addAttribute("scens", scens);
		m.addAttribute("docId", docId);
		return "structuredRepresentation";
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
}
