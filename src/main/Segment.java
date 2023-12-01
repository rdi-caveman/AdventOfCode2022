package main;

public class Segment implements Comparable<Segment> {
	public int begin;
	public int end;

	public Segment(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}
	
	public static Segment coverage(Sensor s, int y) {
		Segment returnSegment = null;
		int coverage = s.strength - Math.abs(s.location.y - y);
		if (coverage >= 0) {
			returnSegment = new Segment(s.location.x - coverage, s.location.x + coverage);
		}
		//System.out.println(s + " " + returnSegment);
		return returnSegment;
	}

	@Override
	public String toString() {
		return "Segment [begin=" + begin + ", end=" + end + "]";
	}
	
	@Override
	public int compareTo(Segment o) {
		if (begin == o.begin) {
			if (end == o.end) {
				return 0;
			}
			return end < o.end ? -1 : 1;
		}
		return begin < o.begin ? -1 : 1;
	}
}