package main;

import advent_code_common.FileUtility;

public class Day8 {
	final static String DAY8_INPUT_TXT = "./src/resources/day8_input.txt";
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 8");
		int[][] trees = FileUtility.readIntArray(DAY8_INPUT_TXT);
		part1(trees);
		part2(trees);
	}

	private static void part1(int[][] trees) {
		int visibleCount = 0;
		for (int i=0; i<trees.length; i++) {
			for (int j=0; j<trees[0].length; j++) {
				if (isVisible(trees, i, j)) {
					visibleCount++;
				}
			}
		}
		System.out.println("part 1: " + visibleCount);
	}
	
	private static void part2(int[][] trees) {
		int maxScenicScore = 0;
		for (int i=0; i<trees.length; i++) {
			for (int j=0; j<trees.length; j++) {
				maxScenicScore = Math.max(maxScenicScore, scenicScore(trees, i, j));
			}
		}
		System.out.println("part 1: " + maxScenicScore);
	}

	private static int scenicScore(int[][] trees, int i, int j) {
		// check for edges
		if (i == 0 || i == trees.length-1 || j == 0 || j == trees.length-1) {		
			//System.out.println(i + "," + j + " " + trees[i][j] + " edge");
			return 0;
		}
		int score1 = 0;
		int score2 = 0;
		int score3 = 0;
		int score4 = 0;
		for (int x=i-1; x>=0; x--) {
			score1++;
			if (trees[x][j] >= trees[i][j]) {
				break;
			}
		}
		for (int x=i+1; x<trees.length; x++) {
			score2++;
			if (trees[x][j] >= trees[i][j]) {
				break;
			}
		}
		for (int y=j-1; y>=0; y--) {
			score3++;
			if (trees[i][y] >= trees[i][j] ) {
				break;
			}
		}
		for (int y=j+1; y<trees[0].length; y++) {
			score4++;
			if (trees[i][y] >= trees[i][j] ) {
				break;
			}
		}
		return score1 * score2 * score3 * score4;
	}

	private static boolean isVisible(int[][] trees, int i, int j) {
		// check for edges
		if (i == 0 || i == trees.length-1 || j == 0 || j == trees.length-1) {		
			//System.out.println(i + "," + j + " " + trees[i][j] + " edge");
			return true;
		}
		int max1 = 0;
		int max2 = 0;
		int max3 = 0;
		int max4 = 0;
		for (int x=0; x<i; x++) {
			max1 = Math.max(trees[x][j], max1);
		}
		for (int x=i+1; x<trees.length; x++) {
			max2 = Math.max(trees[x][j], max2);
		}
		if (Math.min(max1, max2) < trees[i][j]) {
			//System.out.println(i + "," + j + " " + trees[i][j] + " vertical");
			return true;
		}
		for (int y=0; y<j; y++) {
			max3 = Math.max(trees[i][y], max3);
		}
		for (int y=j+1; y<trees[i].length; y++) {
			max4 = Math.max(trees[i][y], max4);
		}
		if (Math.min(max3,max4) < trees[i][j]) {
			//System.out.println(i + "," + j + " " + trees[i][j] + " horizontal");
			return true;
		}
		return false;
	}

}
