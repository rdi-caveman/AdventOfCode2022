package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent_code_common.FileUtility;

public class Day21 {
	private static final String DAY21_SAMPLE_TXT = "./src/resources/day21_sample.txt";
	private static final String DAY21_INPUT_TXT = "./src/resources/day21_input.txt";
	private static Map<String, Monkey> evaluated = new HashMap<>();
	private static Map<String, Monkey> unevaluated = new HashMap<>();
	
	public static void main(String[] args) {
		parse(DAY21_INPUT_TXT);
		while (!evaluated.containsKey("root")) {
			List<String> keys = new ArrayList<>();
			keys.addAll(unevaluated.keySet());
			for (var key : keys) {
				evaluate(key);
			}
		}
		System.out.println("part 1: " + evaluated.get("root").value);

	}

	private static void evaluate(String key) {
		Monkey m = unevaluated.get(key);
		if (evaluated.containsKey(m.var1) && evaluated.containsKey(m.var2)) {
			long value = switch(m.operator) {
			case "+" -> evaluated.get(m.var1).value + evaluated.get(m.var2).value;
			case "-" -> evaluated.get(m.var1).value - evaluated.get(m.var2).value;
			case "*" -> evaluated.get(m.var1).value * evaluated.get(m.var2).value;
			case "/" -> evaluated.get(m.var1).value / evaluated.get(m.var2).value;
			default -> throw new IllegalArgumentException("Unexpected value: " + m.operator);
			};
			unevaluated.remove(key);
			evaluated.put(key,new Monkey(m.var1, m.operator, m.var2, value));
		}
	}

	private static void parse(String filename) {
		FileUtility.readListOfString(filename).stream()
			.forEach(s -> {
				String[] a = s.split(": ");
				String[] b = a[1].split(" ");
				if (b.length == 1) {
					evaluated.put(a[0], new Monkey(null, null, null, Long.valueOf(a[1])));
				}
				else {
					unevaluated.put(a[0], new Monkey(b[0], b[1], b[2], null));
				}
			});
		
	}

	record Monkey(String var1, String operator, String var2, Long value) {}
	
}
