/*
<Time_Constraints_List>
	<ConstraintBasicCompulsoryTime>
		<Weight_Percentage>100</Weight_Percentage>
	</ConstraintBasicCompulsoryTime>

	<ConstraintBreakTimes>
		<Weight_Percentage>100</Weight_Percentage>
		<Number_of_Break_Times></Number_of_Break_Times>
		<Break_Time>
			<Day></Day>
			<Hour></Hour>
		</Break_Time>
	</ConstraintBreakTimes>

	<ConstraintMinNDaysBetweenActivities>
		<Weight_Percentage>95</Weight_Percentage>
		<Consecutive_If_Same_Day>yes</Consecutive_If_Same_Day>
		<Number_of_Activities>4</Number_of_Activities>
		<Activity_Id>19</Activity_Id>
		<Activity_Id>20</Activity_Id>
		<Activity_Id>21</Activity_Id>
		<Activity_Id>22</Activity_Id>
		<MinDays>1</MinDays>
	</ConstraintMinNDaysBetweenActivities>

	<ConstraintTeacherNotAvailableTimes>
		<Weight_Percentage>100</Weight_Percentage>
		<Teacher>L.Singh</Teacher>
		<Number_of_Not_Available_Times>45</Number_of_Not_Available_Times>
		<Not_Available_Time>
			<Day>Monday</Day>
			<Hour>09:30</Hour>
		</Not_Available_Time>
	</ConstraintTeacherNotAvailableTimes>
</Time_Constraints_List>

<Space_Constraints_List>
	<ConstraintBasicCompulsorySpace>
		<Weight_Percentage>100</Weight_Percentage>
	</ConstraintBasicCompulsorySpace>

	<ConstraintStudentsSetHomeRoom>
		<Weight_Percentage>100</Weight_Percentage>
		<Students>FFY</Students>
		<Room>Room 1</Room>
	</ConstraintStudentsSetHomeRoom>

	<ConstraintSubjectActivityTagPreferredRooms>
		<Weight_Percentage>100</Weight_Percentage>
		<Subject>DSP</Subject>
		<Activity_Tag>Practical</Activity_Tag>
		<Number_of_Preferred_Rooms>2</Number_of_Preferred_Rooms>
		<Preferred_Room>SP Lab</Preferred_Room>
		<Preferred_Room>SE Lab1</Preferred_Room>
	</ConstraintSubjectActivityTagPreferredRooms>
</Space_Constraints_List>
<Activity_Tags_List>
	<Activity_Tag></Activity_Tag>
</Activity_Tags_List>

<Activities_List>
	<Activity>
		<Id></Id>
		<Activity_Tag></Activity_Tag>
		<Teacher></Teacher>
		<Subject></Subject>
		<Total_Duration></Total_Duration>
		<Activity_Group_Id></Activity_Group_Id>
		<Students></Students>
	</Activity>
</Activities_List>

 */


package DataSourceKB;

import java.util.ArrayList;

public class ActivitySet {
	private int id;
	private String tag;
	private String teacher;
	private String student;
	private String subject;
	private String homeRoom;
	private ArrayList<String> room;
	private int Duration;

	public ActivitySet(int i){
		setId(i);
		room=new ArrayList<String>();
	}

	public void setHomeRoom(String homeRoom) {
		this.homeRoom = homeRoom;
	}

	public String getHomeRoom() {
		return homeRoom;
	}

	public ArrayList<String> getRooms() {
		return room;
	}

	public void setId(int i) {
		id = i;
	}

	public int getId() {
		return id;
	}

	public void setTag(String acttag) {
		tag = acttag;
	}

	public String getTag() {
		return tag;
	}

	public void setSubject(String subj) {
		subject = subj;
	}

	public String getSubject() {
		return subject;
	}

	public void setStudent(String stud) {
		student=stud;
	}

	public String getStudent() {
		return student;
	}

	public void setDuration(int Dur) {
		Duration = Dur;
	}

	public int getDuration() {
		return Duration;
	}
	
	public void setTeacher(String tch) {
		teacher=tch;
	}

	public String getTeacher() {
		return teacher;
	}

}
