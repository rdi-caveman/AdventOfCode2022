package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import advent_code_common.FileUtility;
import advent_code_common.Pair;

public class Day13 {
	static final String DAY13_INPUT_TXT = "./src/resources/day13_input.txt";
	static final String RECORD_DELIMITER = "\\r?\\n\\r?\\n";

	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 13");
		part1(DAY13_INPUT_TXT);
		part2(DAY13_INPUT_TXT);
	}

	private static void part1(String filename) {
		List<Pair<Packet,Packet>> input = FileUtility.readRecords(filename, RECORD_DELIMITER).stream()
				.map(s -> parseRecord(s))
				.toList();
		int sumInOrder = 0;
		int pairNum = 1;
		for (Pair<Packet, Packet> pair : input) {
			if (pair.first.compareTo(pair.second) == -1) {
				sumInOrder += pairNum;
			}
			pairNum++;
		}
		System.out.println("part 1: " + sumInOrder);
	}

	private static void part2(String filename) {
		Packet divider1 = Packet.parse("[[2]]");
		Packet divider2 = Packet.parse("[[6]]");
		List<Packet> message = new ArrayList<>();
		message.add(divider1);
		message.add(divider2);
		List<Packet> puzzle = 
				FileUtility.readListOfString(filename).stream()
				.filter(s -> !s.replace("\s","").isEmpty())
				.map(s -> Packet.parse(s))
				.toList();
		message.addAll(puzzle);
		Collections.sort(message);
		System.out.println("part 2: " + (message.indexOf(divider1)+1)*(message.indexOf(divider2)+1) );
		
		
	}

	private static Pair<Packet, Packet> parseRecord(String s) {
		String[] r = s.split("\r?\n");
		return new Pair(Packet.parse(r[0]),Packet.parse(r[1]));
	}



}
