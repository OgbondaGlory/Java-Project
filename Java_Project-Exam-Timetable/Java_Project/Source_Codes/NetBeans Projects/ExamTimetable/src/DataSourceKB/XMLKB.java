package DataSourceKB;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XMLKB {
	public static final String ext = ".ttgs.xml";
	DocumentBuilderFactory documentBuilderFactory;
	DocumentBuilder documentBuilder;
	Document document;

	Element rootElement;
	Element dayElement;
	Element hourElement;
	Element teacherElement;
	Element subjectElement;
	Element subjectAllocElement;
	Element studentElement;
	Element roomElement;
	Element activityElement;

/*
 *  OS=Windows_NT
 *  HOMEDRIVE=C:, 
 *  USERPROFILE=C:\Documents and Settings\Administrator, 
 *  COMPUTERNAME=STUDENT, 
 *  OS=Windows_NT, 
 *  USERNAME=Administrator, 
*/
	public XMLKB() {

	}

	public void NewKB() {
		try {
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();
			rootElement = document.createElement("ETS");
			dayElement = document.createElement("Days_List");
			hourElement = document.createElement("Hours_List");
			teacherElement = document.createElement("Teachers_List");
			subjectElement = document.createElement("Subjects_List");
			subjectAllocElement = document.createElement("Subjects_Alloc_List");
			studentElement = document.createElement("Students_List");
			roomElement = document.createElement("Rooms_List");
			activityElement = document.createElement("Activity_List");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void SaveKB(String XMLFile, DataSet ds) {

		for (int i = 0; i < ds.getTimeSet().getDay().size(); i++) {
			Element em = document.createElement("Day");
			em.appendChild(AddElement(ds.getTimeSet().getDay().get(i),DataSet.DATA_NAME));
			AddKBElement("Day", em);
		}
		for (int i = 0; i < ds.getTimeSet().getHour().size(); i++) {
			Element em = document.createElement("Hour");
			em.appendChild(AddElement(ds.getTimeSet().getHour().get(i),DataSet.DATA_NAME));
			AddKBElement("Hour", em);
		}
		for (int i = 0; i < ds.getTeacherLength(); i++) {
			TeacherSet temp=ds.getTeacher(i);
			AddKBElement("Teacher", AddTeacherKB(temp));
		}
		for (int i = 0; i < ds.getSubjectLength(); i++) {
			SubjectSet temp=ds.getSubject(i);
			AddKBElement("Subject", AddSubjectKB(temp));
		}
		for (int i = 0; i < ds.getStudentLength(); i++) {
			StudentSet temp=ds.getStudent(i);
			AddKBElement("Student", AddStudentKB(temp,true));
		}
		for (int i = 0; i < ds.getRoomLength(); i++) {
			RoomSet temp=ds.getRoom(i);
			AddKBElement("Room", AddRoomKB(temp));
		}
		for (int i = 0; i < ds.getAssigndSubjectLength(); i++) {
			AssigndSubjects temp=ds.getAssigndSubject(i);
			AddKBElement("SubjectAlloc", AddSubjectAllocKB(temp));
		}
		for (int i = 0; i < ds.getActivityLength(); i++) {
			ActivitySet temp=ds.getActivity(i);
			AddKBElement("Activity", AddActivityKB(temp));
		}
//		for (int i = 0; i < ds.getRulesLength(); i++) {
//			Rules temp=ds.getRule(i);
//			AddKBElement("Rule", AddRulesKB(temp));
//		}

		try {
			rootElement.setAttribute("College_Name", ds.getRule().getCollageName());
			rootElement.setAttribute("Department_Name", ds.getRule().getDeptName());
			rootElement.appendChild(dayElement);
			rootElement.appendChild(hourElement);
			rootElement.appendChild(studentElement);
			rootElement.appendChild(teacherElement);
			rootElement.appendChild(subjectElement);
			rootElement.appendChild(roomElement);
			rootElement.appendChild(subjectAllocElement);
			rootElement.appendChild(activityElement);

			document.appendChild(rootElement);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			if (XMLFile.endsWith(ext)){
				StreamResult result = new StreamResult(XMLFile);
				transformer.transform(source, result);				
			}
			else{
				StreamResult result = new StreamResult(XMLFile+ext);
				transformer.transform(source, result);				
			}
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}
	
	public Element AddTeacherKB(TeacherSet teacher) {
		Element em = document.createElement("Teacher");
		em.appendChild(AddElement(teacher.getName(),DataSet.DATA_NAME));
		em.appendChild(AddElement(teacher.getType(),DataSet.DATA_TYPE));
		return em;
	}
		
	public Element AddRoomKB(RoomSet room) {
		Element em = document.createElement("Room");
		em.appendChild(AddElement(room.getName(),DataSet.DATA_NAME));
		em.appendChild(AddElement(room.getType(),DataSet.DATA_TYPE));
		em.appendChild(AddElement(String.valueOf(room.getCapacity()), DataSet.DATA_CAPACITY));
		return em;
	}
		
	public Element AddSubjectKB(SubjectSet subj) {
		Element em = document.createElement("Subject");
		em.appendChild(AddElement(subj.getName(),DataSet.DATA_NAME));
		em.appendChild(AddElement(String.valueOf(subj.getTPPW()), DataSet.DATA_NOT));
		em.appendChild(AddElement(String.valueOf(subj.getPPPW()), DataSet.DATA_NOP));
		return em;
	}
	
	public Element AddSubjectAllocKB(AssigndSubjects subj) {
		Element em = document.createElement("SubjectAlloc");
		Element em1 = document.createElement("Theory");
		Element em2 = document.createElement("Practical");

		em1.appendChild(AddElement(subj.getTchTH(), "Teacher"));
		em1.appendChild(AddElement(subj.getStudTH(), "Student"));

		for(int i=0;i<subj.getTchPR().size();i++)
			  em2.appendChild(AddElement(subj.getTchPR().get(i), "Teacher"));
		for(int i=0;i<subj.getStudPR().size();i++)
			  em2.appendChild(AddElement(subj.getStudPR().get(i), "Student"));

		em.appendChild(AddElement(subj.getSubj().getName(),DataSet.DATA_NAME));
		
		if(subj.getSubj().getTPPW()>0)
		em.appendChild(em1);
		if(subj.getSubj().getPPPW()>0)
		em.appendChild(em2);
		return em;
	}
	
	public Element AddActivityKB(ActivitySet act) {
		Element em = document.createElement("Activity");
		em.setAttribute("ID",String.valueOf(act.getId()) );
		em.setAttribute("Tag", act.getTag());
		em.setAttribute("Subject", act.getSubject());
		em.setAttribute("Student", act.getStudent());
		em.setAttribute("Teacher", act.getTeacher());
		em.setAttribute("Duration", String.valueOf(act.getDuration()));

		String roomName="";
		if(act.getTag().equals("TH")){
			roomName=act.getHomeRoom();
			em.appendChild(AddElement(roomName, "Room"));
		}
		else if(act.getTag().equals("PR")){
			for(int i=0;i<act.getRooms().size();i++){
				roomName=act.getRooms().get(i);
				em.appendChild(AddElement(roomName, "Room"));
			}
		}
		return em;
	}
	
	public Element AddStudentKB(StudentSet stud, boolean b) {
		Element em = document.createElement("Student");
		em.appendChild(AddElement(stud.getName(),DataSet.DATA_NAME));
		em.appendChild(AddElement(String.valueOf(stud.getTotStud()), DataSet.DATA_STRENGTH));
		if(b)
		for (int j = 0; j < stud.getGroupList().size(); j++) {
			StudentSet gtemp=stud.getGroupList().get(j);
			Element group = document.createElement("Group");
			group.setAttribute("ID",String.valueOf(j));
			group.appendChild(AddElement(gtemp.getName(),DataSet.DATA_NAME));
			group.appendChild(AddElement(String.valueOf(gtemp.getTotStud()), DataSet.DATA_STRENGTH));
			em.appendChild(group);
		}
		return em;
	}
	
	public Element AddElement(String name, String caption) {
		Element em;
		if(name==null)
			name="-Not alloacte yet-";
		em = document.createElement(caption);
		em.appendChild(document.createTextNode(name));
		return em;
	}
	
	public Element AddElement(String name, int data) {
		Element em;
		switch (data) {
		case DataSet.DATA_NAME:
			em = document.createElement("Name");
			em.appendChild(document.createTextNode(name));
			return em;
		case DataSet.DATA_TYPE:
			em = document.createElement("Type");
			em.appendChild(document.createTextNode(name));
			return em;
		case DataSet.DATA_CAPACITY:
			em = document.createElement("Capacity");
			em.appendChild(document.createTextNode(name));
			return em;
		case DataSet.DATA_NOT:
			em = document.createElement("No_Theory");
			em.appendChild(document.createTextNode(name));
			return em;
		case DataSet.DATA_NOP:
			em = document.createElement("No_Practical");
			em.appendChild(document.createTextNode(name));
			return em;
		case DataSet.DATA_STRENGTH:
			em = document.createElement("Strength");
			em.appendChild(document.createTextNode(name));
			return em;
		default:
		}
		return null;
	}

	public void AddKBElement(String ele, Element em) {
		if (ele.equalsIgnoreCase("teacher"))
			teacherElement.appendChild(em);
		else if (ele.equalsIgnoreCase("subject"))
			subjectElement.appendChild(em);
		else if (ele.equalsIgnoreCase("subjectalloc"))
			subjectAllocElement.appendChild(em);
		else if (ele.equalsIgnoreCase("student"))
			studentElement.appendChild(em);
		else if (ele.equalsIgnoreCase("room"))
			roomElement.appendChild(em);
		else if (ele.equalsIgnoreCase("day"))
			dayElement.appendChild(em);
		else if (ele.equalsIgnoreCase("hour"))
			hourElement.appendChild(em);
		else if (ele.equalsIgnoreCase("activity"))
			activityElement.appendChild(em);
	}

}
