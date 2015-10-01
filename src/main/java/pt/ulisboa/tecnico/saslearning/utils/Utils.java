package pt.ulisboa.tecnico.saslearning.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.saslearning.domain.User;
import pt.ulisboa.tecnico.saslearning.jsonsupport.TagGroup;
import pt.ulisboa.tecnico.saslearning.jsonsupport.Tags;

import com.google.gson.Gson;

public class Utils {
	private static String[] scenarios = {"Scenario Description", "Source Of Stimulus", "Stimulus", "Artifact",
			"Environment", "Response", "Response Measure"};
	private static String[] tactics  = {"Tactic"};
	private static String[] moduleVT = {"Module","Module - Name", "Module - Implementation Details", "Module - Interface", "Module - Responsibility"};
	private static String[] views = {"View"};
	private static String[] ccVT = {"Component", "Component Port", "Connector", "Connector Role"};
	public static List<String> allScenarioConcepts(){
		List<String> concepts = new ArrayList<String>();
		for(String i : scenarios) {
			concepts.add(i);
		}
		for(String k : tactics) {
			concepts.add(k);
		}
		return concepts;
	}
	
	public static List<String> moduleConcepts(){
		List<String> concepts = new ArrayList<String>();
		for(String i : moduleVT) {
			concepts.add(i);
		}
		return concepts;
	}

	public static String getJsonTags() {
		TagGroup scenarioTags = new TagGroup();
		scenarioTags.setName("Scenarios");
		scenarioTags.setTags(scenarios);
		TagGroup tacticsTags = new TagGroup();
		tacticsTags.setName("Tactics");
		tacticsTags.setTags(tactics);
		TagGroup moduleVtTags = new TagGroup();
		moduleVtTags.setName("Module ViewType");
		moduleVtTags.setTags(moduleVT);
		TagGroup ccVtTags = new TagGroup();
		ccVtTags.setName("Component&Connector ViewType");
		ccVtTags.setTags(ccVT);
		TagGroup viewsTags = new TagGroup();
		viewsTags.setName("Views");
		viewsTags.setTags(views);
		Tags t = new Tags();
		t.setScenarios(scenarioTags);
		t.setTactics(tacticsTags);
		t.setModulevt(moduleVtTags);
		t.setViews(viewsTags);
		t.setCcvt(ccVtTags);
		Gson g = new Gson();
		String obj = g.toJson(t);
		return obj;
	}
	
	private static Map<String, String[]> availabilityTactics() {
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		String n1 = "Detect Faults";
		String[] detectFaults = {"Ping/Echo", "Monitor", "Heartbeat", "Timestamp", 
				"Sanity Checking", "Condition Monitoring", "Voting", 
				"Exception Detection", "Self-Test"};
		map.put(n1, detectFaults);
		String[] prepRep = {"Active Redundancy", "Passive Redundancy", "Spare", 
				"Exception Handling", "Rollback", "Software Upgrade", "Retry",
				"Ignore Faulty Behavior", "Degradation", "Reconfiguration"};
		String n2 = "Recover from Faults - Preparation and Repair";
		map.put(n2, prepRep);
		String[] reint = {"Shadow", "State Resynchronization", "Escalating Restart",
				"Non-Stop Forwarding"};
		String n3 = "Recover from Faults - Reintroduction";
		map.put(n3, reint);
		String[] prev = {
				"Removal from Service",
				"Transactions",
				"Predictive Model",
				"Exception Prevention",
				"Increase Competence Set"
		};
		String n4 = "Prevent Faults";
		map.put(n4,prev);
		return map;
	}
	
	public static Map<String, String[]> interoperabilityTactics() {
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		String[] loc = {"Discover Service"};
		String n1 = "Locate";
		map.put(n1,loc);
		String[] mif = {"Orchestrate", "Tailor Interface"};
		map.put("Manage Interfaces",mif);
		return map;
	}

	
	public static Map<String, Map<String, String[]>> getTactics() {
		Map<String, Map<String, String[]>> map = new LinkedHashMap<String, Map<String,String[]>>(); 
		map.put("Availability", availabilityTactics());
		map.put("Interoperability", interoperabilityTactics());
		map.put("Usability", usabilityTactics());
		map.put("Testability", testabilityTactics());
		map.put("Security", securityTactics());
		map.put("Performance", performanceTactics());
		map.put("Modifiability", modifiabilityTactics());
		return map;
	}
	
