package main;

import java.util.ArrayList;

/*
 * Strategy - only consider moves from AA to workable valve or between workable valves
 *            only consider moves to valve that is not already open
 *            
 * Precalculate move costs
 */


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import advent_code_common.FileUtility;
import advent_code_common.Pair;


public class Day16 {
	private static final String DAY16_INPUT_TXT = "./src/resources/day16_input.txt";
	private static final Pattern valve_pattern = Pattern.compile("Valve ([A-Z]+) has flow rate=(\\d+); (tunnel leads to valve|tunnels lead to valves) ([A-Z ,]+)");
	private static final Matcher valve_matcher = valve_pattern.matcher("");
	private static final int TOTAL_MINUTES = 30; // minutes to close all valves
	private static final int ELEPHANT_TRAINING = 4; // minutes to train an elephant
	private static final String FIRST_VALVE = "AA";
	private static List<String> workableValves = new ArrayList<>();
	private static Map<String, Integer> moveCost = new HashMap<>();
    private static Map<String, Valve> valves = new HashMap<>();
    private static Map<Integer,Integer> bestFlow = new HashMap<>();
    private static Set<String> history = new HashSet<>();
    
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 16");
		initialize(DAY16_INPUT_TXT);
		System.out.println("part 1: " + calculateMaximalFlow(FIRST_VALVE, 0, TOTAL_MINUTES, FIRST_VALVE));
		System.out.println("part 2: " + calculateMaximalFlow2(FIRST_VALVE, 0, TOTAL_MINUTES-ELEPHANT_TRAINING, FIRST_VALVE,
				FIRST_VALVE, 0, TOTAL_MINUTES-ELEPHANT_TRAINING, FIRST_VALVE));
		
	}

	private static void initialize(String filename) {
		for (var s : FileUtility.readListOfString(filename)) {
			Valve v = Valve.parse(s);
			valves.put(v.name, v);
			if (v.flowRate > 0) {
				workableValves.add(v.name);
			}
		}
		calculateMoveCost();
	}
	
	private static int calculateMaximalFlow(final String curValve, final int curFlow, final int curRemainingMinutes, final String state) {
		int maxFlow = curFlow;
		for (var v : workableValves) {
			if (!state.contains(v)) {
				if (moveCost.get(curValve + "," + v) + 1 < curRemainingMinutes) {
					int remainingMinutes = curRemainingMinutes - moveCost.get(curValve + "," + v) -1; 
					String newState = state + "," + v;
					maxFlow = Math.max(maxFlow, calculateMaximalFlow(v, curFlow + remainingMinutes*valves.get(v).flowRate, remainingMinutes, newState));
				}
			}
		}
		return maxFlow;
	}
	
	private static int calculateMaximalFlow2(final String curValve1, final int curFlow1, final int curRemainingMinutes1, final String state1,
			final String curValve2, final int curFlow2, final int curRemainingMinutes2, final String state2) {
		System.out.println(String.format("flows: %04d, States: %-20s %-20s", curFlow1+curFlow2, state1, state2));
		//System.out.println(bestFlow);
		int maxFlow = curFlow1 + curFlow2;
		Pair<Integer, Integer> p;
		for (var v : workableValves) {
			if (!state1.contains(v) && !state2.contains(v)) {
				if (moveCost.get(curValve1 + "," + v) + 1 < curRemainingMinutes1) {
					int remainingMinutes = curRemainingMinutes1 - moveCost.get(curValve1 + "," + v) -1; 
					int newFlow = curFlow1 + remainingMinutes*valves.get(v).flowRate;
					int prevBest = bestFlow.getOrDefault(remainingMinutes + curRemainingMinutes2,0);
					if ( newFlow + curFlow2 > prevBest) {
						bestFlow.put(remainingMinutes + curRemainingMinutes2, newFlow + curFlow2);
					}
					if (newFlow+curFlow2 > prevBest*0.8) {
						String newState = state1 + "," + v;
						if (!history.contains(newState+state2)) {
							history.add(newState+state2);
							history.add(state2+newState);
							maxFlow = Math.max(maxFlow, calculateMaximalFlow2(v, newFlow, remainingMinutes, newState,
									curValve2, curFlow2, curRemainingMinutes2, state2));
							
						}
					}	
				}
				if (moveCost.get(curValve2 + "," + v) + 1 < curRemainingMinutes2) {
					int remainingMinutes = curRemainingMinutes2 - moveCost.get(curValve2 + "," + v) -1; 
					int newFlow = curFlow2 + remainingMinutes*valves.get(v).flowRate;
					int prevBest = bestFlow.getOrDefault(remainingMinutes + curRemainingMinutes1,0);
					if (prevBest <= curFlow1 + newFlow) {
						bestFlow.put(remainingMinutes + curRemainingMinutes1, curFlow1 + newFlow);
					}
					if (curFlow1 + newFlow > prevBest*0.8) {
						String newState = state2 + "," + v;
						if (!history.contains(state1+newState)) {
							history.add(state1+newState);
							history.add(newState+state1);
							maxFlow = Math.max(maxFlow, calculateMaximalFlow2(curValve1, curFlow1, curRemainingMinutes1, state1,
									v, newFlow, remainingMinutes, newState));
						}
					}
				}
			}	
		}
		return maxFlow;
	}
	
	

    private static void calculateMoveCost() {
    	List<String> startValve = new ArrayList<>();
    	startValve.add(FIRST_VALVE);
    	startValve.addAll(workableValves);
    	for (var firstValve : startValve) {
    		findMinimalPath(firstValve,firstValve,0);
    	}
    	workableValves.sort((String v1, String v2) -> ((Integer)valves.get(v2).flowRate).compareTo((Integer)valves.get(v1).flowRate));
    }

	private static void findMinimalPath(final String firstValve, final String curValve, int cost) {
		Valve v = valves.get(curValve);
		for (var tunnel : v.tunnels) {
			String p = firstValve + "," + tunnel;
			if (!moveCost.containsKey(p)) {
				moveCost.put(p, cost+1);
				findMinimalPath(firstValve, tunnel, cost+1);
			}
			else if (moveCost.get(p) > cost+1) {
				moveCost.put(p, cost+1);
				findMinimalPath(firstValve, tunnel, cost+1);
			}
		}			
	}

	record Valve(String name, int flowRate, List<String> tunnels) {
		public static Valve parse(String s) {
			valve_matcher.reset(s);
			if (valve_matcher.find()) {
				return new Valve(valve_matcher.group(1),
						Integer.valueOf(valve_matcher.group(2)),
						Arrays.asList(valve_matcher.group(4).split(", ")));
			}
			return null;
		}
	}
	
}
