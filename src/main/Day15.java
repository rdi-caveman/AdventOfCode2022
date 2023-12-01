package main;

import java.util.List;

import advent_code_common.FileUtility;

public class Day15 {
	static final String DAY15_INPUT_TXT = "./src/resources/day15_input.txt";
	static final int PART1_INPUT = 2000000;
	static final int PART2_INPUT = 4000000;
	//static final String DAY15_INPUT_TXT = "./src/resources/day15_sample.txt";
	//static final int PART1_INPUT = 10;
	//static final int PART2_INPUT = 20;
	static final long X_MULTIPLIER = 4000000;
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 15");
		List<Sensor> sensors = FileUtility.readListOfString(DAY15_INPUT_TXT).stream()
				.map(s -> new Sensor(s))
				.toList();
		part1(sensors);	
		part2(sensors);
	}

	private static void part2(List<Sensor> sensors) {
		long result=0;
		Integer x;
		for (int y =0; y<PART2_INPUT; y++) {
			x = findGap(sensors, y);
			if (x != null) {
				// found our missing beacon a x,y
				result = x * X_MULTIPLIER + y;
				break;
			}
		}
		System.out.println("part 2: " + result);
	}
	
	private static void part1(List<Sensor> sensors) {
		int sum = getCoverage(sensors, PART1_INPUT);
		System.out.println("part 1: " + sum);
	}

	private static int getCoverage(List<Sensor> sensors, int y) {
		// get list of segments on y
		List<Segment> coverageList = getSegments(sensors, y);
		int sum = 0;
		int min = coverageList.get(0).begin;
		int max = coverageList.get(0).end;
		for (int i=1; i<coverageList.size(); i++) {
			if (coverageList.get(i).begin <= max+1) {
				// combine segments
				max = Math.max(coverageList.get(i).end, max);
			}
			else {
				// found a gap - start new segment
				sum += max-min;
				min = coverageList.get(i).begin;
				max = coverageList.get(i).end;
			}
		}
		// total coverage of all segments
		sum += max-min;
		return sum;
	}
	
	private static Integer findGap(List<Sensor> sensors, int y) {
		// get list of segments on y
		List<Segment> coverageList = getSegments(sensors, y);
		int max = coverageList.get(0).end;
		for (int i=1; i<coverageList.size(); i++) {
			if (coverageList.get(i).begin <= max+1) {
				max = Math.max(coverageList.get(i).end, max);
			}
			else {
				// found gap - return x location on y
				return max+1;
			}
		}
		return null; // no gap found
	}

	private static List<Segment> getSegments(List<Sensor> sensors, int y) {
		List<Segment> coverageList = sensors.stream()
				.map(s -> Segment.coverage(s, y))
				.filter(c -> c != null)
				.sorted()
				.toList();
		return coverageList;
	}
	
}
