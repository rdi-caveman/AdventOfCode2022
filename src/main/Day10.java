package main;

public class Day10 {
	final static String DAY10_INPUT_TXT = "./src/resources/day10_input.txt";
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 10");
		//test();
		part1();
		part2();

	}
	
	public static void test() {
		CPU cpu = new CPU();
		cpu.initialize("./src/resources/day10_small.txt");
		boolean running = true;
		while (running) {
			running = cpu.executeCycle();
		}
		System.out.println(cpu.history);
	}
	
	public static void part1() {
		CPU cpu = new CPU();
		cpu.initialize(DAY10_INPUT_TXT);
		boolean running = true;
		while (running) {
			running = cpu.executeCycle();
		}
		int signalStrengthSum = cpu.signalStrength(20) 
				+ cpu.signalStrength(60)
				+ cpu.signalStrength(100)
				+ cpu.signalStrength(140)
				+ cpu.signalStrength(180)
                + cpu.signalStrength(220);

		System.out.println("part 1: " + signalStrengthSum);
	}
	
	public static void part2() {
		CPU cpu = new CPU();
		cpu.initialize(DAY10_INPUT_TXT);
		boolean running = true;
		while (running) {
			running = cpu.executeCycle();
		}
		System.out.println("part 2:");
		String crtLine;
		for (int v=0; v<6; v++) {
			crtLine = "";
			for (int h=0; h<=39; h++) {
				crtLine += Math.abs(cpu.history.get(v*40+h+1) - h) <= 1 ? "#" : ".";
			}
			System.out.println(crtLine);
		}
	}

}
