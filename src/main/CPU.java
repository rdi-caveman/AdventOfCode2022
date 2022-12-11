package main;

import java.util.ArrayList;
import java.util.List;

public class CPU {
	private long cycle;
	private List<Instruction> program;
	private int x;
	public List<Integer> history;
	private int instructionCycle;
	private int instructionPointer;
	private Instruction curInstruction;
	
	public void initialize(String filename) {
		program = new ArrayList<>();
		FileUtility.readListOfString(filename).stream()
			.forEach(s -> {
				String[] m = s.split(" ");
				if (m.length == 1) {
					program.add(new Instruction(m[0], null));
				}
				else {
					program.add(new Instruction(m[0],Integer.valueOf(m[1])));
				}	
			});
		x = 1;
		cycle = 0;
		history = new ArrayList<>();
		history.add(x);
		instructionCycle = 0;
		instructionPointer = 0;
	}
	
	public boolean executeCycle() {
		if (instructionCycle == 0) {
			if (!loadInstruction()) {
				return false;
			}
		}
		cycle++;
		history.add(x);
		//System.out.println(cycle + ": " + curInstruction.command + " " + x);
		executeInstruction();
		return true;
	}
	

	private void executeInstruction() {
		instructionCycle--;
		if (instructionCycle == 0) {
			switch (curInstruction.command) {
			case "noop":
				break;
			case "addx": 
				x += curInstruction.operand;
				break;
			}
		}
		
	}

	private boolean loadInstruction() {
		if (instructionPointer == program.size()) {
			return false;	
		}
		curInstruction = program.get(instructionPointer++);
		switch (curInstruction.command) {
		case "noop": 
			instructionCycle = 1;
			break;
		case "addx":
			instructionCycle = 2;
			break;
		}
		return true;
	}

    public record Instruction(String command, Integer operand) {}

	public int signalStrength(int i) {
		return history.get(i) * i;
	}
	
}

