package DataSourceKB;

public class RoomSet {
	private String name;
	private int capacity;
	private String type;

	public RoomSet(String Name, int Capacity, String Type) {
		name = Name;
		type = Type;
		capacity = Capacity;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setName(String s) {
		 name=s;
	}

	public void setType(String s) {
		 type=s;
	}

	public void setCapacity(int i) {
		 capacity=i;
	}

	
}
