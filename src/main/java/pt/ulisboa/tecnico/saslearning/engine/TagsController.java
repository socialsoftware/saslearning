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
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.Tag;

@Controller
public class TagsController {
	
	@RequestMapping(value = "/tagFragment")
	public String getFragment(){
		return "tagFragment";
	}

	@RequestMapping(value = "/manageTags")
	public String manageTags(Model m) {
		List<TagAux> tags = getTagHierarchy();
		TagAux nt = new TagAux();
		m.addAttribute("tags", tags);
		List<TagAux> flatList = getTagsFlatList(tags);
		m.addAttribute("tagFlatList", flatList);
		System.out.println("hierarchy: " + tags);
		System.out.println("flatlist " + flatList);
		m.addAttribute("newTag", nt);
		return "manageTags";
	}

	@RequestMapping(value = "/addTag", method = RequestMethod.POST)
	public RedirectView addTag(@ModelAttribute TagAux tag, Model m) {
		addNewTag(tag);
		List<TagAux> tags = getTagHierarchy();
		TagAux nt = new TagAux();
		m.addAttribute("tags", tags);
		m.addAttribute("newTag", nt);
		List<TagAux> flatList = getTagsFlatList(tags);
		m.addAttribute("tagFlatList", flatList);
		System.out.println("hierarchy: " + tags);
		System.out.println("flatlist " + flatList);
		RedirectView rv = new RedirectView("/manageTags");
		return rv;
	}

	@RequestMapping(value = "/removeTag/{id}")
	public RedirectView removeTag(Model m, @PathVariable String id) {
		removeTagById(id);
		List<TagAux> tags = getTagHierarchy();
		m.addAttribute("tags", tags);
		m.addAttribute("newTag", new TagAux());
		List<TagAux> flatList = getTagsFlatList(tags);
		m.addAttribute("tagFlatList", flatList);
		RedirectView rv = new RedirectView("/manageTags");
		return rv;
	}

	@Atomic(mode = TxMode.WRITE)
	private void addNewTag(TagAux tag) {
		String parentId = tag.getParentId();
		if (!tagExists(tag.getTag(), parentId)) {
			Tag t = new Tag();
			t.setTag(tag.getTag());
			if(parentId.equals("none")){
				FenixFramework.getDomainRoot().addTag(t);	
			}else{
				Tag parent = FenixFramework.getDomainObject(parentId);
				parent.addSubtag(t);
			}
			
		}
	}

	@Atomic
	private boolean tagExists(String tag, String parentId) {
		if(parentId.equals("none")){
			Set<Tag> tags = FenixFramework.getDomainRoot().getTagSet();
			return findTagOccurrenceInSet(tag, tags);
		}else{
			Tag parent = FenixFramework.getDomainObject(parentId);
			Set<Tag> children = parent.getSubtagSet();
			return findTagOccurrenceInSet(tag, children);
		}
		
	}
	
	private boolean findTagOccurrenceInSet(String tag, Set<Tag> set){
		for (Tag t : set) {
			if (t.getTag().equals(tag)) {
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
	private List<TagAux> getTagHierarchy() {
		DomainRoot dr = FenixFramework.getDomainRoot();
		Set<Tag> tags = dr.getTagSet();
		List<TagAux> tagList = new ArrayList<TagAux>();
		for (Tag t : tags) {
			List<TagAux> children = getChildrenList(t.getExternalId());
			if(children != null){
				Collections.sort(children);
			}
			TagAux nt = new TagAux();
			nt.setTag(t.getTag());
			nt.setId(t.getExternalId());
			nt.setSubtags(children);
			tagList.add(nt);
		}
		Collections.sort(tagList);
		return tagList;
	}

	@Atomic
	private List<TagAux> getChildrenList(String externalId) {
		List<TagAux> list = new ArrayList<TagAux>();
		Tag t = FenixFramework.getDomainObject(externalId);
		Set<Tag> children = t.getSubtagSet();
		if(children.size() == 0){
			return null;
		}else{
			for(Tag ch : children){
				TagAux c = new TagAux();
				c.setId(ch.getExternalId());
				c.setTag(ch.getTag());
				List<TagAux> subtags = getChildrenList(ch.getExternalId());
				if(subtags!= null){
					Collections.sort(subtags);
				}
				c.setSubtags(subtags);
				list.add(c);
			}
			Collections.sort(list);
			return list;
		}
	}
	
	public List<TagAux> getTagsFlatList(List<TagAux> tagHierarchy){
		List<TagAux> flatList = new ArrayList<TagAux>();
		for(TagAux t : tagHierarchy){
			TagAux n = new TagAux();
			n.setId(t.getId());
			n.setTag(t.getTag());
			if(t.getSubtags() != null){
				getTagChildren(t, flatList);
			}
			flatList.add(n);
		}
		Collections.sort(flatList);
		return flatList;
	}

	private void getTagChildren(TagAux t, List<TagAux> flatList) {
		for(TagAux child : t.getSubtags()){
			TagAux ch = new TagAux();
			ch.setId(child.getId());
			ch.setTag(child.getTag());
			flatList.add(ch);
			if(ch.getSubtags() != null){
				getTagChildren(child, flatList);
			}
		}
	}
	
	
}
