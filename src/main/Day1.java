package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import advent_code_common.FileUtility;


/*
 *  --- Day 1: Calorie Counting ---
 *  
 */

public class Day1 {
	final static String DAY1_INPUT_TXT = "./src/resources/day1_input.txt";

	public static void main(String[] args) {
		List<ElfFood>  provisions = getProvisions(DAY1_INPUT_TXT);
		System.out.println("Advent of Code 2022 - Day 1");
		part1(provisions);
		part2(provisions);
			
	}
	
	private static void part1(List<ElfFood> provisions) {
		int maxCals = provisions.stream()
				.mapToInt(p -> p.getTotalCalories())
				.max().orElse(0);
		System.out.println("part 1: " + maxCals);
	}
	
	private static void part2(List<ElfFood> provisions) {
		 int max3Cals = provisions.stream()
				.map(p -> p.getTotalCalories())
				.sorted(Collections.reverseOrder())
				.limit(3)
				.mapToInt(i -> i.intValue())
				.sum();
		System.out.println("part 2: " + max3Cals);
	}
	
	private static List<ElfFood> getProvisions(String filename) {
		List<Integer> input = FileUtility.readListOfInteger(filename);
		List<ElfFood> provisions = new ArrayList<>();
		int num=1;
		List<Integer> food = new ArrayList<>();
		for (Integer cals : input) {
			if (cals == null) {
				provisions.add(new ElfFood(num++, food));
				food = new ArrayList<>();
			}
			else {
				food.add(cals);
			}
		}
		provisions.add(new ElfFood(num++, food));
		return provisions;
	}
	
	public record ElfFood(int number, List<Integer> foodItems) {
		public int getTotalCalories() {
			return foodItems.stream().mapToInt(f -> f.intValue()).sum();
		}
	}
}
