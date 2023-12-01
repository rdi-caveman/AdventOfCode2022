package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import advent_code_common.FileUtility;
import advent_code_common.Pair;

public class Day23 {
	private static final String DAY22_INPUT_TXT = "./src/resources/day23_input.txt";
	private static Map<String, Pair<Integer, Integer>> directions = new HashMap<>();
	private static Move[] moves = new Move[4];

	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 23");
		initializeDirections();
		initializeMoves();
		part1();
		part2();
	}
	
	private static void part1() {
		Set<Pair<Long, Long>> curElfLocations = readInitialElfPositions(DAY22_INPUT_TXT);  
		//printMap(0,curElfLocations);
		int firstMoveToConsider = 0;
		for (int round=1; round<=10; round++) {
			List<Pair<Pair<Long,Long>,Pair<Long,Long>>> validMoves = consider(curElfLocations, firstMoveToConsider);
			firstMoveToConsider = (firstMoveToConsider + 1) % moves.length;
			// apply valid moves
			for (var move : validMoves) {
				curElfLocations.remove(move.first);
				curElfLocations.add(move.second);
			}
			//printMap(round, curElfLocations);
		}
		long emptyTiles = calculateEmptyTiles(curElfLocations);
		System.out.println("Part 1: " + emptyTiles);
	}

	private static void part2() {
		Set<Pair<Long, Long>> curElfLocations = readInitialElfPositions(DAY22_INPUT_TXT);  
		//printMap(0,curElfLocations);
		int firstMoveToConsider = 0;
		boolean elvesStopped = false;
		int round = 0;
		while (!elvesStopped) {
			round++;
			List<Pair<Pair<Long,Long>,Pair<Long,Long>>> validMoves = consider(curElfLocations, firstMoveToConsider);
			elvesStopped = validMoves.isEmpty();
			firstMoveToConsider = (firstMoveToConsider + 1) % moves.length;
			// apply valid moves
			for (var move : validMoves) {
				curElfLocations.remove(move.first);
				curElfLocations.add(move.second);
			}
			//printMap(round, curElfLocations);
		}
		System.out.println("Part 2: " + round);
	}
	
	private static void printMap(Integer round, Set<Pair<Long, Long>> curElfLocations) {
		System.out.println("== End of Round " + round + " == number of elves " + curElfLocations.size());
		long minRow = 0;
		long maxRow = 0;
		long minCol = 0;
		long maxCol = 0;
		for (var loc : curElfLocations) {
			minRow = Math.min(minRow, loc.first);
			maxRow = Math.max(maxRow, loc.first);
			minCol = Math.min(minCol, loc.second);
			maxCol = Math.max(maxCol, loc.second);			
		}
		for (long row=minRow; row<=maxRow; row++) {
			for (long col=minCol; col<=maxCol; col++) {
				System.out.print(curElfLocations.contains(new Pair<Long,Long>(row,col)) ? "#" : ".");
			}
			System.out.println();
		}
	}

	private static long calculateEmptyTiles(Set<Pair<Long, Long>> curElfLocations) {
		long minRow = 0;
		long maxRow = 0;
		long minCol = 0;
		long maxCol = 0;
		for (var loc : curElfLocations) {
			minRow = Math.min(minRow, loc.first);
			maxRow = Math.max(maxRow, loc.first);
			minCol = Math.min(minCol, loc.second);
			maxCol = Math.max(maxCol, loc.second);			
		}
		return (maxRow - minRow + 1)*(maxCol - minCol + 1) - curElfLocations.size();
	}

	private static List<Pair<Pair<Long,Long>,Pair<Long,Long>>> consider(Set<Pair<Long, Long>> curElfLocations,
			int firstMoveToConsider) {
		List<Pair<Pair<Long,Long>,Pair<Long,Long>>> returnMoves = new ArrayList<>();
		Map<Pair<Long,Long>,Integer> collision = new HashMap<>();
		Pair<Long, Long> newLoc;
		/*
		 *  During the first half of each round, each Elf considers the eight positions adjacent to themself. 
		 *  If no other Elves are in one of those eight positions, the Elf does not do anything during this round. 
		 *  Otherwise, the Elf looks in each of four directions in the following order 
		 *  and proposes moving one step in the first valid direction
		 */
		for (var loc : curElfLocations) {
			if (hasAdjacentElf(curElfLocations, loc)) {
				// elves with no adjacent elves don't need to move
				newLoc = proposeMove(curElfLocations, loc, firstMoveToConsider);
				if (newLoc != null) {
					returnMoves.add(new Pair<Pair<Long,Long>,Pair<Long,Long>>(loc, newLoc));
					collision.put(newLoc, collision.getOrDefault(newLoc,0)+1);
				}
			}
		}
		/*
		 * Simultaneously, each Elf moves to their proposed destination tile 
		 * if they were the only Elf to propose moving to that position. 
		 * If two or more Elves propose moving to the same position, none of those Elves move.
		 */
		return returnMoves.stream()
				.filter(move -> collision.get(move.second) <= 1) // proposed newLoc is not a collision
				.toList();
	}

	private static boolean hasAdjacentElf(Set<Pair<Long, Long>> curElfLocations, Pair<Long, Long> loc) {
		for (var dir : directions.values()) {
			if (curElfLocations.contains(new Pair<Long,Long>(loc.first+dir.first, loc.second+dir.second))) {
				return true;
			}
		}
		return false;
	}
	
	private static Pair<Long,Long> proposeMove (Set<Pair<Long, Long>> curElfLocations, Pair<Long, Long> loc,
			int firstMoveToConsider) {
		int m;
		moves: for (int i=0; i<moves.length; i++) {
			m = (firstMoveToConsider + i) % moves.length;
			for (int j=0; j<3; j++) {
				if (curElfLocations.contains(new Pair<Long,Long>(loc.first + directions.get(moves[m].check[j]).first, 
						loc.second + directions.get(moves[m].check[j]).second))) {
					continue moves;
				}
 			}
			return new Pair<Long,Long>(loc.first + directions.get(moves[m].move).first,
					loc.second + directions.get(moves[m].move).second);			
		}
		return null;
	}
	
	private static Set<Pair<Long, Long>> readInitialElfPositions(String filename) {
		Set<Pair<Long, Long>> elves = new HashSet<>();
		List<String> input = FileUtility.readListOfString(filename);
		final int width = input.get(0).length(); 
		IntStream.range(0, input.size()).forEach( row -> {
			IntStream.range(0,width).forEach( col -> {
				if (input.get(row).charAt(col) == '#') {
					elves.add(new Pair<Long, Long>((long) row, (long) col));
				}
			});
		});
		return elves; 
	}

	private static void initializeDirections() {
		directions.put("N", new Pair<Integer, Integer>(-1,0));
		directions.put("NE", new Pair<Integer, Integer>(-1,1));
		directions.put("E", new Pair<Integer, Integer>(0,1));
		directions.put("SE", new Pair<Integer, Integer>(1,1));
		directions.put("S", new Pair<Integer, Integer>(1,0));
		directions.put("SW", new Pair<Integer, Integer>(1,-1));
		directions.put("W", new Pair<Integer, Integer>(0,-1));
		directions.put("NW", new Pair<Integer, Integer>(-1,-1));
	}
	
	private static void initializeMoves() {
		moves[0] = new Move(new String[] {"N", "NE", "NW"}, "N");
		moves[1] = new Move(new String[] {"S", "SE", "SW"}, "S");
		moves[2] = new Move(new String[] {"W", "NW", "SW"}, "W");
		moves[3] = new Move(new String[] {"E", "NE", "SE"}, "E");
	}
	
	record Move (String[] check, String move) {} 
}
