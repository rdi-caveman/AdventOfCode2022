package main;

/*
 *  arrays are [row][col}
 * 
 *        3 
 *        2
 *        1
 *  Floor 0 -------
 *          0123456
 */

import java.util.ArrayList;
import java.util.List;

import advent_code_common.FileUtility;

public class Day17 {
	private static final String DAY17_INPUT_TXT = "./src/resources/day17_input.txt";
	private static List<int[][]> shapes;

	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 17");
		String hotGas = FileUtility.readEntireFile(DAY17_INPUT_TXT);
		shapes = initializeShapes();
		part1(hotGas);
		part2(hotGas);
	}

	private static void part1(String hotGas) {
		int[][] game = new int[4000][7];
		int maxHeight = simulate(game, hotGas, 2022);
		System.out.println("Part 1: " + maxHeight);
	}
	
	private static void part2(String hotGas) {
		long shapesPerCycle = 3434 - 1709;
		long heightPerCycle = 5336 - 2642;
		long totalShapes = 1000000000000L;
		long totalCycles = (totalShapes - 1709) / shapesPerCycle;
		long numShapes = totalShapes -(totalCycles * shapesPerCycle);
		long extraHeight = totalCycles * heightPerCycle;
		//System.out.println(String.format("numShapes: %d, totalCycles: %d, extraHeight: %d",
		//		numShapes, totalCycles, extraHeight));
		int[][] game = new int[10000][7];
		int maxHeight = simulate(game, hotGas, (int)numShapes);
		Long totalHeight = extraHeight + maxHeight;
		System.out.println("Part 1: " + totalHeight);
	}
	
	
	private static int simulate(int[][] game, String hotGas, int numShapes) {
		// initialize
		int nextJet = 0;
		int maxHeight = 0;
		int nextShape = 0;
		boolean rockStopped = true;
		int curShape = 0;
		int shapeRow = 0;
		int shapeCol = 0;
		int direction = 0;
		while (nextShape < numShapes) {
			if (rockStopped) {
				// next shape appears
				curShape = nextShape % 5;
				shapeRow = maxHeight+3; // bottom of shape
				shapeCol = 2;
				rockStopped = false;
				//visualize(game,curShape,shapeRow,shapeCol,rockStopped,"appear");
			}
			// pushed by gas
			direction = hotGas.charAt(nextJet) == '<' ? -1 : +1;
			nextJet = (nextJet +1) % hotGas.length();
//          // uncomment this section to get parameters for part 2			
//			if ( nextJet == 0) {
//				System.out.println(String.format("New Cycle: nextShape %d, curShape %d, maxHeight %d",
//						nextShape, curShape, maxHeight));
//				visualize(game,curShape,shapeRow,shapeCol,rockStopped,"newCycle");
//			}
			shapeCol += movePossible(game, curShape, shapeRow, shapeCol, 0, direction) ? direction : 0;
			//visualize(game,curShape,shapeRow,shapeCol,rockStopped,"pushed " + direction);
			// fall
			if (movePossible(game, curShape, shapeRow, shapeCol, -1, 0)) {
				shapeRow--;
			}
			else {
				rockStopped = true;
				nextShape++;
				maxHeight = Math.max(maxHeight, shapeRow + shapes.get(curShape).length);
				for (int row=0; row<shapes.get(curShape).length; row++) {
					for (int col=0; col<shapes.get(curShape)[0].length; col++) {
						game[shapeRow+row][shapeCol+col] += shapes.get(curShape)[row][col];
					}
				}
				//visualize(game,curShape,shapeRow,shapeCol,rockStopped,"stopped");
			}
		}
		//visualize(game,curShape,shapeRow,shapeCol,rockStopped,"stopped");
		
		return maxHeight;
		
	}
	
	private static void visualize(final int[][] game, int curShape, int shapeRow, int shapeCol, 
			boolean rockStopped, String label) {
		System.out.println("\n"+label);
		for (int row=shapeRow+shapes.get(curShape).length-1; row>=shapeRow-10; row--) {
			System.out.print("|");
			for (int col=0; col<7; col++) {
				if (!rockStopped && row >= shapeRow && shapeCol <= col && col < shapeCol+shapes.get(curShape)[0].length) {
					if (shapes.get(curShape)[row-shapeRow][col-shapeCol] == 1) {
						System.out.print("@");
					}
					else {
						System.out.print(game[row][col] == 0 ? "." : "#");
					}
				}
				else {
					System.out.print(game[row][col] == 0 ? "." : "#");
				}	
			}
			System.out.println("|");
		}
		System.out.println("+-------+");
	}

	private static boolean movePossible(final int[][] game, final int curShape, final int shapeRow, 
			final int shapeCol, final int moveRow, final int moveCol) {
		if (shapeCol + moveCol < 0) return false; // left of wall
		if (shapeCol + shapes.get(curShape)[0].length + moveCol > 7) return false; // right of wall
		if (shapeRow + moveRow < 0) return false; // below floor
		// check for collision;
		for (int row=0; row<shapes.get(curShape).length; row++) {
			for (int col=0; col<shapes.get(curShape)[0].length; col++) {
				if (shapes.get(curShape)[row][col] + game[shapeRow+row+moveRow][shapeCol+col+moveCol] > 1) {
					return false;
				}
			}
		}
		return true;
	}

	private static List<int[][]> initializeShapes() {
		int[][] bar = {{1,1,1,1}};
		int[][] plus = {{0,1,0},{1,1,1},{0,1,0}};
		int[][] ell = {{1,1,1},{0,0,1},{0,0,1}};
		int[][] rod = {{1},{1},{1},{1}};
		int[][] box = {{1,1},{1,1}};
		List<int[][]> shapes = new ArrayList<>();
		shapes.add(bar);
		shapes.add(plus);
		shapes.add(ell);
		shapes.add(rod);
		shapes.add(box);
		return shapes;
	}

}
