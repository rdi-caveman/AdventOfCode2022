package main;

import java.util.Arrays;
import java.util.List;

import advent_code_common.FileUtility;

public class Day25 {
	private static final String DAY25_INPUT_TXT = "./src/resources/day25_input.txt";
	private static long[] pow5;
	
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 25");
		List<String> input = FileUtility.readListOfString(DAY25_INPUT_TXT);
		System.out.println(input);
		initializePow5(20);
		System.out.println(Arrays.toString(pow5));
		part1(input);

		
		
		
	}

	
	private static void initializePow5(int size) {
		pow5 = new long[size];
		long value = 1;
		for (int i=0; i< size; i++) {
			pow5[i] = value;
			value *= 5;
		}
	}


	private static void part1(List<String> input) {
		Long sumInput = input.stream()
				.mapToLong(s -> snafuToDecimal(s))
				.sum();
		System.out.println("part 1:" + decimalToSnafu(sumInput));
	}


	private static long snafuToDecimal(String s) {
		long returnValue = 0;
		for (int i=0; i<s.length(); i++) {
			returnValue += decode(s.charAt(s.length() - i - 1)) * pow5[i];
		}
		return returnValue;
	}
	
	private static StringBuffer decimalToSnafu(Long l) {
		// initial snafu
		StringBuffer sb = new StringBuffer(pow5.length);
		for (int i=0; i<pow5.length; i++) {
			sb.append("0");
		}
		// fill snafu
		long remainder = l;
		int pow = pow5.length;
		int pos;
		while (remainder != 0) {
			pos = pow5.length - pow; // character position
			pow--; // power of 5
			int multiplier = 0;
			long minRemainder = Math.abs(remainder);
			for (int m=-2; m<=2; m++) {
				if (Math.abs(remainder - m*pow5[pow]) < minRemainder) {
					minRemainder = Math.abs(remainder - m*pow5[pow]);
					multiplier = m;
				}
			}
			remainder -= multiplier*pow5[pow];
			sb.setCharAt(pos, switch(multiplier) {
			case -2 -> '=';
			case -1 -> '-';
			case 0 -> '0';
			case 1 -> '1';
			case 2 -> '2';
			default -> throw new IllegalArgumentException("Unexpected value: " + multiplier);
			});
			
			
		}
		return sb;
	}
	
	private static int decode(char c) {
		return switch(c) {
		case '=' -> -2;
		case '-' -> -1;
		case '0' -> 0;
		case '1' -> 1;
		case '2' -> 2;
		default -> throw new IllegalArgumentException("Unexpected value: " + c);
		};
	}
}
