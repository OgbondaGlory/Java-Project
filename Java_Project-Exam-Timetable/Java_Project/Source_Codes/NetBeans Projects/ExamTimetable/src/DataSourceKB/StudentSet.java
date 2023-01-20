package DataSourceKB;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class StudentSet {
	private String name;
	private int totStud, groupLimit;
	private ArrayList<StudentSet> groupList;

	public StudentSet(String Name, int Strength) {
		name = Name;
		totStud = Strength;
		groupLimit = 0;
		groupList = new ArrayList<StudentSet>();
	}

	public ArrayList<StudentSet> getGroupList() {
		return groupList;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public int getTotStud() {
		return totStud;
	}

	public void setTotStud(int size) {
		if (size < totStud)
			alterGroupSize(totStud - size);
		totStud = size;
	}

	private void alterGroupSize(int d) {
		for (int i = 0; i < groupList.size(); i++) {
			int s = groupList.get(i).getTotStud();
			if ((s - d) > 0) {
				groupList.get(i).setTotStud(s - d);
				return ;
			}
		}
	}

	public int getGroupLimit() {
		return groupLimit;
	}

	public void setGroupLimit(int i) {
		groupLimit += i;
	}

	public String getGroupName(int gno) {
		return groupList.get(gno).getName();
	}

	public void setGroupName(int gno, String s) {
		groupList.get(gno).setName(s);
	}

	public int getGroupSize(int gno) {
		return groupList.get(gno).getTotStud();
	}

	public void setGroupSize(int gno, int i) {
		if (i != getGroupSize(gno)) {
			if (i <= (totStud - groupLimit + getGroupSize(gno))) {
				setGroupLimit(i - getGroupSize(gno));
				groupList.get(gno).setTotStud(i);
			} else
				JOptionPane.showMessageDialog(null, "The Group Size for Group:"
						+ gno + " \n must be less than Strength:"
						+ (totStud - groupLimit + getGroupSize(gno)),
						"Group Size Exceeds!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
