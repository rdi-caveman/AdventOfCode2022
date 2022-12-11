package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day9 {
	final static String DAY9_INPUT_TXT = "./src/resources/day9_input.txt";
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 9");
		List<Move> moves = new ArrayList<>();
		FileUtility.readListOfString(DAY9_INPUT_TXT).stream()
			.forEach(s -> {
				String[] m = s.split(" ");
				moves.add(new Move(m[0],Integer.valueOf(m[1])));
			});
		part1(moves);
		part2(moves);
	}
	
	private static void part1(List<Move> moves) {
		// head and tails start at 0,0
		Point head = new Point(0,0);
		List<Point> tail = new ArrayList<>();
		tail.add(new Point(0,0));
		Map<String, Integer> tailPositions = new HashMap<>();
		tailPositions.put(tail.get(tail.size()-1).toString(), 1);
		// apply moves
		moves.stream().forEach(m -> applyMove(m, head, tail, tailPositions));
		System.out.println("part 1: " + tailPositions.size());
	}

	private static void part2(List<Move> moves) {
		// head and tails start at 0,0
		Point head = new Point(0,0);
		List<Point> tail = new ArrayList<>();
		for (int i=0; i<9; i++) {
			tail.add(new Point(0,0));
		}	
		Map<String, Integer> tailPositions = new HashMap<>();
		tailPositions.put(tail.get(tail.size()-1).toString(), 1);
		// apply moves
		moves.stream().forEach(m -> applyMove(m, head, tail, tailPositions));
		System.out.println("part 2: " + tailPositions.size());
	}

	
	private static void applyMove(Move move, Point head, List<Point> tail, Map<String, Integer> tailPositions) {
		for(int i=0; i<move.distance; i++) {
			moveHead(move.direction, head);
			moveTail(head, tail.get(0));
			for (int j=1; j<tail.size(); j++) {
				moveTail(tail.get(j-1), tail.get(j));
			}
			tailPositions.put(tail.get(tail.size()-1).toString(), tailPositions.getOrDefault(tail.size()-1,0)+1);
			//System.out.println(tail.get(tail.size()-1).toString());
		}
	}

	private static void moveTail(Point head, Point tail) {
		// If the head is ever two steps directly up, down, left, or right from the tail, 
		// the tail must also move one step in that direction so it remains close enough:
		if (head.x == tail.x && head.y-tail.y >= 2) {
			tail.y += 1;
		}
		if (head.x == tail.x && head.y-tail.y <= -2) {
			tail.y -= 1;
		}
		else if (head.y == tail.y && head.x-tail.x >= 2) {
			tail.x += 1;
		}
		else if (head.y == tail.y && head.x-tail.x <= -2) {
			tail.x -= 1;
		}
		// Otherwise, if the head and tail aren't touching and aren't in the same row or column, 
		// the tail always moves one step diagonally to keep up:
		else if (head.x != tail.x && head.y != tail.y && Math.abs(head.x - tail.x) + Math.abs(head.y - tail.y) > 2) {
			// need to move diagonally
			tail.x += (head.x>tail.x) ? 1 : -1;
			tail.y += (head.y>tail.y) ? 1 : -1;
		}
		
	}

	private static void moveHead(String direction, Point head) {
		switch (direction) {
		case "U": 
			head.y -= 1;
			break;
		case "D":
			head.y += 1;
			break;
		case "L": 
			head.x -= 1;
			break;
		case "R":
			head.x += 1;
			break;
		}
	}

	public record Move(String direction, int distance) {}
}
