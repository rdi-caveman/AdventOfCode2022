package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import advent_code_common.FileUtility;
import advent_code_common.Pair;
import advent_code_common.Triple;

public class day22 {
	private static final char STONE = '#';
	private static final char SPACE = ' ';
	private final static String DAY22_INPUT_TXT = "./src/resources/day22_input.txt";
	private static final String LINE_DELIMITER = "\\r?\\n";
	private static final String RECORD_DELIMITER = LINE_DELIMITER + LINE_DELIMITER;
	private final static Pattern move_pattern = Pattern.compile("(\\d+|[RL])");
	private final static Matcher move_matcher = move_pattern.matcher("");
	private final static int RIGHT = 0;
	private final static int DOWN = 1;
	private final static int LEFT = 2;
	private final static int UP = 3;
	private final static char[] path = new char[] {'>','v','<','^'};
	
	private static int curRow;
	private static int curCol;
	private static int curDirection;
	private static int curFace;
	private static StringBuffer[] map;
	private static int maxWidth;
	private static int[] firstCol;
	private static int[] lastCol;
	private static int[] firstRow;
	private static int[] lastRow;
	
	public static void main(String[] args) {
		List<String> input = FileUtility.readRecords(DAY22_INPUT_TXT,RECORD_DELIMITER);
		parseMap(input.get(0));
		//List<String> moves = parseDirections(input.get(1));
		List<String> moves = Arrays.asList(new String[] {"7", "L", "36"});
		//part1(moves);
		part2(moves);
	}
	
	private static void part1(List<String> moves) {
		int password = findPassword(moves, false);
		System.out.println("part 1: " + password);
	}

	private static void part2(List<String> moves) {
		int password = findPassword(moves, true);
		System.out.println("part 2: " + password);
	}

	private static int findPassword(List<String> moves, boolean threeD) {
		// vertical top 0 to bottom, horizontal left 0 to right
		// find starting location
		curRow = 0;
		curCol = map[0].indexOf(".");
		curDirection = RIGHT;
		map[curRow].setCharAt(curCol, path[curDirection]);
		curFace = face(curRow, curCol);
		for (var move : moves) {
			if (move.equals("R") || move.equals("L")) {
				turn(move);
			}
			else {
				move(Integer.valueOf(move), threeD);
			}
			printMap(move);
		}
		int password = 1000 * (curRow+1) + 4 * (curCol+1) + curDirection;
		return password;
	}
	
	private static void move(int distance, boolean threeD) {
		int distTravelled = 0;
		while (distTravelled < distance && legalMove(threeD)) {
			moveOne(threeD);
			distTravelled++;
		}		
	}

	
	private static void moveOne(boolean threeD) {
		Triple<Integer, Integer, Integer> dest = nextSpace(threeD);
		curRow = dest.first;
		curCol = dest.second;
		curDirection = dest.third;
		map[curRow].setCharAt(curCol, path[curDirection]);
		curFace = face(curRow,curCol);
	}
	
	private static boolean legalMove(boolean threeD) {
		Triple<Integer, Integer, Integer> dest = nextSpace(threeD);
		return map[dest.first].charAt(dest.second) != STONE; 		
	}
		
	private static Triple<Integer, Integer, Integer> nextSpace(boolean threeD) {
		if (!threeD) {
			Pair<Integer, Integer> dest = nextSpace2d();
			return new Triple(dest.first, dest.second, curDirection);
		}
		int newCol = curCol;
		int newRow = curRow;
		int newDirection = curDirection;
		switch(curDirection) {
		case RIGHT:
			if (curCol < lastCol[curRow]) { 
				newCol++; 
				}
			else {
				switch(curFace) {
				case 6:
					newDirection = LEFT;
					newRow = 149-curRow; // row 0..49 maps to row 149..100
					newCol = 99;
					break;
				case 4:
					newDirection = UP;
					newRow = 49;
					newCol = curRow + 50; // row 50.99 maps to col 100.149
					break;
				case 5:
					newDirection = LEFT;
					newRow = 149 - curRow; // row 100..149 maps to row 49..00
					newCol = 149;
					break;
				case 2:
					newDirection = UP;
					newRow = 149;
					newCol = curRow-100; // row 150-199 maps to col 50-99
					break;
				}
			}	
			break;
		case LEFT:
			if (curCol > firstCol[curRow]) {
				newCol--;
			}
			else {
				switch(curFace) {
				case 1:
					newDirection = RIGHT;
					newRow = 149-curRow; // row 0..49 maps to row 149..100
					newCol = 0;			
					break;
				case 4:
					newDirection = DOWN;
					curRow = 100;
					curCol = curRow - 50; // row 50..99 map to col 0..49
					break;
				case 3:
					newDirection = RIGHT;
					newRow = 149 - curRow; // row 100..149 map to row 49..0
					newCol = 50;
					break;
				case 2:
					newDirection = DOWN;
					newRow = 0;
					newCol = curRow - 100 ; // row 150..199 map to col 50..99
					break;
				}
			}
			break;
		case UP:
			if (curRow > firstRow[curCol] && curCol >= firstCol[curRow-1] && curCol <= lastCol[curRow-1]) {
				newRow--;
			}
			else {
				switch (curFace) {
				case 1:
					newDirection = RIGHT;
					newRow = curCol + 100; // col 50..99 map to row 150..199
					newCol = 0;
					break;
				case 6: 
					newDirection = UP;
					newRow = 199;
					newCol = curCol - 100; // col 100..149 map to 0..49
					break;
				case 3:
					newDirection = RIGHT;
					newRow = curCol + 50; // col 0..49 map to row 50..99
					newCol = 50;
					break;
				}
			}
			break;
		case DOWN:
			if (curRow < lastRow[curCol] && curCol >= firstCol[curRow+1] && curCol <= lastCol[curRow+1]) {
				newRow++;
			}
			else {
				switch (curFace) {
				case 6:
					newDirection = LEFT;
					newRow = curCol - 50; // col 100..149 map to row 50..99
					newCol = 99;
					break;
				case 5:
					newDirection = LEFT;
					newRow = curCol + 100; // col 50..99 map to row 150..199
					newCol = 49;
					break;
				case 2: 
					newDirection = DOWN;
					newRow = 0;
					newCol = curCol + 100; // col 0..49 map to col 100.. 149 							
					break;
				}
			}
			break;
		}	
		return new Triple(newRow, newCol, newDirection);
	}

