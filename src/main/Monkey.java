package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Monkey {
	static final Pattern monkey_pattern = Pattern.compile("Monkey (\\d+):"
			+ ".*Starting items: ([\\d, ]+\\d)"
			+ ".*Operation: new = old ([+*]) (\\S+)"
			+ ".*Test: divisible by (\\d+)"
			+ ".*If true: throw to monkey (\\d+)"
			+ ".*If false: throw to monkey (\\d+)",
			Pattern.DOTALL);
	static final Matcher monkey_matcher = monkey_pattern.matcher("");
	
	Integer number;
	List<Long> startingItems = new ArrayList<>();;
	Operation operation;
	Test test;
	long inspectedItems = 0;
	
	public Monkey(String input) {
		//System.out.println(input);
		monkey_matcher.reset(input);
		if (monkey_matcher.find()) {
			number = Integer.valueOf(monkey_matcher.group(1));
			for (String item : Arrays.asList(monkey_matcher.group(2).split(", "))) {
				startingItems.add(Long.valueOf(item));
			}		
			operation = new Operation(monkey_matcher.group(3), 
					monkey_matcher.group(4));
			test = new Test(Long.valueOf(monkey_matcher.group(5)), 
					Integer.valueOf(monkey_matcher.group(6)), 
					Integer.valueOf(monkey_matcher.group(7)));
		}
		
	}
	
	public long getDivisor() {
		return test.divisor;
	}
	
	public long inspect(long item) {
		inspectedItems++;
		long operand = operation.operand.equals("old") ? item : Long.valueOf(operation.operand);
		return operation.operator.equals("+") ? item + operand : item * operand;
	}
	
	@Override
	public String toString() {
		return "Monkey [number=" + number + ", startingItems=" + startingItems + ", operation=" + operation + ", test="
				+ test + "]";
	}

	public int test(long item) {
		return item % test.divisor == 0? test.trueMonkey : test.falseMonkey;
	}
	
	public void addItem(long item) {
		startingItems.add(item);
	}
	
	public record Operation(String operator, String operand) {}
	public record Test(long divisor, Integer trueMonkey, Integer falseMonkey) {}

}
