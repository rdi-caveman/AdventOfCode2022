package main;


import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

public class Day11 {
	static final String DAY11_INPUT_TXT = "./src/resources/day11_input.txt";
	static final String RECORD_SEPARATOR = "\\r?\\n\\r?\\n";
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 11");
		part1();
		part2();
	}

	private static void part1() {
		final int rounds = 20;
		final long worryLevelDivisor = 3;
		long monkeyBusiness = monkeyBusiness(rounds, worryLevelDivisor);
			System.out.println("part 1: " + monkeyBusiness);
	}
	
	private static void part2() {
		final int rounds = 10000;
		final long worryLevelDivisor = 1;
		long monkeyBusiness = monkeyBusiness(rounds, worryLevelDivisor);
			System.out.println("part 2: " + monkeyBusiness);
	}
	

	private static long monkeyBusiness(final int rounds, final long worryLevelDivsor) {
		// populate initial conditions
		List<Monkey> monkeys = FileUtility.readRecords(DAY11_INPUT_TXT, RECORD_SEPARATOR).stream()
				.map(s -> new Monkey(s))
				.toList();
		
		// get cumulative worry divisor
		 final long cumulativeWorryDivisor = getCumulativeWorryDivsor(monkeys);
		 
		
		for (int i=1; i<=rounds; i++) {
			for (Monkey m : monkeys) {
				m.startingItems.stream()
					.map(item -> m.inspect(item)) // worry level increase
					.map(item -> item / worryLevelDivsor) // worry level decreases
					.forEach(item -> {
						int target = m.test(item); // decide which monkey to throw to
						monkeys.get(target).addItem(item % cumulativeWorryDivisor); // throw item
					});
				m.startingItems.clear(); // all items have been inspected and thrown	
			}
			//summarizeRound(i, monkeys);
			//if (i == 1 || i == 20 || i % 1000 == 0) {
			//	summarizeRound(i, monkeys);
			//	summarizeInspections(i, monkeys);
			//}	
		}
		Long monkeyBusiness = monkeys.stream()
				.map(m -> m.inspectedItems)
				.sorted(Comparator.reverseOrder())
				.map(i -> (long) i)
				.limit(2)
				.reduce((i1,i2) -> i1*i2)
				.orElse(0L);
		return monkeyBusiness;
	}

	private static long getCumulativeWorryDivsor(List<Monkey> monkeys) {
		long cumulativeWorryDivisor =1;
		for (Monkey m : monkeys) {
			cumulativeWorryDivisor *= m.getDivisor();
		}
		return cumulativeWorryDivisor;
	}

	private static void summarizeRound(int i, List<Monkey> monkeys) {
		System.out.println("\nAfter round " + i + " the monkeys are holding items with these worry levels:");
		monkeys.stream()
			.forEach(m -> System.out.println("Monkey " + m.number + " " + m.startingItems));
	}

	private static void summarizeInspections(int i, List<Monkey> monkeys) {
		System.out.println("\n== After round " + i + "==");
		monkeys.stream()
			.forEach(m -> System.out.println("Monkey " + m.number + " inspected items " + m.inspectedItems + " times."));
	}
}
