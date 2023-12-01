package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import advent_code_common.FileUtility;

public class Day5 {
	final static String DAY5_INPUT_TXT = "./src/resources/day5_input.txt";
	final static Pattern MOVE_PATTERN = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
	final static Matcher MOVE_MATCHER = MOVE_PATTERN.matcher("");
	final static Pattern CRATE_PATTERN = Pattern.compile("\\[(.)\\]");
	final static Matcher CRATE_MATCHER = CRATE_PATTERN.matcher("");
	// cargo stacks
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 5");
		Map<Integer, Stack<String>> crates = parseCrates(DAY5_INPUT_TXT);
		final List<Move> moves = parseMoves(DAY5_INPUT_TXT);
		part1(crates, moves);
		// reinitialize crates
		crates = parseCrates(DAY5_INPUT_TXT);
		part2(crates, moves);		
		
	}
	
	private static void part1(Map<Integer, Stack<String>> crates, List<Move> moves) {
		moves.stream().forEach(m ->{
			for (int i=0; i<m.qty; i++) {
				String box = crates.get(m.src).pop();
				crates.get(m.dst).push(box);
			}
		}); 
		String topCrates = "";
		for (int i=1; i<=crates.size(); i++) {
			topCrates += crates.get(i).pop();
		}
		System.out.println("part 1: " + topCrates);
	}
	
	private static void part2(Map<Integer, Stack<String>> crates, List<Move> moves) {
		// create a temp stack
		Stack<String> temp = new Stack<>();
		moves.stream().forEach(m ->{
			for (int i=0; i<m.qty; i++) {
				String box = crates.get(m.src).pop();
				temp.push(box);
			}
			for (int i=0; i<m.qty; i++) {
				String box = temp.pop();
				crates.get(m.dst).push(box);
			}
		}); 
		String topCrates = "";
		for (int i=1; i<=crates.size(); i++) {
			topCrates += crates.get(i).pop();
		}
		System.out.println("part 2: " + topCrates);
	}

	private static List<Move> parseMoves(String filename) {
		List<Move> moves = new ArrayList<>();
		FileUtility.readListOfString(filename).stream()
				.filter(s -> s.contains("move"))
				.forEach(s -> {
					MOVE_MATCHER.reset(s);
					if (MOVE_MATCHER.matches()) {
						moves.add(new Move(Integer.valueOf(MOVE_MATCHER.group(1)),
								Integer.valueOf(MOVE_MATCHER.group(2)), 
								Integer.valueOf(MOVE_MATCHER.group(3))));
					}	
				});
				
		return moves;
	}

	private static Map<Integer, Stack<String>> parseCrates(String filename) {
		Map<Integer, Stack<String>> supplies = new HashMap<>();
		List<String> crates = FileUtility.readListOfString(filename).stream()
				.filter(s -> s.contains("[") || !(s.isEmpty() || s.contains("move")))
				.toList();
		int maxCrate = Arrays.stream(crates.get(crates.size()-1).split(" +"))
				.filter(s -> s != null && !s.isEmpty())
				.map(s -> Integer.valueOf(s))
				.max(Integer::compare).orElse(0);
		for (int i=1; i<=maxCrate; i++) {
			supplies.put(i, new Stack<>());
		}
		for(int i=crates.size()-2; i>=0; i--) {
			for (int j=0; j<maxCrate; j++) {
				CRATE_MATCHER.reset(crates.get(i).substring(j*4, j*4+3));
				if (CRATE_MATCHER.matches()) {
					supplies.get(j+1).push(CRATE_MATCHER.group(1));
				}
			}
		}
		//System.out.println(supplies);
		return supplies;
	}

	public record Move(int qty, int src, int dst) {}
}
