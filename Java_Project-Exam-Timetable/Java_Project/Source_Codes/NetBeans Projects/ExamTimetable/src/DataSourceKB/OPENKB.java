package DataSourceKB;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import ETS.ETS;
import ETS.ETSModule;


public class OPENKB extends ETSModule {
	private static final long serialVersionUID = 1L;

	public OPENKB(ETS ets) {
		super(ets, "", "");
	}

	@SuppressWarnings("unchecked")
	public void readData(String filename) {
		setETSData();
		Document doc;

		try {
			List<Element> innerEle;
			List<Element> groupElements;
			doc = new SAXReader().read(new File(filename));
			String _collageName =doc.getRootElement().attributeValue("College_Name");
			String _deptName =doc.getRootElement().attributeValue("Department_Name");
			getETSData().getRule().setCollageName(_collageName);
			getETSData().getRule().setDeptName(_deptName);
			System.out.println(_collageName);
			System.out.println(_deptName);
			XPath catPath = DocumentHelper.createXPath("//Teachers_List/Teacher");
			List<Element> catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getETSData().addTeacher(element.elementText("Name"),
						element.elementText("Type"));
			}

			catPath = DocumentHelper.createXPath("//Subjects_List/Subject");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getETSData().addSubject(element.elementText("Name"),
						Integer.parseInt(element.elementText("No_Theory")),
						Integer.parseInt(element.elementText("No_Practical")));
			}

			catPath = DocumentHelper.createXPath("//Students_List/Student");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getETSData().addStudent(element.elementText("Name"),
						Integer.parseInt(element.elementText("Strength")));

				if (element.element("Group") != null) {
					groupElements = element.elements("Group");
					for (Iterator<Element> itg = groupElements.iterator(); itg.hasNext();) {
						Element Gelement = itg.next();
						//System.out.println(Gelement.getName());
						getETSData().addGroup(element.elementText("Name"),Gelement.elementText("Name"),Integer.parseInt(Gelement.elementText("Strength")));
					}
				}
			}

			catPath = DocumentHelper.createXPath("//Subjects_Alloc_List/SubjectAlloc");
			catElements = catPath.selectNodes(doc);

			if(getETSData().getAssigndSubjectLength()<getETSData().getSubjectLength())
				getETSData().copySubjToAssignSubj();
			int subjlen=0;
			int PR=0,TH=0;
			String s;
			for ( Iterator<Element> it = catElements.iterator(); it.hasNext();subjlen++) {
				Element element = it.next();
				String Subj=element.elementText("Name");
				//System.out.println(Subj);

				if (element.element("Theory") != null) {
					TH+=getETSData().getSubject(subjlen).getTPPW();
					Element Nelement = element.element("Theory");
					s=Nelement.elementText("Teacher");
					getETSData().getAssigndSubject(subjlen).setTchTH(s);
					//System.out.println("TH "+Nelement.elementText("Teacher"));
					
					s=Nelement.elementText("Student");
					getETSData().getAssigndSubject(subjlen).setStudTH(s);
					//System.out.println("TH "+Nelement.elementText("Student"));
				}
				
				if (element.element("Practical") != null) {
					groupElements = element.elements("Practical");
					for (Iterator<Element> itn = groupElements.iterator(); itn.hasNext();) {
						Element Nelement = itn.next();	
						innerEle=Nelement.elements("Teacher");
						for(int i=0;i<innerEle.size();i++){
							s=innerEle.get(i).getText();
							getETSData().getAssigndSubject(subjlen).getTchPR().add(s);
							//System.out.println("PR "+ s);
						}
						innerEle=Nelement.elements("Student");
						for(int i=0;i<innerEle.size();i++){
							PR++;//=getETSData().getSubject(index).getPPPW();
							s=innerEle.get(i).getText();
							getETSData().getAssigndSubject(subjlen).getStudPR().add(s);
							//System.out.println("PR "+ s);
						}						
					}
				}
			}
			getETSData().setTHPR(TH, PR);
			System.out.println(getETSData().getPR()+":0:"+getETSData().getTH());

			catPath = DocumentHelper.createXPath("//Rooms_List/Room");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getETSData().addRoom(element.elementText("Name"),
						Integer.parseInt(element.elementText("Capacity")),
						element.elementText("Type"));
			}

			catPath = DocumentHelper.createXPath("//Days_List/Day");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getETSData().getTimeSet().getDay().add(
						element.elementText("Name"));
			}

			catPath = DocumentHelper.createXPath("//Hours_List/Hour");
			catElements = catPath.selectNodes(doc);
			for (Iterator<Element> it = catElements.iterator(); it.hasNext();) {
				Element element = it.next();
				getETSData().getTimeSet().getHour().add(
						element.elementText("Name"));
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
		
}
