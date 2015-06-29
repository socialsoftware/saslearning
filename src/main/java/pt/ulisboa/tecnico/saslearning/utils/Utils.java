package pt.ulisboa.tecnico.saslearning.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import pt.ulisboa.tecnico.saslearning.domain.ArtifactFragment;
import pt.ulisboa.tecnico.saslearning.domain.EnvironmentFragment;
import pt.ulisboa.tecnico.saslearning.domain.ResponseFragment;
import pt.ulisboa.tecnico.saslearning.domain.ResponseMeasureFragment;
import pt.ulisboa.tecnico.saslearning.domain.ScenarioFragment;
import pt.ulisboa.tecnico.saslearning.domain.SrcOfStimulusFragment;
import pt.ulisboa.tecnico.saslearning.domain.StimulusFragment;
import pt.ulisboa.tecnico.saslearning.jsonsupport.TacticsTags;
import pt.ulisboa.tecnico.saslearning.jsonsupport.TagGroup;
import pt.ulisboa.tecnico.saslearning.jsonsupport.Tags;

public class Utils {

	private Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	private static String tagsJson = "{"
			+ "\"Scenarios\" : "
			+ "[\"Scenario\", \"Source Of Stimulus\", \"Stimulus\","
			+" \"Artifact\", \"Environment\", \"Response\", "
			+"\"Response Measure\"],"
			+ "\"Tactics\" : "
			+ "[\"Tactic for Availability\", \"Tactic for Interoperability\" "
			+ ", \"Tactic for Modifiability\", "
			+ "\"Tactic for Performance\", \"Tactic for Security\","
			+ "\"Tactic for Testability\", \"Tactic for Usability\"],"
			+ "\"Quality Attributes\" : [\"Availability\", \"Interoperability\", \"Modifiability\","
					+ "\"Performance\", \"Security\", \"Testability\",\"Usability\"]}";
	private static String[] scenarios = {"Scenario", "Source Of Stimulus", "Stimulus", "Artifact",
			"Environment", "Response", "Response Measure"};
	private static String[] qualityAttrs = {"Availability", "Interoperability", "Modifiability",
			  "Performance", "Security", "Testability", "Usability"};
	private static String[] tactics  = {"Tactic for Availability","Tactic for Interoperability",
			"Tactic for Modifiability", "Tactic for Performance", "Tactic for Security",
			  "Tactic for Testability", "Tactic for Usability"};
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
		TagGroup scenarioTags = new TagGroup();
		scenarioTags.setName("Scenarios");
		scenarioTags.setTags(scenarios);
		TagGroup qualityAttrTags = new TagGroup();
		qualityAttrTags.setName("Quality Attributes");
		qualityAttrTags.setTags(qualityAttrs);
		TagGroup tacticsTags = new TagGroup();
		tacticsTags.setName("Tactics");
		tacticsTags.setTags(tactics);
		Tags t = new Tags();
		t.setScenarios(scenarioTags);
		t.setQualityAttrs(qualityAttrTags);
		t.setTactics(tacticsTags);
		Gson g = new Gson();
		String obj = g.toJson(t);
		return obj;
	}
	
	private static String availabilityTactics() {
		TagGroup a1 = new TagGroup();
		a1.setName("Detect Faults");
		String[] detectFaults = {"Ping/Echo", "Monitor", "Heartbeat", "Timestamp", 
				"Sanity Checking", "Condition Monitoring", "Voting", 
				"Exception Detection", "Self-Test"};
		a1.setTags(detectFaults);
		String[] prepRep = {"Active Redundancy", "Passive Redundancy", "Spare", 
				"Exception Handling", "Rollback", "Software Upgrade", "Retry",
				"Ignore Faulty Behavior", "Degradation", "Reconfiguration"};
		TagGroup a2 = new TagGroup();
		a2.setName("Recover from Faults - Preparation and Repair");
		a2.setTags(prepRep);
		String[] reint = {"Shadow", "State Resynchronization", "Escalating Restart",
				"Non-Stop Forwarding"};
		TagGroup a3 = new TagGroup();
		a3.setName("Recover from Faults - Reintroduction");
		a3.setTags(reint);
		String[] prev = {
				"Removal from Service",
				"Transactions",
				"Predictive Model",
				"Exception Prevention",
				"Increase Competence Set"
		};
		TagGroup a4 = new TagGroup();
		a4.setName("Prevent Faults");
		a4.setTags(prev);
		TagGroup[] tactics = {a1,a2,a3,a4};
		TacticsTags t = new TacticsTags();
		t.setTacticGroups(tactics);
		Gson g = new Gson();
		String avail = g.toJson(t);
		return avail;
	}
	
	public static String getTactics(String type) {
		switch(type) {
		case "Tactic for Availability":
			return availabilityTactics();
		default:
			return null;
		}
	}

}