	public static List<String> getQualityRequirements(){
		List<String> qr = new ArrayList<String>();
		qr.add("Availability");
		qr.add("Interoperability");
		qr.add("Usability");
		qr.add("Testability");
		qr.add("Security");
		qr.add("Performance");
		qr.add("Modifiability");
		return qr;
	}

	private static Map<String, String[]> usabilityTactics() {
		String n1 = "Support User Initiative";
		String[] t1 = {"Cancel", "Undo", "Pause/Resume", "Aggregate"};
		String n2 = "Support System Initiative";
		String[] t2 = {"Maintain Task Model", "Maintain User Model",
				"Maintain System Model"
		};
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		map.put(n1,t1);
		map.put(n2,t2);
		return map;
	}

	private static Map<String, String[]> testabilityTactics() {
		String n1 = "Control and Observe System State";
		String[] t1 = {"Specialized Interfaces", "Record/Playback", 
				"Localize State Storage", "Abstract Data Sources",
				"Sandbox", "Executable Assertions"
		};
		String n2 = "Limit Complexity";
		String[] t2 = {"Limit Structural Complexity", "Limit Nondeterminism"};
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		map.put(n1, t1);
		map.put(n2, t2);
		return map;
	}

	private static Map<String, String[]> securityTactics() {
		String n1 = "Detect Attacks";
		String[] t1 = {"Detect Intrusion", "Detect Service Denial",
				"Verify Message Integrity", "Detect Message Delay"};
		String n2 = "Resist Attacks";
		String[] t2 = {"Identify Actors", "Authenticate Actors",
				"Authorize Actors", "Limit Access", "Limit Exposure",
				"Encrypt Data", "Separate Entities", "Change Default Settings"};
		String n3 = "React to Attacks";
		String[] t3 = {"Revoke Access", "Lock Computer", "Inform Actors"};
		String n4 = "Recover from Attacks";
		String[] t4 = {"Maintain Audit Trail"};
		String n5 = "Recover from Attacks - Restore";
		String[] t5 = {"See Availability"};
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		map.put(n1, t1);
		map.put(n2, t2);
		map.put(n3, t3);
		map.put(n4, t4);
		map.put(n5, t5);
		return map;
	}

	private static Map<String, String[]> performanceTactics() {
		String n1 = "Control Resource Demand";
		String[] t1 = {"Manage Sampling Rate", "Limit Event Response", 
				"Prioritize Events", "Reduce Overhead", 
				"Bound Execution Times", "Increase Resource Efficiency"};
		String n2 = "Manage Resources";
		String[] t2 = {"Increase Resources", "Introduce Concurrency",
				"Maintain Multiple Copies of Computations",
				"Maintain Multiple Copies of Data",
				"Bound Queue Sizes", "Schedule Resources"};
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		map.put(n1, t1);
		map.put(n2, t2);
		return map;
	}

	private static Map<String, String[]> modifiabilityTactics() {
		String n1 = "Reduce Size of a Module";
		String[] t1 = {"Split Module"};
		String n2 = "Increase Cohesion";
		String[] t2 = {"Increase Semantic Coherence"};
		String n3 = "Reduce Coupling";
		String[] t3 = {"Encapsulate", "Use an Intermediary", 
				"Restrict Dependencies", "Refactor", 
				"Abstract Common Services"};
		String n4 = "Defer Binding";
		String[] t4 = {"Defer Binding"};
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		map.put(n1, t1);
		map.put(n2, t2);
		map.put(n3, t3);
		map.put(n4, t4);
		return map;
	}
	
	public static boolean userExists(String username) {
		for(User u : FenixFramework.getDomainRoot().getUserSet()) {
			if(u.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

}
