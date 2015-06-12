package pt.ulisboa.tecnico.saslearning.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Tag;
import pt.ulisboa.tecnico.saslearning.jsonsupport.TagJ;

import com.google.gson.Gson;

@Controller
public class TagsController {
	
	@RequestMapping(value = "/tagFragment")
	public String getFragment(){
		return "tagFragment";
	}

	@RequestMapping(value = "/manageTags")
	public String manageTags(Model m) {
		TagAux nt = new TagAux();
		Set<Tag> tags = getTagList();
		m.addAttribute("tags", tags);
		m.addAttribute("newTag", nt);
		return "manageTags";
	}
	
	@RequestMapping(value = "/addTag", method = RequestMethod.POST)
	public RedirectView addTag(@ModelAttribute TagAux tag) {
		addNewTag(tag);
		RedirectView rv = new RedirectView("/manageTags");
		return rv;
	}

	@RequestMapping(value = "/removeTag/{id}")
	public RedirectView removeTag(@PathVariable String id) {
		removeTagById(id);
		RedirectView rv = new RedirectView("/manageTags");
		return rv;
	}
	
	//GET TAGS
	@RequestMapping(value = "/annotator/getTags")
	@ResponseBody
	public String getTags(){
		String json = getJsonTags();
		return json;
	}
	
	//---------------------------------------------------------------
	@Atomic(mode=TxMode.READ)
	private String getJsonTags(){
		DomainRoot dr = FenixFramework.getDomainRoot();
		Set<Tag> set = dr.getTagSet();
		List<String> tags = new ArrayList<String>(set.size());
		for(Tag t : set) {
			tags.add(t.getTag());
		}
		Collections.sort(tags);
		TagJ tagObj = new TagJ();
		tagObj.setTags(tags);
		Gson gson = new Gson();
		String json = gson.toJson(tagObj);
		return json;
	}
	
	//lista de tags:
	@Atomic(mode=TxMode.READ)
	private Set<Tag> getTagList(){
		DomainRoot dr = FenixFramework.getDomainRoot();
		Set<Tag> tags = dr.getTagSet();
		return tags;
		
	}

	@Atomic(mode = TxMode.WRITE)
	private void addNewTag(TagAux tag) {
		if (!tagExists(tag.getTag())) {
			DomainRoot dr = FenixFramework.getDomainRoot();
			Tag t = new Tag();
			t.setTag(tag.getTag());
			dr.addTag(t);
		}
	}

	@Atomic(mode=TxMode.READ)
	private boolean tagExists(String tag) {
		DomainRoot dr = FenixFramework.getDomainRoot();
		for(Tag t : dr.getTagSet()) {
			if(t.getTag().equals(tag)) {
				return true;
			}
		}
		return false;
		
	}
	@Atomic(mode=TxMode.WRITE)
	private void removeTagById(String id) {
		Tag t = FenixFramework.getDomainObject(id);
		t.delete();
	}
}
