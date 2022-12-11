package main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3 {
	final static String DAY3_INPUT_TXT = "./src/resources/day3_input.txt";

	// Rucksacks
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 3");
		List<String> rucksacks = FileUtility.readListOfString(DAY3_INPUT_TXT);
		part1(rucksacks);
		part2(rucksacks);
	}

	private static void part1(List<String> rucksacks) {
		int sumPriorities = rucksacks.stream()
				.map(r -> duplicateItem(r))
				.mapToInt(i -> priority(i))
				.sum();
		System.out.println("part 1: " + sumPriorities);
	}

	private static void part2(List<String> rucksacks) {
		int sumBadgePriorities = IntStream.iterate(0, i -> i+3).limit(rucksacks.size()/3)
			.map(i -> findCommon(rucksacks.get(i), rucksacks.get(i+1), rucksacks.get(i+2)))
			.map(c -> priority((char) c))
			.sum();
		System.out.println("part 2: " + sumBadgePriorities);		
	}

	private static char findCommon(String pack1, String pack2, String pack3) {
		Set<Character> items1 =	stringToCharSet(pack1);
		Set<Character> items2 = stringToCharSet(pack2);
		Set<Character> items3 = stringToCharSet(pack3);
		items1.retainAll(items2);
		items1.retainAll(items3);
		return items1.iterator().next();
	}
		
	private static int priority(char i) {
		int priority = 0;
		if (i >= 'a' && i <= 'z') {
			priority = (i-'a') + 1;
		}
		else if (i >= 'A' && i <= 'Z') {
			priority = (i-'A') + 27;
		}
		//System.out.println(i + " " + priority);
		return priority;
	}

	private static char duplicateItem(String r) {
		char duplicate = '?';
		Set<Character> items = stringToCharSet(r.substring(0,r.length()/2));
		for (int i=r.length()/2; i<r.length(); i++) {
			if (items.contains(r.charAt(i))) {
				duplicate = r.charAt(i);
				break;
			}
		}
		//System.out.println(r + " " + duplicate);
		return duplicate;
	}
	
	private static Set<Character> stringToCharSet(String s) {
		Set<Character> charSet = new HashSet<>();
		for (int i=0; i<s.length(); i++) {
			charSet.add(s.charAt(i));
    	}
		return charSet;
	}
}
