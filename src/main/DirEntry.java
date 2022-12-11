package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirEntry {
	String name;
	Map<String,DirEntry> subDirs;
	List<FileEntry> files;
	DirEntry parent;
	long size;
	String path;
	
	public DirEntry(String myName, DirEntry myParent) {
		name = myName;
		parent = myParent;
		size = 0;
		files = new ArrayList<>();
		subDirs = new HashMap<>();
		path = myParent == null ? name : myParent.path + name + "/";
	}
	
	public long calculateSize(Map<String, Long> dirSize) {
		size = 0L;
		for (FileEntry file : files) {
			size += file.size;
		}
		for (String key : subDirs.keySet()) {
			size += subDirs.get(key).calculateSize(dirSize);
		}
		dirSize.put(path,size);
		return size;
	}
}

