package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import advent_code_common.Point;

public class Sensor {
	static final Pattern sensor_pattern = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
	static final Matcher sensor_matcher = sensor_pattern.matcher("");
	
	Point location;
	Point nearestBeacon;
	int strength;
	
	public Sensor(Point location, Point nearestSensor) {
		this.location = location;
		this.nearestBeacon = nearestBeacon;
		strength = Math.abs(location.x - nearestBeacon.x) + Math.abs(location.y - nearestBeacon.y);
	}
	
	public Sensor(String s) {
		sensor_matcher.reset(s);
		if (sensor_matcher.find()) {
			this.location = new Point(Integer.valueOf(sensor_matcher.group(1)),Integer.valueOf(sensor_matcher.group(2)));
			this.nearestBeacon = new Point(Integer.valueOf(sensor_matcher.group(3)),Integer.valueOf(sensor_matcher.group(4)));
			strength = Math.abs(location.x - nearestBeacon.x) + Math.abs(location.y - nearestBeacon.y);
		}
		else {
			System.out.println("Couldn't parse " + s);
		}
	}

	@Override
	public String toString() {
		return "Sensor [location=" + location + ", nearestBeacon=" + nearestBeacon + ", strength=" + strength + "]";
	}
	
	
}
