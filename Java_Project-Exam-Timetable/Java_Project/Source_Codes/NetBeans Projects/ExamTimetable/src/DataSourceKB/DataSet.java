package DataSourceKB;

import ETS.Timetable.Rules;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;



public class DataSet {
	private ArrayList<RoomSet> roomList;
	private ArrayList<SubjectSet> subjectList;
	private ArrayList<TeacherSet> teacherList;
	private ArrayList<StudentSet> studentList;
	private ArrayList<ActivitySet> activityList;
	private ArrayList<AssigndSubjects> assignSubList;
	private Rules ruleSet;
	private TimeSet timeSet;

	private boolean uptodate;
	private int TH,PR; 

	public static final int MAXSUBJECT = 150; /* SUBJECT field */
	public static final int MAXSTUDENT = 50; /* STUDENT field */
	public static final int MAXTEACHER = 150; /* TEACHER field */
	public static final int MAXROOM = 50; /* ROOM field */
	public static final int MAXDAY = 6; /* MAXDAY field */
	public static final int MAXHOUR = 12; /* MAXHOUR field */

	public static final int SUBJECT = 0; /* SUBJECT field */
	public static final int STUDENT = 1; /* STUDENT field */
	public static final int TEACHER = 2; /* TEACHER field */
	public static final int ROOM = 3; /* ROOM field */
	public static final int TIME = 4; /* ROOM field */
	public static final int ACTIVITY = 5; /* ACTIVITY field */
	public static final int ASSIGNSUBJECT = 6; /* SUBJECT field */

	// public static final int STUDENTPR = 7; /* STUDENT field */
	// public static final int TEACHERPR = 8; /* TEACHER field */

	public static final int DATA_NAME = 0; /* name field */
	public static final int DATA_TYPE = 1; /* type field */
	public static final int DATA_CAPACITY = 2; /* capacity field */
	public static final int DATA_NOT = 3; /* no. of theory field */
	public static final int DATA_NOP = 4; /* no. of practical field */
	public static final int DATA_STRENGTH = 5; /* no. of student field */
	public static final int ID = 6; /* no. of student field */

	public DataSet() {
		uptodate = true;
		roomList = new ArrayList<RoomSet>();
		subjectList = new ArrayList<SubjectSet>();
		teacherList = new ArrayList<TeacherSet>();
		studentList = new ArrayList<StudentSet>();
		activityList = new ArrayList<ActivitySet>();
		assignSubList = new ArrayList<AssigndSubjects>();
		ruleSet =new Rules();
		timeSet = new TimeSet();
	}

	public void setTHPR(int TH,int PR) {
		this.TH = TH;
		this.PR = PR;
	}

	public int getTH() {
		return TH;
	}

	public int getPR() {
		return PR;
	}

	public boolean getStatus() {
		return uptodate;
	}

	public void setStatus(boolean status) {
		uptodate = status;
	}

	public TimeSet getTimeSet() {
		return timeSet;
	}

	// STUDENT////////////////////////////////////////////////

	public StudentSet getStudent(int i) {
		return studentList.get(i);
	}

	public int searchStudent(String name) {
		for (int i = 0; i < studentList.size(); i++)
			if (studentList.get(i).getName().equalsIgnoreCase(name))
				return i;
		return -1;
	}

	public int searchGroupStudent(int i, String name) {
		StudentSet temp = getStudent(i);
		for (int j = 0; j < temp.getGroupList().size(); j++)
			if (temp.getGroupList().get(j).getName().equalsIgnoreCase(name))
				return j;
		return -1;
	}

	public StudentSet searchGroupStudentSet(StudentSet temp, String name) {
		for (int i = 0; i < temp.getGroupList().size(); i++)
			if (temp.getGroupList().get(i).getName().equalsIgnoreCase(name))
				return temp.getGroupList().get(i);
		return null;
	}

	public void removeStudent(int pos) {
		studentList.remove(pos);
	}

	public void removeGroup(int pos, int gpos) {
		int gs =studentList.get(pos).getGroupSize(gpos);
		studentList.get(pos).setGroupLimit(gs);
		studentList.get(pos).getGroupList().remove(gpos);
		
	}

