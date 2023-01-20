package DataSourceKB;

public class SubjectSet {
	private int pppw = 0;
	private int tppw = 0;
	private String name;

	public SubjectSet(String Name, int noT, int noP) {
		pppw = noP;
		tppw = noT;
		name = Name;
	}

	public String getName() {
		return name;
	}

	public int getPPPW() {
		return pppw;
	}

	public int getTPPW() {
		return tppw;
	}

	public void setName(String s) {
		name=s;
	}

	public void setPPPW(int i) {
		pppw=i;
	}

	public void setTPPW(int i) {
		tppw=i;
	}
}