	private static Pair<Integer, Integer> nextSpace2d() {
		int newCol = curCol;
		int newRow = curRow;
		switch(curDirection) {
		case RIGHT:
			if (curCol < lastCol[curRow]) newCol++;
			else newCol = firstCol[curRow];
			break;
		case LEFT:
			if (curCol > firstCol[curRow]) newCol--;
			else newCol = lastCol[curRow];
			break;
		case UP:
			if (curRow > firstRow[curCol] && curCol >= firstCol[curRow-1] && curCol <= lastCol[curRow-1]) newRow--;
			else newRow = lastRow[curCol];
			break;
		case DOWN:
			if (curRow < lastRow[curCol] && curCol >= firstCol[curRow+1] && curCol <= lastCol[curRow+1]) newRow++;
			else newRow = firstRow[curCol];
			break;
		}	
		return new Pair<Integer, Integer>(newRow, newCol);
	}

	private static int topRow(int col) {
		for (int i=0; i<map.length; i++) {
			if (col < map[i].length() && map[i].charAt(col) != SPACE) {
				return i;
			}
		}
		throw new RuntimeException("map exception. no row with this column found");
	}

	private static int bottomRow(int col) {
		for (int i=map.length-1; i>=0; i--) {
			if (col < map[i].length() && map[i].charAt(col) != SPACE) {
				return i;
			}
		}
		throw new RuntimeException("map exception. no row with this column found");
	}
	
	private static int findFirstNonBlank(StringBuffer sb) {
		for (int i=0; i<sb.length(); i++) {
			if (sb.charAt(i) != SPACE) return i;
		}
		throw new RuntimeException("map exception.  no non blank character found.");
		
	}

	private static void turn(String turn) {
		curDirection = (curDirection + 4 + (turn.equals("R") ? 1 : -1)) % 4;	
		map[curRow].setCharAt(curCol, path[curDirection]);
	}

	private static StringBuffer[] parseMap(String s) {
		String[] input = s.split(LINE_DELIMITER);
		map = new StringBuffer[input.length];
		firstCol = new int[input.length];
		lastCol = new int[input.length];
		for (int i=0; i<input.length; i++) {
			map[i] = new StringBuffer(input[i]);
			lastCol[i] = map[i].length()-1;
			firstCol[i] = findFirstNonBlank(map[i]);
			maxWidth = Math.max(maxWidth, lastCol[i]);
		}
		firstRow = new int[maxWidth+1];
		lastRow = new int [maxWidth+1];
		for (int i=0; i<=maxWidth; i++) {
			firstRow[i] = topRow(i);
			lastRow[i] = bottomRow(i);
		}
		return map;
	}

	private static List<String> parseDirections(String s) {
		List<String> moves = new ArrayList<>();
		move_matcher.reset(s);
		while(move_matcher.find()) {
			moves.add(move_matcher.group(1));
		}
		return moves;
	}

	private static int face(int row, int col) {
		/*
		 *   [ ][1][6]
		 *   [ ][4][ ]
		 *   [3][5][ ]
		 *   [2][ ][ ]
		 */
		int bigRow = row / 50;
		int bigCol = col / 50;
		return switch(bigRow*10+bigCol) {
			case 01 -> 1;
			case 02 -> 6;
			case 11 -> 4;
			case 20 -> 3;
			case 21 -> 5;
			case 30 -> 2;
		default -> throw new IllegalArgumentException("Unexpected value: " + bigRow*10+bigCol);
		};
	}
	
	private static void printMap(String move) {
		System.out.println("\n\n" + move);
		for (int i=0; i<map.length; i++) {
			System.out.println(map[i]);
		}
	}
	
	record Move(int distance, String turn) {}
}
