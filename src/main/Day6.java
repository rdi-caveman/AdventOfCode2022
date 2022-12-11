package main;

public class Day6 {
	final static String DAY6_INPUT_TXT = "./src/resources/day6_input.txt";
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 6");
		String signal = FileUtility.readEntireFile(DAY6_INPUT_TXT);
		System.out.println("part 1: " + detectMarker(signal,4));
		System.out.println("part 2: " + detectMarker(signal,14));
	}

	private static int detectMarker(String signal, int size) {
		int i=size;
		boolean sopFound = false;
		sopSearch: do {
			//System.out.println(signal.substring(i-size,i) + " " + i);
			outerLoop: for (int j=size; j>1; j--) {
				for(int k=j-1; k>0; k--) {
					if (signal.charAt(i-j) == signal.charAt(i-k)) {
						continue sopSearch;
					}
				}
			}
			sopFound = true;
			break sopSearch;
		} while(++i <= signal.length());
		return i;
	}
}
