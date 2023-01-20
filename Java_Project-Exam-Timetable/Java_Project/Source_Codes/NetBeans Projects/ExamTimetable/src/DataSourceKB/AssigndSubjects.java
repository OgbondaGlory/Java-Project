package DataSourceKB;

import java.util.ArrayList;

public class AssigndSubjects {
	private ArrayList<String> studNamePR;
	private ArrayList<String> tchNamePR;	
	private ArrayList<String> roomNamePR;	
	private String roomNameTH;
	private String studNameTH;
	private String tchNameTH;
	private SubjectSet subj;
	
	public AssigndSubjects(){
		studNamePR=new ArrayList<String>() ;
		tchNamePR=new ArrayList<String>();
		roomNamePR=new ArrayList<String>();
	}
	
	public void setSubj(SubjectSet subj) {
		this.subj = subj;
	}
	
	public SubjectSet getSubj() {
		return subj;
	}
	
	public void setRoomTH(String s) {
		roomNameTH=s;
	}
	
	public String getRoomTH() {
		return roomNameTH;
	}

	public void setStudTH(String s) {
		studNameTH=s;
	}
	
	public String getStudTH() {
		return studNameTH;
	}

	public void setTchTH(String s) {
		tchNameTH=s;
	}
	
	public String getTchTH() {
		return tchNameTH;
	}
	
	public ArrayList<String> getRoomPR() {
		return roomNamePR;
	}
	
	public ArrayList<String> getStudPR() {
		return studNamePR;
	}
	
	public ArrayList<String> getTchPR() {
		return tchNamePR;
	}
	
	public void clearRoomPR() {
		 roomNamePR.clear();
	}
	
	public void clearStudPR() {
		 studNamePR.clear();
	}
	
	public void clearTchPR() {
		 tchNamePR.clear();
	}
}
