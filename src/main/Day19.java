package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import advent_code_common.FileUtility;

public class Day19 {
	private static final String DAY19_INPUT_TXT = "./src/resources/day19_sample.txt";
	private static final Pattern blueprint_pattern = Pattern.compile("Blueprint (\\d+):");
	private static final Pattern robot_pattern = Pattern.compile("Each ([a-z]+) robot costs ([^.]+)\\.");
	private static final Pattern cost_pattern = Pattern.compile("(\\d+) ([a-z]+)");
	private static final Matcher blueprint_matcher = blueprint_pattern.matcher("");
	private static final Matcher robot_matcher = robot_pattern.matcher("");
	private static final Matcher cost_matcher = cost_pattern.matcher("");
	private static final int ORE = 0;
	private static final int CLAY = 1;
	private static final int OBSIDIAN = 2;
	private static final int GEODE = 3;
	private static final int ORE_ROBOT = 4;
	private static final int CLAY_ROBOT = 5;
	private static final int OBSIDIAN_ROBOT = 6;
	private static final int GEODE_ROBOT = 7;
	private static final int MINUTE = 8;

	
	public static void main(String[] args) {
		List<Blueprint> blueprints = Blueprint.parse(DAY19_INPUT_TXT);
		System.out.println(evaluate(blueprints.get(0)));

	}
	
	private static List<int[]> nextStates(int[] state, Blueprint blueprint) {
		List<int[]> states = new ArrayList<>();
		// time passes
		state[MINUTE]++;
		// always purchase geode robot
		if (canPurchase(GEODE,state,blueprint)) {
			int[] newState = purchase(GEODE,state,blueprint);
			collect(state,newState);
			states.add(newState);
			return states;
		}
		if (canPurchase(OBSIDIAN,state,blueprint)) {
			int[] newState = purchase(OBSIDIAN,state,blueprint);
			collect(state,newState);
			states.add(newState);
			return states;
		}
		int couldPurchase = 0;
		for (int res=0; res<2; res++) {
			if (canPurchase(res,state,blueprint)) {
				int[] newState = purchase(res,state,blueprint);
				collect(state, newState);
				states.add(newState);
			}	
		}	
		// or do nothing - but only if we haven't saved enough to purchase 
		if (couldPurchase < 2) {
			collect(state,state);	
			states.add(state);
		}	
		return states;
	}

	private static int[] purchase(int robotResource, int[] state, Blueprint blueprint) {
		int[] newState = Arrays.copyOf(state,9);
		for (int res=0; res<3; res++) {
			newState[res] -= blueprint.robot[robotResource].cost[res]; // deduct cost
		}
		newState[robotResource+4]++; // add robot
		return newState;
	}

	private static void collect(int[] state, int[] newState) {
		// robots do robot stuff
		newState[ORE] += state[ORE_ROBOT];
		newState[CLAY] += state[CLAY_ROBOT];
		newState[OBSIDIAN] += state[OBSIDIAN_ROBOT];
		newState[GEODE] += state[GEODE_ROBOT];
	}
	

	private static boolean canPurchase(int robotResource, int[] state, Blueprint blueprint) {
		for (int res=0; res<3; res++) {
			if (state[res] < blueprint.robot[robotResource].cost[res]) {
				return false;
			}
		}
		return true;
	}

	private static int evaluate(Blueprint blueprint) {
		int[] initialState = new int[] {0,0,0,0, 1,0,0,0, 0}; // 1 ore robot
		int geodes = 0;
		Set<int[]> states = new HashSet<>();
		states.add(initialState);
		while (!states.isEmpty()) {
			System.out.println(states.size());
			int[] state = states.iterator().next();
			states.remove(state);
			if(state[MINUTE] == 20) {
				geodes = Math.max(geodes, state[GEODE]);
			}
			else {
				states.addAll(nextStates(state, blueprint));
			}
		}
		return geodes;	
	}



	record Blueprint (int number, Robot[] robot) {
		
		public static List<Blueprint> parse(String filename) {
			List<Blueprint> blueprints = new ArrayList<>();
			FileUtility.readListOfString(filename).stream()
				.forEach(s -> {
					blueprint_matcher.reset(s);
					if (blueprint_matcher.find()) {
						Blueprint blueprint = new Blueprint(Integer.valueOf(blueprint_matcher.group(1)),
								 new Robot[4]);
						robot_matcher.reset(s);
						while (robot_matcher.find()) {
							blueprint.robot[resource(robot_matcher.group(1))] = Robot.parse(robot_matcher.group(2));
						}
						//System.out.println(blueprint);
						blueprints.add(blueprint);
					}
				});
			return blueprints;
		}
		
		@Override
		public String toString() {
			return String.format("Blueprint %d:\n\tore robot: %s\n\tclay robot: %s\n\tobsidian robot: %s\n\tgeode robot: %s",
					number, robot[ORE], robot[CLAY], robot[OBSIDIAN], robot[GEODE]);
		}
	}
	

	record Robot (int[] cost) {

		public static Robot parse(String s) {
			int[] cost = new int[3];
			cost_matcher.reset(s);
			while(cost_matcher.find()) {
				cost[resource(cost_matcher.group(2))] = Integer.valueOf(cost_matcher.group(1));
			}
			return new Robot(cost);
		}		
		
		@Override
		public String toString() {
			return String.format("ore: %d, clay: %d, obsidian: %d", 
					cost[ORE], cost[CLAY], cost[OBSIDIAN]);
		}
	}

	private static int resource(String s) {
		return switch (s) {
		case "ore" -> ORE;
		case "clay" -> CLAY;
		case "obsidian" -> OBSIDIAN;
		case "geode" -> GEODE;
		default -> throw new IllegalArgumentException("Unexpected value: " + s);
		};
	}	
}
