package DataSourceKB;

import java.util.Vector;

public class TimeSet {

	private Vector<String> dayList;
	private Vector<String> hourList;
	
	public TimeSet() {
		dayList = new Vector<String>();
		hourList = new Vector<String>();
	}

	public Vector<String> getDay() {
		return dayList;
	}

	public Vector<String> getHour() {
		return hourList;
	}

}
