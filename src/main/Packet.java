package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Packet implements Comparable{
	static final Pattern parse_pattern = Pattern.compile("(\\[|\\]|,|\\d+)");
	static final Matcher parse_matcher = parse_pattern.matcher("");
	List<Object> contents;
	
	public Packet(List<Object> contents) {
		this.contents = contents;
	}
	
	public static Packet parse(String string) {
		List<Object> temp = null;
		Stack<List<Object>> stack = new Stack<>();
		parse_matcher.reset(string);
		while (parse_matcher.find()) {
			switch (parse_matcher.group(1)) {
			case "[":
				stack.push(new ArrayList<>());
				break;
			case ",":
				break;
			case "]":
				temp = stack.pop();
				if (!stack.empty()) {
					stack.peek().add(temp);
				}
				break;
			default:
				stack.peek().add(Integer.valueOf(parse_matcher.group(1)));
			}
		}
		return new Packet(temp);
	}
	
	@Override 
	public int compareTo(Object o) throws RuntimeException {
		if (o instanceof Packet) {
			Packet p = (Packet) o;
			return compare(this.contents, p.contents);
		}
		throw new RuntimeException("not a Packet");	
	}
	
	@Override
	public String toString() {
		return contents.toString();
	}
	
	private int compare(List<Object> a, List<Object> b) {
		List<Object> first = new ArrayList<>();
		first.addAll(a);
		List<Object> second = new ArrayList<>();
		second.addAll(b);
		// one list is empty
		if (first.isEmpty() && !second.isEmpty()) return -1; // left list ran out of items first
		if (!first.isEmpty() && second.isEmpty()) return +1; // right list ran out of items first
		if (first.isEmpty() && second.isEmpty()) return 0; // lists are equal
		// neither list is empty, get first elements
		Object left = first.get(0);
		Object right = second.get(0);
		// either is integer
		while (left instanceof Integer && right instanceof Integer) {
			if ( (Integer) left < (Integer) right ) return -1;
			if ( (Integer) left > (Integer) right ) return 1;
			first.remove(0);
			second.remove(0);
			return compare(first,second);
		}
		if (left instanceof Integer) {
			List<Object> temp = new ArrayList<>();
			temp.add(left);
			first.set(0, temp);
			return compare(first,second);
		}
		if (right instanceof Integer) {
			List<Object> temp = new ArrayList<>();
			temp.add(right);
			second.set(0, temp);
			return compare(first,second);
		}	
		// neither is integer, both first elements are lists
		int returnValue = compare((List<Object>) left, (List<Object>) right);
		if (returnValue == 0) {
			first.remove(0);
			second.remove(0);
			returnValue = compare(first,second);
		}	
		return returnValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contents == null) ? 0 : contents.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Packet other = (Packet) obj;
		if (contents == null) {
			if (other.contents != null)
				return false;
		} else if (!contents.equals(other.contents))
			return false;
		return true;
	}

}
