package main;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import advent_code_common.FileUtility;

public class Day20 {

	private static final BigInteger DECRYPTION_KEY = new BigInteger("811589153");
	private static final String DAY20_SAMPLE_TXT = "./src/resources/day20_sample.txt";
	private static final String DAY20_INPUT_TXT = "./src/resources/day20_input.txt";

	public static void main(String[] args) {
		System.out.println("Advent of Code 2020 - Day 20");
		int[] input = FileUtility.readArrayOfInt(DAY20_INPUT_TXT);
		part1(input);
		part2(input);
	}

	private static void part2(int[] input) {
		// TODO Auto-generated method stub
		BigInteger[] mixInput = decrypt(input);
		List<Integer> output = mixSwap(mixInput);
		Integer zeroLoc = findZero(mixInput);
		int zeroOutput = output.indexOf(zeroLoc);
		/*
		 * System.out.println(String.format("1K: %d, 2K: %d, 3K: %d",
		 * mixInput[output.get((zeroOutput + 1000) % output.size())],
		 * mixInput[output.get((zeroOutput + 2000) % output.size())],
		 * mixInput[output.get((zeroOutput + 3000) % output.size())]));
		 */
		System.out.println(String.format("part 2: %d",
				mixInput[output.get((zeroOutput + 1000) % output.size())]
						.add(mixInput[output.get((zeroOutput + 2000) % output.size())])
						.add(mixInput[output.get((zeroOutput + 3000) % output.size())])
				));
	}

	private static List<Integer> mixSwap(BigInteger[] input) {
		// initialize output
		CircularList<Integer> output = new CircularList<>();
		IntStream.range(0,input.length).forEach(i -> output.add(i));
		//print(0,output,input);
		// do the mixing - ten times
		for (int round=1; round<=10; round++) {
			for(int i=0; i< input.length; i++) {
				BigInteger offset = input[i];
				output.move(i,  offset);
			}
			//print(round,output,input);
		}	
		return output;
	}

	private static BigInteger[] decrypt(int[] input) {
		BigInteger[] output = new BigInteger[input.length];
		for (int i=0; i< input.length; i++) {
			output[i] = BigInteger.valueOf(input[i]).multiply(DECRYPTION_KEY);
		}
		return output;
	}

	private static void part1(int[] input) {
		List<Integer> output = mixSwap(input);
		Integer zeroLoc = findZero(input);
		int zeroOutput = output.indexOf(zeroLoc);
		System.out.println(String.format("part 1: %d",
				input[output.get((zeroOutput + 1000) % output.size())] +
				input[output.get((zeroOutput + 2000) % output.size())] +
				input[output.get((zeroOutput + 3000) % output.size())]
				));
	}

	private static Integer findZero(int[] input) {
		//find zero
		Integer zeroLoc = 0;
		for (int i=0; i<input.length; i++) {
			if (input[i] == 0) {
				zeroLoc = i;
				break;
			}
		}
		return zeroLoc;
	}
	

	private static Integer findZero(BigInteger[] input) {
		//find zero
		Integer zeroLoc = 0;
		for (int i=0; i<input.length; i++) {
			if (input[i].compareTo(BigInteger.ZERO)==0)  {
				zeroLoc = i;
				break;
			}
		}
		return zeroLoc;
	}
	
	private static List<Integer> mixSwap(int[] input) {
		// initialize output
		CircularList<Integer> output = new CircularList<>();
		IntStream.range(0,input.length).forEach(i -> output.add(i));
		// do the mixing
		//print(0,output,input);
		for(int i=0; i< input.length; i++) {
			long offset = (long) input[i];
			output.move(i,  offset);
			//print(i+1,output,input);
		}

		return output;
	}
	
	private static void print(int move, List<Integer> output, BigInteger[] input) {
		System.out.println(String.format("%5d: %s",move, output));
		System.out.print("      ");
		for(int i=0; i<output.size(); i++) {
			System.out.print( input[output.get(i)] + " ");
		}
		System.out.println();
	}
}
