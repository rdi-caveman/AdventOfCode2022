package main;

import java.util.ArrayList;
import java.util.List;

import advent_code_common.FileUtility;

public class Day12 {
	static final String DAY12_INPUT_TXT = "./src/resources/day12_input.txt";
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 12");
		char[][] map = FileUtility.readCharArray(DAY12_INPUT_TXT);
		part1(map);
		part2(map);
	}

	private static void part1(char[][] map) {
		GridPoint start = (find('S', map));
		GridPoint end = find('E', map);
		HillSolver solver = new HillSolver();
		int steps = solver.solve(map, start.row, start.col, end.row, end.col);
		System.out.println("part 1: " + steps);
	}
	
	private static void part2(char[][] map) {
		List<GridPoint> startList = findAll('a', map);
		startList.add(find('S', map));
		GridPoint end = find('E', map);
		HillSolver solver = new HillSolver();
		int minSteps = startList.stream()
			.mapToInt(gp -> solver.solve(map, gp.row, gp.col, end.row, end.col))
			.min()
			.orElse(-1);
		System.out.println("part 2: " + minSteps);
	}
	
	private static List<GridPoint> findAll(char c, char[][] map) {
		List<GridPoint> startList = new ArrayList<>();
		for (int row=0; row<map.length; row++) {
			for (int col=0; col< map[0].length; col++) {
				if (map[row][col] == c) {
					startList.add(new GridPoint(row,col));
				}
			}
		}
		return startList;
	}

	private static GridPoint find(char c, char[][] map) {
		for (int row=0; row<map.length; row++) {
			for (int col=0; col< map[0].length; col++) {
				if (map[row][col] == c) {
					return new GridPoint(row,col);
				}
			}
		}
		return null;
	}
	
	record GridPoint (int row, int col ) {}

}
