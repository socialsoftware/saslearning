package pt.ulisboa.tecnico.saslearning.utils;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.saslearning.domain.ArtifactFragment;
import pt.ulisboa.tecnico.saslearning.domain.EnvironmentFragment;
import pt.ulisboa.tecnico.saslearning.domain.ResponseFragment;
import pt.ulisboa.tecnico.saslearning.domain.ResponseMeasureFragment;
import pt.ulisboa.tecnico.saslearning.domain.ScenarioFragment;
import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulusFragment;
import pt.ulisboa.tecnico.saslearning.domain.StimulusFragment;

public class Utils {

	private Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	private static String tagsJson = "{"
			+ "\"Scenario\" : "
			+ "[\"Scenario\", \"Source Of Stimulus\", \"Stimulus\","
			+" \"Artifact\", \"Environment\", \"Response\", "
			+"\"Response Measure\"]}";

	public Utils() {
		map.put("Scenario", ScenarioFragment.class);
		map.put("Source Of Stimulus", SrcOfStimulusFragment.class);
		map.put("Stimulus", StimulusFragment.class);
		map.put("Artifact", ArtifactFragment.class);
		map.put("Environment", EnvironmentFragment.class);
		map.put("Response", ResponseFragment.class);
		map.put("Response Measure", ResponseMeasureFragment.class);

	}

	public Class<?> getFragClass(String name) {
		return map.get(name);
	}
	
	public static String getJsonTags() {
		return tagsJson;
	}

}
