package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import advent_code_common.FileUtility;

public class Day7 {
	final static String DAY7_INPUT_TXT = "./src/resources/day7_input.txt";
	final static long TOTAL_FILE_SPACE    = 70000000;
	final static long REQUIRED_FREE_SPACE = 30000000;
	
	public static void main(String[] args) {
		System.out.println("Advent of Code 2022 - Day 7");
		List<String> history = FileUtility.readListOfString(DAY7_INPUT_TXT);
		DirEntry root = new DirEntry("/", null);
		parseHistory(root, history);
		Map<String, Long> dirSize = new HashMap<>();
		root.calculateSize(dirSize);
		//System.out.println(dirSize);
		part1(dirSize);
		part2(dirSize);
	}

	private static void part1(Map<String, Long> dirSize) {
		long sumDirSizes = dirSize.entrySet().stream()
				.mapToLong(e -> e.getValue())
				.filter(l -> l <= 100000)
				.sum();
		System.out.println("part 1: " + sumDirSizes);		
	}

	private static void part2(Map<String, Long> dirSize) {
		long usedSpace = dirSize.get("/");
		long minToDelete = REQUIRED_FREE_SPACE - (TOTAL_FILE_SPACE - usedSpace);
		//System.out.println(String.format("used: %d, free: %d, needed to delete: %d", usedSpace, TOTAL_FILE_SPACE - usedSpace, minToDelete ));
		long dirToDelete = dirSize.entrySet().stream()
				.mapToLong(d -> d.getValue())
				.filter(l -> l >= minToDelete)
				.sorted()
				.findFirst().orElse(0L);
		System.out.println("part 2: " + dirToDelete);
	}
	
	
	private static void parseHistory(DirEntry root, List<String> history) {
		DirEntry curDir = null;
		for(String command: history) {
			String[] cmd = command.split(" ");
			if (cmd[0].equals("$")) {
				if (cmd[1].equals("cd")) {
					if (cmd[2].equals("/")) {
						curDir = root;
					}
					else if (cmd[2].equals("..")) {
						curDir = curDir.parent;
					}
					else {
						DirEntry temp = curDir;
						curDir = curDir.subDirs.get(cmd[2]);
						curDir.parent = temp;
					}
				}
				else if (cmd[1].equals("ls")) {
					curDir.subDirs = new HashMap<>();
					curDir.files = new ArrayList<>();
				}
			}
			else if (cmd[0].equals("dir")) {
				curDir.subDirs.put(cmd[1],new DirEntry(cmd[1], curDir));
			}
			else {
				curDir.files.add(new FileEntry(cmd[1], Integer.valueOf(cmd[0])));
			}
		}
		
	}


}
