package pt.ulisboa.tecnico.saslearning.utils;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulusFragment;

public class Utils {
	
	private Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	
	public Utils() {
		map.put("Source Of Stimulus", SrcOfStimulusFragment.class);
	}
	
	public Class<?> getFragClass(String name){
		return map.get(name);
	}
}
