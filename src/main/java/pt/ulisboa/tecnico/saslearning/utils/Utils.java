package pt.ulisboa.tecnico.saslearning.utils;

import pt.ulisboa.tecnico.saslearning.jsonsupport.TacticsTags;
import pt.ulisboa.tecnico.saslearning.jsonsupport.TagGroup;
import pt.ulisboa.tecnico.saslearning.jsonsupport.Tags;

import com.google.gson.Gson;

public class Utils {
	private static Gson g = new Gson();
	private static String[] scenarios = {"Scenario", "Source Of Stimulus", "Stimulus", "Artifact",
			"Environment", "Response", "Response Measure"};
	private static String[] qualityAttrs = {"Availability", "Interoperability", "Modifiability",
			  "Performance", "Security", "Testability", "Usability"};
	private static String[] tactics  = {"Tactic for Availability","Tactic for Interoperability",
			"Tactic for Modifiability", "Tactic for Performance", "Tactic for Security",
			  "Tactic for Testability", "Tactic for Usability"};
	

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
		String avail = getTacts(tactics);
		return avail;
	}
	
	public static String interoperabilityTactics() {
		TagGroup i1 = new TagGroup();
		String[] loc = {"Discover Service"};
		i1.setName("Locate");
		i1.setTags(loc);
		
		TagGroup i2 = new TagGroup();
		String[] mif = {"Orchestrate", "Tailor Interface"};
		i2.setName("Manage Interfaces");
		i2.setTags(mif);
		
		TagGroup[] tacs = {i1, i2};
		String inter = getTacts(tacs);
		return inter;
	}
	
	private static String getTacts(TagGroup[] tacs) {
		TacticsTags t = new TacticsTags();
		t.setTacticGroups(tacs);
		String inter = g.toJson(t);
		return inter;
	}
	
	public static String getTactics(String type) {
		switch(type) {
		case "Tactic for Availability":
			return availabilityTactics();
		case "Tactic for Interoperability":
			return interoperabilityTactics();
		case "Tactic for Modifiability":
			return modifiabilityTactics();
		case "Tactic for Performance":
			return performanceTactics();
		case "Tactic for Security":
			return securityTactics();
		case "Tactic for Testability":
			return testabilityTactics();
		case "Tactic for Usability":
			return usabilityTactics();
		default:
			return null;
		}
	}

	private static String usabilityTactics() {
		String n1 = "Support User Initiative";
		String[] t1 = {"Cancel", "Undo", "Pause/Resume", "Aggregate"};
		String n2 = "Support System Initiative";
		String[] t2 = {"Maintain Task Model", "Maintain User Model",
				"Maintain System Model"
		};
		TagGroup g1 = new TagGroup();
		TagGroup g2 = new TagGroup();
		g1.setName(n1);
		g2.setName(n2);
		g1.setTags(t1);
		g2.setTags(t2);
		TagGroup[] grps = {g1,g2};
		String tacts = getTacts(grps);
		return tacts;
	}

	private static String testabilityTactics() {
		String n1 = "Control and Observe System State";
		String[] t1 = {"Specialized Interfaces", "Record/Playback", 
				"Localize State Storage", "Abstract Data Sources",
				"Sandbox", "Executable Assertions"
		};
		String n2 = "Limit Complexity";
		String[] t2 = {"Limit Structural Complexity", "Limit Nondeterminism"};
		TagGroup g1 = new TagGroup();
		g1.setName(n1);
		g1.setTags(t1);
		TagGroup g2 = new TagGroup();
		g2.setName(n2);
		g2.setTags(t2);
		TagGroup[] grps = {g1,g2};
		String tacts = getTacts(grps);
		return tacts;
	}

	private static String securityTactics() {
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
		TagGroup g1 = new TagGroup();
		g1.setName(n1);
		g1.setTags(t1);
		
		TagGroup g2 = new TagGroup();
		g2.setName(n2);
		g2.setTags(t2);
		
		TagGroup g3 = new TagGroup();
		g3.setName(n3);
		g3.setTags(t3);
		
		TagGroup g4 = new TagGroup();
		g4.setName(n4);
		g4.setTags(t4);
		
		TagGroup g5 = new TagGroup();
		g5.setName(n5);
		g5.setTags(t5);
		
		TagGroup[] grps = {g1,g2,g3,g4,g5};
		String tacts = getTacts(grps);
		return tacts;
	}

	private static String performanceTactics() {
		String n1 = "Control Resource Demand";
		String[] t1 = {"Manage Sampling Rate", "Limit Event Response", 
				"Prioritize Events", "Reduce Overhead", 
				"Bound Execution Times", "Increase Resource Efficiency"};
		String n2 = "Manage Resources";
		String[] t2 = {"Increase Resources", "Introduce Concurrency",
				"Maintain Multiple Copies of Computations",
				"Maintain Multiple Copies of Data",
				"Bound Queue Sizes", "Schedule Resources"};
		TagGroup g1 = new TagGroup();
		g1.setName(n1);
		g1.setTags(t1);
		TagGroup g2 = new TagGroup();
		g2.setName(n2);
		g2.setTags(t2);
		TagGroup[] grps = {g1,g2};
		String tacts = getTacts(grps);
		return tacts;
	}

	private static String modifiabilityTactics() {
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
		TagGroup g1 = new TagGroup();
		g1.setName(n1);
		g1.setTags(t1);
		TagGroup g2 = new TagGroup();
		g2.setName(n2);
		g2.setTags(t2);
		TagGroup g3 = new TagGroup();
		g3.setName(n3);
		g3.setTags(t3);
		TagGroup g4 = new TagGroup();
		g4.setName(n4);
		g4.setTags(t4);
		TagGroup[] grps = {g1,g2,g3,g4};
		String tacts = getTacts(grps);
		return tacts;
	}

}
