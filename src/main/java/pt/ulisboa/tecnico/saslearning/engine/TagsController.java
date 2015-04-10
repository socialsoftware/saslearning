package pt.ulisboa.tecnico.saslearning.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import pt.ulisboa.tecnico.saslearning.domain.Tag;

@Controller
public class TagsController {

	@RequestMapping(value="/manageTags")
	public String manageTags(Model m){
		List<TagAux> tags = getTags();
		TagAux nt = new TagAux();
		m.addAttribute("tags", tags);
		m.addAttribute("newTag", nt);
		return "manageTags";
	}
	
	@RequestMapping(value="/addTag", method = RequestMethod.POST)
	public String addTag(@ModelAttribute TagAux tag, Model m){
		addNewTag(tag);
		List<TagAux> tags = getTags();
		TagAux nt = new TagAux();
		m.addAttribute("tags", tags);
		m.addAttribute("newTag", nt);
		return "manageTags";
	}
	
	@RequestMapping(value="/removeTag/{id}")
	public String removeTag(Model m, @PathVariable String id){
		removeTagById(id);
		List<TagAux> tags = getTags();
		m.addAttribute("tags", tags);
		m.addAttribute("newTag", new TagAux());
		return "manageTags";
	}

	@Atomic(mode=TxMode.WRITE)
	private void addNewTag(TagAux tag) {
		if(!tagExists(tag.getTag())){
			Tag t = new Tag();
			t.setTag(tag.getTag());
			FenixFramework.getDomainRoot().addTag(t);
		}
	}
	
	@Atomic
	private boolean tagExists(String tag){
		Set<Tag> tags = FenixFramework.getDomainRoot().getTagSet();
		for(Tag t : tags){
			if(t.getTag().equals(tag)){
				return true;
			}
		}
		return false;
	}

	@Atomic
	private void removeTagById(String id) {
		Tag t = FenixFramework.getDomainObject(id);
		t.delete();
	}

	@Atomic
	private List<TagAux> getTags(){
		DomainRoot dr = FenixFramework.getDomainRoot();
		Set<Tag> tags = dr.getTagSet();
		List<TagAux> tagList = new ArrayList<TagAux>();
		for(Tag t : tags){
			TagAux nt = new TagAux();
			nt.setTag(t.getTag());
			nt.setId(t.getExternalId());
			tagList.add(nt);
		}
		return tagList;
	}
}
