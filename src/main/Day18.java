package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import advent_code_common.FileUtility;

public class Day18 {
	private static String DAY18_INPUT_TXT = "./src/resources/day18_input.txt";
	private static List<Cube> neighbors = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 18");
		Map<Cube,Integer> cubes = 
				FileUtility.readListOfString(DAY18_INPUT_TXT).stream()
				.map(s -> s.split(","))
				.map(a -> new Cube(Integer.valueOf(a[0]),
						Integer.valueOf(a[1]),
						Integer .valueOf(a[2])))
				.collect(Collectors.toMap(c -> c, c -> 6));
		initializeNeighbors();		
		part1(cubes);
		part2(cubes);
	}

	private static void part2(Map<Cube, Integer> cubes) {
		// find surface area, ignoring interior cavities.
		// find min/max x,y and z.  That defines extent of object
		//      expand out one cube in every direction so objecr is entirely enveloped
		// 		fill in cube from outside in
		//		entire surface area of outer cube set 
		//      minus outer surface area = outer surface area
		//      of inner shape
		Map<Cube,Integer> outerCubes = new HashMap<>();
		List<Cube> interiorCubes = new ArrayList<>();
		Cube min = cubes.keySet().stream().reduce((c1,c2) -> c1.min(c2)).orElse(null);
		Cube max = cubes.keySet().stream().reduce((c1,c2) -> c1.max(c2)).orElse(null);
		min = new Cube(min.x-1, min.y-1, min.z-1); // define min/max so there is a one cube continuous shell
		max = new Cube(max.x+1, max.y+1, max.z+1);
		for (int x=min.x; x<=max.x; x++) {
			for (int y=min.y; y<=max.y; y++) {
				for (int z=min.z; z<=max.z; z++) {
					Cube test = new Cube(x,y,z);
					if (cubes.containsKey(test)) {
						continue;
					}
					if (outerCubes.isEmpty() || test.adjacentTo(outerCubes)) {
						outerCubes.put(test,6);
						continue;
					}
					interiorCubes.add(test);					
				}
			}
		}
		// find any cubes misidentified as interior
		boolean allExteriorCubesFound = false;
		while(!allExteriorCubesFound) {
			List<Cube> misClassed = findAdditionalExteriorCubes(outerCubes, interiorCubes);
			allExteriorCubesFound = misClassed.isEmpty();
			for (var cube : misClassed) {
				interiorCubes.remove(cube);
			}
		}	
		//System.out.println(outerCubes);
		Long surfaceArea = findNetSurfaceArea(outerCubes, min, max);
		System.out.println("part 2: " + surfaceArea);
		
	}

	private static List<Cube> findAdditionalExteriorCubes(Map<Cube, Integer> outerCubes, List<Cube> interiorCubes) {
		List<Cube> misClassified = new ArrayList<>();
		for (var cube : interiorCubes) {
			if (cube.adjacentTo(outerCubes)) {
				outerCubes.put(cube,6);
				misClassified.add(cube);
				//System.out.println("misClassified " + cube);
			}
		}
		return misClassified;
	}

	private static long findNetSurfaceArea(Map<Cube, Integer> outerCubes, Cube min,
			Cube max) {
		// find net surface area
		for (var cube : outerCubes.keySet()) {
			int faces = 6;
			for (var neighbor : neighbors) {
				if (outerCubes.containsKey(cube.plus(neighbor))) {
					faces-=1;
				}
			}
			faces -= (cube.x == min.x || cube.x == max.x) ? 1 : 0;
			faces -= (cube.y == min.y || cube.y == max.y) ? 1 : 0;
			faces -= (cube.z == min.z || cube.z == max.z) ? 1 : 0;
			outerCubes.put(cube,faces);
		}
		long surfaceArea = outerCubes.values().stream()
				.mapToLong(i -> i)
				.sum();
		return surfaceArea;
	}

	private static void part1(Map<Cube,Integer> cubes) {
		// find surface area
		for (var cube : cubes.keySet()) {
			for (var neighbor : neighbors) {
				if (cubes.containsKey(cube.plus(neighbor))) {
					cubes.put(cube,cubes.get(cube)-1);
				}
			}
		}
		long surfaceArea = cubes.values().stream()
				.mapToLong(i -> i)
				.sum();
		System.out.println("part 1: " + surfaceArea);
	}

	private static void initializeNeighbors() {
		neighbors.add(new Cube(-1,0,0));
		neighbors.add(new Cube(1,0,0));
		neighbors.add(new Cube(0,-1,0));
		neighbors.add(new Cube(0,1,0));
		neighbors.add(new Cube(0,0,-1));
		neighbors.add(new Cube(0,0,1));		
	}

	record Cube (int x, int y, int z) {
		public Cube plus(Cube c) {
			return new Cube(x+c.x, y+c.y, z+c.z);
		}
		public boolean adjacentTo(Map<Cube, Integer> outerCubes) {
			for (var neighbor : neighbors) {
				if (outerCubes.containsKey(this.plus(neighbor))) {
					return true;
				}
			}
			return false;
		}
		public Cube min(Cube c) {
			return new Cube(Math.min(x,c.x),Math.min(y,c.y),Math.min(z,c.z));
		}
		public Cube max(Cube c) {
			return new Cube(Math.max(x,c.x),Math.max(y,c.y),Math.max(z,c.z));
		}
	}
	
	
}
