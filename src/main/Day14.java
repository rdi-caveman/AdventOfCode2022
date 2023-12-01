package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent_code_common.FileUtility;
import advent_code_common.Point;


public class Day14 {
	static final String DAY14_INPUT_TXT = "./src/resources/day14_input.txt";
	static final int AIR = 0;
	static final int ROCK = 1;
	static final int SAND = 2;

	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 14");
		Map<Point,Integer> puzzle = initialize(DAY14_INPUT_TXT);
		part1(puzzle);
		part2(puzzle);
	}

	private static void part1(Map<Point, Integer> puzzle) {
		int minX = puzzle.keySet().stream()
				.mapToInt(p -> p.x)
				.min()
				.orElse(0);
		int maxX = puzzle.keySet().stream()
				.mapToInt(p -> p.x)
				.max()
				.orElse(0);
		int maxY = puzzle.keySet().stream()
				.mapToInt(p -> p.y)
				.max()
				.orElse(0);	
		boolean filled = false;
		while (!filled) {
			//System.out.println("drop sand");
			filled = dropSand(new Point(500,0), puzzle, minX, maxX, maxY);
		}
		long sandCount = puzzle.entrySet().stream()
				.filter(p -> p.getValue() == SAND)
				.count();
		System.out.println("part 1: " + sandCount);
	}

	private static void part2(Map<Point, Integer> puzzle) {
		int maxY = puzzle.keySet().stream()
				.mapToInt(p -> p.y)
				.max()
				.orElse(0);	
		boolean filled = false;
		while (!filled) {
			//System.out.println("drop sand");
			filled = dropSand(new Point(500,0), puzzle, null, null, maxY);
		}
		long sandCount = puzzle.entrySet().stream()
				.filter(p -> p.getValue() == SAND)
				.count();
		System.out.println("part 2: " + sandCount);
	}
	
	private static boolean dropSand(Point point, Map<Point, Integer> puzzle, Integer minX, Integer maxX, Integer maxY) {
		boolean atRest = false;
		while (!atRest) {
			//System.out.println(point);
			if (maxY !=null && point.y == maxY+1) {
				// infinite width, have hit the infinite floor
				atRest = true;
			}
			else if (puzzle.getOrDefault(new Point(point.x, point.y+1),AIR) == AIR) {
				point.y += 1;
			}
			else if (puzzle.getOrDefault(new Point(point.x-1, point.y+1),AIR) == AIR) {
				point.x -=1;
				point.y +=1;
			}
			else if (puzzle.getOrDefault(new Point(point.x+1, point.y+1),AIR) == AIR) {
				point.x +=1;
				point.y +=1;
			}
			else {
				atRest = true;
			}
			if  (point.y == 0) {
				// full to top
				puzzle.put(point, SAND);
				return true;
			}
			if ((minX != null) && (point.x < minX || point.x > maxX || point.y > maxY)) {
				// finite width and sand is falling into abyss
				return true;
			}
		}
		puzzle.put(point, SAND);
		//System.out.println("Sand came to rest at " + point);
		return false;
	}

	
	private static Map<Point, Integer> initialize(String filename) {
		List<List<Point>> puzzleInput = FileUtility.readListOfString(filename).stream()
				.map(s -> parseInputList(s))
				.toList();
		Map<Point,Integer> puzzle = new HashMap<>();
		for (List<Point> points : puzzleInput) {
			for (int i=1; i<points.size(); i++) {
				fill(puzzle, points.get(i-1), points.get(i), ROCK);
			}
		}
		return puzzle;
	}
	
	private static void fill(Map<Point,Integer> puzzle, Point p1, Point p2, int i) {
		if (p1.x == p2.x) {
			//fill vertical
			for (int y=Math.min(p1.y,p2.y); y<=Math.max(p1.y,p2.y); y++) {
				puzzle.put(new Point(p1.x, y), ROCK);
			}
		}
		else {
			// fill horizontal
			for (int x=Math.min(p1.x,p2.x); x<=Math.max(p1.x,p2.x); x++) {
				puzzle.put(new Point(x, p1.y), ROCK);
			}
		}
		
	}

	private static List<Point> parseInputList(String input) {
		return Arrays.asList(input.split(" -> ")).stream()
				.map(s -> new Point(Integer.valueOf(s.split(",")[0]),
						Integer.valueOf(s.split(",")[1])))
				.toList();
	}

}