	public void addStudent(String name, int strength) {
		if (searchStudent(name) == -1) {
			studentList.add(new StudentSet(name, strength));
			setStatus(false);
		} else
			JOptionPane.showMessageDialog(null, "Duplicate Entry : " + name,
					"Duplicate", JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean addGroup(String Name, String GName, int Strength) {
		int i = searchStudent(Name);
		StudentSet temp = getStudent(i);
		if (temp != null) {
			System.out.println(temp.getTotStud() + ":" + temp.getGroupLimit());
			if (searchGroupStudent(i, GName) == -1) {
				if (Strength <= (temp.getTotStud() - temp.getGroupLimit())) {
					temp.getGroupList().add(new StudentSet(GName, Strength));
					temp.setGroupLimit(Strength);
					setStatus(false);
					return true;
				} else
					JOptionPane
							.showMessageDialog(null,
									"The Group Size for Group"
											+ " \n must be less than "
											+ (temp.getTotStud() - temp
													.getGroupLimit()),
									"Group Size Exceeds!",
									JOptionPane.ERROR_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, "Duplicate Entry : "
						+ GName, "Duplicate", JOptionPane.INFORMATION_MESSAGE);

		} else
			JOptionPane.showMessageDialog(null, Name
					+ " not avail!\n The Group can't create", "Group Error!",
					JOptionPane.ERROR_MESSAGE);
		return false;
	}

	public int getAllStudentLength() {
		int size = 0;
		for (int i = 0; i < studentList.size(); i++) {
			size += studentList.get(i).getGroupList().size()+1;
		}
		return size;
	}

	public int getStudentLength() {
		return studentList.size();
	}

	public Object[] getStudentRecord(int i) {
		Object[] obj = { studentList.get(i).getName(),
				studentList.get(i).getTotStud() };
		return obj;
	}

	public Object[] getGroupStudentRecord(int i, int j) {
		Object[] obj = { studentList.get(i).getGroupList().get(j).getName(),
				studentList.get(i).getGroupList().get(j).getTotStud() };
		return obj;
	}

	// TEACHER////////////////////////////////////////////////

	public TeacherSet getTeacher(int i) {
		return teacherList.get(i);
	}

	public int searchTeacher(String name) {
		for (int i = 0; i < teacherList.size(); i++)
			if (teacherList.get(i).getName().equalsIgnoreCase(name))
				return i;
		return -1;
	}

	public void removeTeacher(int pos) {
		teacherList.remove(pos);
	}

	public void addTeacher(String name, String type) {
		if (searchTeacher(name) == -1) {
			teacherList.add(new TeacherSet(name, type));
			setStatus(false);
		} else
			JOptionPane.showMessageDialog(null, "Duplicate Entry : " + name,
					"Duplicate", JOptionPane.INFORMATION_MESSAGE);
	}

	public int getTeacherLength() {
		return teacherList.size();
	}

	public Object[] getTeacherRecord(int i) {
		Object[] obj = { teacherList.get(i).getName(),
				teacherList.get(i).getType() };
		return obj;
	}

	// SUBJECT////////////////////////////////////////////////

	public SubjectSet getSubject(int i) {
		return subjectList.get(i);
	}

	public int searchSubject(String name) {
		for (int i = 0; i < subjectList.size(); i++)
			if (subjectList.get(i).getName().equalsIgnoreCase(name))
				return i;
		return -1;
	}

	public void removeSubject(int pos) {
		subjectList.remove(pos);
	}

	public void addSubject(String name, int noT, int noP) {
		if (searchSubject(name) == -1) {
			subjectList.add(new SubjectSet(name, noT, noP));
			setStatus(false);
		} else
			JOptionPane.showMessageDialog(null, "Duplicate Entry : " + name,
					"Duplicate", JOptionPane.INFORMATION_MESSAGE);
	}

	public int getSubjectLength() {
		return subjectList.size();
	}

	public Object[] getSubjectRecord(int i) {
		Object[] obj = { subjectList.get(i).getName(),
				subjectList.get(i).getTPPW(), subjectList.get(i).getPPPW() };

		return obj;
	}

	// ROOM////////////////////////////////////////////////

	public RoomSet getRoom(int i) {
		return roomList.get(i);
	}

	public int searchRoom(String name) {
		for (int i = 0; i < roomList.size(); i++)
			if (roomList.get(i).getName().equalsIgnoreCase(name))
				return i;
		return -1;
	}

	public void removeRoom(int pos) {
		roomList.remove(pos);
	}

	public void addRoom(String name, int capacity, String type) {
		if (searchRoom(name) == -1) {
			roomList.add(new RoomSet(name, capacity, type));
			setStatus(false);
		} else
			JOptionPane.showMessageDialog(null, "Duplicate Entry : " + name,
					"Duplicate", JOptionPane.INFORMATION_MESSAGE);
	}

	public int getRoomLength() {
		return roomList.size();
	}

	public Object[] getRoomRecord(int i) {
		Object[] obj = { roomList.get(i).getName(), roomList.get(i).getType(),
				roomList.get(i).getCapacity() };
		return obj;
	}

	// Activity////////////////////////////////////////////////

	public ArrayList<ActivitySet> getActivityList() {
		return activityList;
	}

	public void removeActivity(int pos) {
		activityList.remove(pos);
	}

	public void addActivity(int i,ActivitySet act) {
		activityList.add(i, act);
		setStatus(false);
	}

	public ActivitySet getActivity(int i) {
		return activityList.get(i);
	}

	public int getActivityLength() {
		return activityList.size();
	}

	public Object[] getActivityRecord(int i) {
		Object[] obj = { "ID :"+ activityList.get(i).getId(),
				"Tag :"+ activityList.get(i).getTag(), 
				"Subject :"+ activityList.get(i).getSubject(), 
				"Student :"+ activityList.get(i).getStudent(), 
				"Teacher :"+ activityList.get(i).getTeacher(),
				"Duration :"+ activityList.get(i).getDuration()
				 };

		return obj;
	}

	// AssigndSubjects////////////////////////////////////////////////

	public ArrayList<AssigndSubjects> getAssigndSubjectList() {
		return assignSubList;
	}

	public AssigndSubjects getAssigndSubject(int i) {
		return assignSubList.get(i);
	}

	public int searchAssigndSubject(String name) {
		for (int i = 0; i < getAssigndSubjectLength(); i++)
			if (assignSubList.get(i).getSubj().getName().equals(name))
				return i;
		return -1;
	}

	public int getAssigndSubjectLength() {
		return assignSubList.size();
	}

	public String getSubjectsTHRecord(int i) {
		String obj = "[Subject : " + assignSubList.get(i).getSubj().getName()
				+ "]";
		obj += "[TH: ";
		obj += "[Teacher : " + assignSubList.get(i).getTchTH() + "]";
		obj += "[Students : " + assignSubList.get(i).getStudTH() + "]";
		return obj;
	}

	public String getSubjectsPRRecord(int i) {
		String obj = "[PR: ";

		for (int j = 0; j < assignSubList.get(i).getTchPR().size(); j++)
			obj += "[Teacher : " + assignSubList.get(i).getTchPR().get(j) + "]";

		for (int j = 0; j < assignSubList.get(i).getStudPR().size(); j++)
			obj += "[Students : " + assignSubList.get(i).getStudPR().get(j)
					+ "]";
		return obj;
	}

	public void copySubjToAssignSubj() {
		//if (assignSubList.size() < getSubjectLength())
		//	assignSubList.clear();

		for (int i = 0; i < getSubjectLength(); i++) {
			if (assignSubList.size() <= i) {
				AssigndSubjects temp = new AssigndSubjects();
				temp.setSubj(getSubject(i));
				assignSubList.add(i, temp);
				System.out.println("i::" + i);
			}
		}
	}

	public void updateAssignSubj() {
		//System.out.println("updateAssignSubj\n\n");
		for (int i = 0; i < getAssigndSubjectLength(); i++) {
			AssigndSubjects temp = assignSubList.get(i);

			if(searchSubject(temp.getSubj().getName())==-1)
				assignSubList.remove(i);
			else{
				//System.out.println("Subj: " + temp.getSubj().getName());
				
				if(temp.getStudTH()!=null)
				if(searchStudent(temp.getStudTH())==-1){
					update(temp.getStudPR(),0);
					temp.setStudTH(null);
				}
				
				if(temp.getTchTH()!=null)
				if(searchTeacher(temp.getTchTH())==-1)
					temp.setTchTH(null);
				update(temp.getTchPR(),1);
			}
		}
	}

	public void update(ArrayList<String> str,int t) {
		for (int i = 0; i < str.size(); i++){
			switch(t){
			case 0:
				str.clear();
				break;
			case 1:
				if(searchTeacher(str.get(i))==-1)	
				   str.remove(i);
				break;
			}
		}
	}
	
	public boolean isUptodate(){
		if(getAssigndSubjectLength()==0)
			return false;
		for (int i = 0; i < getAssigndSubjectLength(); i++) {
			AssigndSubjects temp = assignSubList.get(i);
			int index=searchSubject(temp.getSubj().getName());

			if(index==-1)
				return false;
			else{
				//System.out.println("Subj: " + temp.getSubj().getName());
				if(getSubject(index).getTPPW()>0)
				if((temp.getStudTH()==null) || (temp.getTchTH()==null))
					return false;
				
				if(temp.getStudTH()!=null)
				if(searchStudent(temp.getStudTH())==-1)
					return false;
				if(temp.getTchTH()!=null)
				if(searchTeacher(temp.getTchTH())==-1)
					return false;
			}
		}		
		return true;
	}
	/*
	 * public int searchAssignSubList(int WHAT,String KEY){ switch(WHAT){ case
	 * TEACHER:
	 * 
	 * return 0; case TEACHERPR: return 0; case STUDENT: return 0; case
	 * STUDENTPR: return 0; } return -1; }
	 */

	public Vector<String> getTeacherNames() {
		Vector<String> names = new Vector<String>();
		for(int i=0;i<getTeacherLength();i++)
			names.add(i, getTeacher(i).getName());
		return names;
	}

	public Vector<String> getStudentNames() {
		Vector<String> names = new Vector<String>();
		for(int i=0,k=0;i<getStudentLength();i++){
			names.add(k++, getStudent(i).getName());
			for(int j=0;j<getStudent(i).getGroupList().size();j++)
				names.add(k++, getStudent(i).getGroupName(j));
		}
		return names;
	}

	public Vector<String> getRoomNames() {
		Vector<String> names = new Vector<String>();
		for(int i=0;i<getRoomLength();i++)
			names.add(i, getRoom(i).getName());
		return names;
	}

	/////////////////////////////////////////////////////////////////

	public Rules getRule() {
		return ruleSet;
	}

}
