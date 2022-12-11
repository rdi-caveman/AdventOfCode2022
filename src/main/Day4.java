package main;

import java.util.List;

public class Day4 {
	static final String DAY4_INPUT_TXT = "./src/resources/day4_input.txt";

	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 4");
		List<Assignment> assignments = parseInput(DAY4_INPUT_TXT);
		part1(assignments);
		part2(assignments);

	}

	private static void part1(List<Assignment> assignments) {
		long overlaps = assignments.stream()
				.filter(a -> a.r1.contains(a.r2) || a.r2.contains(a.r1)) 
				.count();
		System.out.println("part 1: " + overlaps);
	}
	
	private static void part2(List<Assignment> assignments) {
		long overlaps = assignments.stream()
				.filter(a -> !a.r1.disjoint(a.r2)) 
				.count();
		System.out.println("part 2: " + overlaps);
	}
	
	private static List<Assignment> parseInput(String filename) {
		return FileUtility.readListOfString(filename).stream()
				.map(s -> s.split(","))
				.map(a -> new Assignment(stringToRange(a[0]),stringToRange(a[1])))
				.toList();
	}


	private static Range stringToRange(String s) {
		String[] a = s.split("-");
		return new Range(Integer.valueOf(a[0]),Integer.valueOf(a[1]));
	}


	public record Assignment(Range r1, Range r2) {}
	
	public record Range(int low, int high) {
		public boolean contains(Range r) {
			return this.low <= r.low && this.high >= r.high; 
		}
		public boolean disjoint(Range r) {
			return this.high < r.low || this.low > r.high;
		}
	};
	
}
