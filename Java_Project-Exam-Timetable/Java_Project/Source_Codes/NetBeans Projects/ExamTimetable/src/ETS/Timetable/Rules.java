package ETS.Timetable;

public class Rules {
	private String Break;
	private String collageName;
	private String deptName;
	private Matrix3D<Boolean> roomRule;
	private Matrix3D<Boolean> tchRule;
	private Matrix3D<Boolean> studRule;

	public Rules(){
		setBreak("");
		setCollageName("Na");
		setDeptName("Na");
	}

	public void setCollageName(String _collageName) {
		this.collageName = _collageName;
	}

	public String getCollageName() {
		return collageName;
	}

	public void setDeptName(String _deptName) {
		this.deptName = _deptName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setBreak(String _break) {
		Break = _break;
	}

	public String getBreak() {
		return Break;
	}

	public void setRoom(Matrix3D<Boolean> room) {
		System.out.println("in setRoom");
		this.roomRule = room;
		roomRule.Init(true);
	}

	public Matrix3D<Boolean> getRoom() {
		return roomRule;
	}

	public void setTeacher(Matrix3D<Boolean> tch) {
		System.out.println("in setTeacher");
		this.tchRule = tch;
		tchRule.Init(true);
	}

	public Matrix3D<Boolean> getTeacher() {
		return tchRule;
	}
	
	public void setStudent(Matrix3D<Boolean> stud) {
		System.out.println("in setStudent");
		this.studRule = stud;
		studRule.Init(true);
	}

	public Matrix3D<Boolean> getStudent() {
		return studRule;
	}
}
