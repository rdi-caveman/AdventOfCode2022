package main;

import java.util.List;

public class Day2 {
	// Rock Paper Scissors
	final static String DAY2_INPUT_TXT = "./src/resources/day2_input.txt";
	final static int ROCK = 0;
	final static int PAPER = 1;
	final static int SCISSORS = 2;
	final static int LOSS = 0;
	final static int DRAW = 1;
	final static int WIN = 2;
	

	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 2");
		List<String> strategy = FileUtility.readListOfString(DAY2_INPUT_TXT);
		part1(strategy);
		part2(strategy);
		
	}
	
	private static void part1(List<String> strategy) {
		int totalScore = strategy.stream()
			.mapToInt(p -> score(p))
			.sum();
		System.out.println("part 1: " + totalScore);
	}
	
	private static void part2(List<String> strategy) {
		int totalScore = strategy.stream()
			.mapToInt(p -> score2(p))
			.sum();
		System.out.println("part 2: " + totalScore);
	}

	/*
	 * A,B,C and X,Y,Z are ROCK, PAPER, SCISSORs respectfully
	 * score is 1,2,3 for your move + 0,3,6 for loss, draw, win
	 */
	private static int score(String play) {
		int score;
		int oppMove = play.charAt(0) - 'A';
		int myMove = play.charAt(2) - 'X';
		if (oppMove == myMove) {
			// tie
			score = myMove + 4;
		}
		else if (myMove == (oppMove+1)%3) {
			// win
			score = myMove + 7;
		}
		else {
			// loss
			score = myMove + 1;
		}
		//System.out.println(play + " " + score);
		return score;
	}

	/*
	 * A,B,C is ROCK, PAPER, SCISSORs respectfully
	 * X,Y,X is lose, draw, win
	 * score is 1,2,3 for your move (rock, paper, scissors) and 0,3,6 for loss, draw, win
	 */
	private static int score2(String play) {
		int score;
		int oppMove = play.charAt(0) - 'A';
		int outcome = play.charAt(2) - 'X';
		if (outcome == LOSS) {
			score = (oppMove+2)%3 + 1; 
		}
		else if (outcome == DRAW) {
			score = oppMove + 4;
		}
		else {
			score = (oppMove+1)%3 + 7;
		}
		return score;
	}
	
}
