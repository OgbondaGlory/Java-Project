package DataSourceKB;

public class TeacherSet {
	private String name;
	private String type;

	public TeacherSet(String Name, String Type) {
		name = Name;
		type=Type;
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public void setName(String s) {
		name=s;
	}
	
	public void setType(String s) {
		type=s;
	}

	
}
