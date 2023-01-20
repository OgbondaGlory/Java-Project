package ETS;

/*
 * @(#)MasterData.java	1.14 04/07/26
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ETS.Timetable.ETSGenerator;

import DataSourceKB.DataSet;
import DataSourceKB.StudentSet;
import DataSourceKB.SubjectSet;

/**
 * Internal Frames Demo
 */
public class ActivityPanel extends ETSModule {
	
	JDialog actDialog;
	JPanel activityBasePanel;
	JButton clearTBtn, clearSBtn, assignTchSub, assignStudSub;
	JTabbedPane tabbedpane;
	JList ACTList,TList, TSList, SList, SSList, SUBList;
	JTextArea ACTTextArea,SUBTextArea;

	SubjectSet selSubSet;
	JComboBox jcbATag, jcbSub;
	JTextField jtfAPW, jtfDura;
	String selSub,selActTag;
	
	Vector<String> TVList = new Vector<String>();
	Vector<String> TSVList = new Vector<String>();
	Vector<String> SVList = new Vector<String>();
	Vector<String> SSVList = new Vector<String>();
	Vector<String> ActDataList = new Vector<String>();
	Vector<String> SUBVList = new Vector<String>();

	public JPanel getActivityPanel() {
		return activityBasePanel;
	}

	public void UploadListData(int c) {		
		switch (c) {
		case DataSet.TEACHER:
			TVList.clear();
			for (int i = 0; i < getETSData().getTeacherLength(); i++)
				TVList.add(i, getETSData().getTeacher(i).getName());
			if(TVList.isEmpty()){
				TVList.add("------(No Teacher)------");
				TList.setEnabled(false);
				TSList.setEnabled(false);
			}else{
				TList.setEnabled(true);
				TSList.setEnabled(true);
			}
			TList.setListData(TVList);
			break;
		case DataSet.STUDENT:
			SVList.clear();
			for (int i = 0; i < getETSData().getStudentLength(); i++) 
				SVList.add(getETSData().getStudent(i).getName());
			if(SVList.isEmpty()){
				SVList.add("------(No Student)------");
				SList.setEnabled(false);
				SSList.setEnabled(false);
			}else{
				SList.setEnabled(true);
				SSList.setEnabled(true);
			}
			SList.setListData(SVList);
			break;
		case DataSet.SUBJECT:
			jcbSub.removeAllItems();
			jcbSub.updateUI();
			if (getETSData().getSubjectLength() == 0)
				jcbSub.addItem("No Subject");
			else
				jcbSub.addItem("Select Subject");
			for (int i = 0; i < getETSData().getSubjectLength(); i++)
				jcbSub.addItem(getETSData().getSubject(i).getName());
			break;
		case DataSet.ACTIVITY:
			ActDataList.clear();
			for (int i = 0; i < getETSData().getActivityLength(); i++){
				String s="| ";
				for(int j=0;j<getETSData().getActivityRecord(i).length;j++)
				 s += String.valueOf(getETSData().getActivityRecord(i)[j])+" | " ;
				ActDataList.add(i,s);
			}
			ACTTextArea.setText("Activity Details:\n======================");
			ACTList.removeAll();
			ACTList.setListData(ActDataList);
			break;
		case DataSet.ASSIGNSUBJECT:
			getETSData().copySubjToAssignSubj();
			getETSData().updateAssignSubj();
			SUBList.removeAll();
			SUBVList.clear();
			for (int i = 0; i < getETSData().getAssigndSubjectLength(); i++){
				String s=getETSData().getSubjectsTHRecord(i);
				s+=getETSData().getSubjectsPRRecord(i);
				SUBVList.add(i,s);
			}
			SUBList.setListData(SUBVList);
			break;
		}
	}

	/**
	 * MasterData Constructor
	 */
	
	public ActivityPanel(ETS ets) {
		super(ets, "", "");

		ActDataList = new Vector<String>();
		TVList = new Vector<String>();
		TSVList = new Vector<String>();
		SVList = new Vector<String>();
		SSVList = new Vector<String>();
		SUBVList = new Vector<String>();
		TList = new JList();
		SList = new JList();
		TSList = new JList();
		SSList = new JList();
		
		TSList.setName("'Selected Teacher'");
		SSList.setName("'Selected Student'");
		jcbSub = new JComboBox();
		jcbATag = new JComboBox();
		jcbATag.setModel(new DefaultComboBoxModel(new String[] { "Select Tag", "Theory", "Practical"}));
		selSub="";
		selActTag="";
		jtfAPW=new JTextField();
		jtfDura=new JTextField();
		jtfAPW.setColumns(10);
		jtfDura.setColumns(10);
		jtfAPW.setText("Activity/Week");
		jtfDura.setText("Duration");
		jtfAPW.setEditable(false);
		jtfDura.setEditable(false);

		clearTBtn = new JButton("Clear");
		clearSBtn = new JButton("Clear");
		
		TList.setDragEnabled(true);
		TList.setDropTarget(getDND(TSVList, TList, TSList));
		TSList.setDropTarget(getDND(TSVList, TList, TSList));

		SList.setDragEnabled(true);
		SList.setDropTarget(getDND(SSVList, SList, SSList));
		SSList.setDropTarget(getDND(SSVList, SList, SSList));

		ACTList = new JList();
		ACTTextArea = new JTextArea();
		ACTTextArea.setEditable(false);
		ACTTextArea.setText("Activity Details:\n======================");
		ACTList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(ACTList.getSelectedIndex()>-1)
					ACTTextArea.setText("Activity Details:\n======================\n"
					+ ActDataList.get(ACTList.getSelectedIndex()).replace("|", "\n"));
			}
		});

		SUBList = new JList();
		SUBTextArea = new JTextArea();
		SUBTextArea.setEditable(false);
		SUBTextArea.setText("Subjects Details:\n======================");
		SUBList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(SUBList.getSelectedIndex()>-1){
					jcbSub.setSelectedIndex(SUBList.getSelectedIndex()+1);
					SUBTextArea.setText("Subjects Details:\n======================\n"
					+ SUBVList.get(SUBList.getSelectedIndex()).replace("[", "\n").replace("]", "."));
				}
			}
		});
		

		clearTBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TSList.removeAll();
				TSList.updateUI();
				TSVList.clear();
			}
		});

		clearSBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SSList.removeAll();
				SSList.updateUI();
				SSVList.clear();
			}
		});

		jcbSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbSub.getItemCount() > 0){
					if (!jcbSub.getSelectedItem().toString().contains("Subject")){
						SUBList.setSelectedIndex(jcbSub.getSelectedIndex()-1);
						selSub=jcbSub.getSelectedItem().toString();
						selSubSet=getETSData().getSubject(getETSData().searchSubject(selSub));
					}else {
						jcbATag.setSelectedIndex(0);
						selSub="";
						selActTag="";
					}
					UpdateJCB();
				}
			}
		});
		
		jcbATag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbATag.getItemCount() > 0){
					if (!jcbATag.getSelectedItem().toString().contains("Tag"))
						selActTag=jcbATag.getSelectedItem().toString();
					else {
						jcbSub.setSelectedIndex(0);
						selSub="";
						selActTag="";
					}
					UpdateJCB();
				}
			}
		});
		activityBasePanel = getActContentBasePanel();
	}
	
	protected void UpdateJCB() {

		if(!selActTag.equalsIgnoreCase("") && !selSub.equalsIgnoreCase("")){
		String name,tppw,pppw;
		name=selSubSet.getName();
		tppw=String.valueOf(selSubSet.getTPPW()/Integer.parseInt(getString("MasterData.Dur_Theory")));
		pppw=String.valueOf(selSubSet.getPPPW()/Integer.parseInt(getString("MasterData.Dur_Practical")));
			if(selActTag.equalsIgnoreCase("Theory")){
				jtfAPW.setText(tppw);
				jtfDura.setText(getString("MasterData.Dur_Theory"));			
			}else if(selActTag.equalsIgnoreCase("Practical")){
				jtfAPW.setText(pppw);
				jtfDura.setText(getString("MasterData.Dur_Practical"));						
			}else{
				jtfAPW.setText("Activity/Week");
				jtfDura.setText("Duration");
			}
		}else{
			jtfAPW.setText("Activity/Week");
			jtfDura.setText("Duration");
		}
	}

	protected JPanel getSubAssignPanel(String s,int i) {
		assignTchSub = new JButton("Assign It");
		assignStudSub = new JButton("Assign It");
		
		JPanel activityPanel = new JPanel();

		JPanel act1Panel , act2Panel ;
		act1Panel = getActInnerPanel("Subject Settings : ",jcbATag,jcbSub,jtfDura,jtfAPW);
		if(i==0) act2Panel = getActInnerPanel("Teacher List : ",TList,TSList,clearTBtn,null);
		else if(i==1) act2Panel = getActInnerPanel("Student List : ",SList,SSList,clearSBtn,null);		
		else act2Panel = new JPanel();			
		
		activityPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 2, 10, 2);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx =1;
		gbc.gridy =1;
		activityPanel.add(act1Panel,gbc);
		gbc.gridy =10;
		activityPanel.add(act2Panel,gbc);
		
		JPanel SubPanel = new JPanel();
		SubPanel.setLayout(new BorderLayout());
		JPanel jp = new JPanel();
		jp.setBorder(new TitledBorder(""));
		jp.add(new JLabel(s));
		if(i==0)
		jp.add(assignTchSub);
		else if(i==1)
		jp.add(assignStudSub);
		
		assignTchSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(selSubSet!=null){
					int i= getETSData().searchAssigndSubject(selSubSet.getName());
					if(TSList.getModel().getSize()>0){
						String s= TSList.getModel().getElementAt(0).toString();
						assignTchSub_Func(i,s);
					}
					else
					MSGBOX("Empty", "Must Select atmost one Invigilator",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		assignStudSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(selSubSet!=null){
					int i= getETSData().searchAssigndSubject(selSubSet.getName());
					if(SSList.getModel().getSize()>0){
						String s= SSList.getModel().getElementAt(0).toString();
						assignStudSub_Func(i,s);
					}
					else
					MSGBOX("Empty", "Must Select atmost one Student set",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		JPanel subListPanel = new JPanel();
		subListPanel.setBorder(new TitledBorder(" Subjects List : "));
		subListPanel.setLayout(new GridLayout(1, 1, 10, 10));
		subListPanel.add(new JScrollPane(SUBList));

		JPanel sidePanel = new JPanel();
		sidePanel.setBorder(new TitledBorder(" Subjects Description : "));
		sidePanel.setLayout(new GridLayout(1, 1, 10, 10));
		sidePanel.add(new JScrollPane(SUBTextArea));
		
		SubPanel.add(activityPanel,BorderLayout.LINE_START);
		SubPanel.add(subListPanel,BorderLayout.CENTER);
		SubPanel.add(sidePanel,BorderLayout.LINE_END);
		SubPanel.add(jp,BorderLayout.SOUTH);
		
		return SubPanel;
	}

	protected void assignTchSub_Func(int i, String s) {
		if(selActTag.equalsIgnoreCase("Theory")){
			if(TSList.getModel().getSize()>1){
				MSGBOX("Invalid Selction", "Must Select only one Invigilator",JOptionPane.INFORMATION_MESSAGE);
				TSList.removeAll();
				TSList.updateUI();
				TSVList.clear();
			}else getETSData().getAssigndSubject(i).setTchTH(s);
		}
		else if(selActTag.equalsIgnoreCase("Practical")){
			int slimit=getETSData().getAssigndSubject(i).getStudPR().size();
			if(TSList.getModel().getSize() > slimit){
				MSGBOX("Invalid Selection", "Must Select less than " +slimit +" Teachers",JOptionPane.INFORMATION_MESSAGE);
				TSList.removeAll();
				TSList.updateUI();
				TSVList.clear();
			}else{
				getETSData().getAssigndSubject(i).clearTchPR();
				for(int j = 0;j<TSList.getModel().getSize();j++){
					String ss=TSList.getModel().getElementAt(j).toString();
					getETSData().getAssigndSubject(i).getTchPR().add(j,ss);
				}
			} 
		}else{
			MSGBOX("Empty","Select Tag First!",JOptionPane.INFORMATION_MESSAGE);
		}
		UploadListData(DataSet.ASSIGNSUBJECT);
	}

	protected void assignStudSub_Func(int i,String s) {
		if(selActTag.equalsIgnoreCase("Theory")){
            if(SSList.getModel().getSize()>1){
				MSGBOX("Invalid Selection", "Must Select only one Student",JOptionPane.INFORMATION_MESSAGE);
				SSList.removeAll();
				SSList.updateUI();
				SSVList.clear();
			}else getETSData().getAssigndSubject(i).setStudTH(s);
		}
		else if(selActTag.equalsIgnoreCase("Practical")){
			if(SSList.getModel().getSize() > 1){
				MSGBOX("Invalid  Selection", "Must Select only one " +
				"\n The batches related classes automatically gets added!",JOptionPane.INFORMATION_MESSAGE);
				SSList.removeAll();
				SSList.updateUI();
				SSVList.clear();
			}else {
				getETSData().getAssigndSubject(i).clearStudPR();
				StudentSet stud =getETSData().getStudent(getETSData().searchStudent(s));
				
				if(stud.getGroupList().size()>0)
				for(int j = 0;j<stud.getGroupList().size();j++){
					s=stud.getGroupName(j);
					getETSData().getAssigndSubject(i).getStudPR().add(j,s);
				}
				else MSGBOX("Group creation Error", "The groups are not created!",JOptionPane.ERROR_MESSAGE);
			}
		}else{
			MSGBOX("Invalid Selection","Select Tag First!",JOptionPane.INFORMATION_MESSAGE);
		}
		UploadListData(DataSet.ASSIGNSUBJECT);
	}

	protected JPanel getActContentBasePanel() {
		JPanel actListPanel = new JPanel();
		actListPanel.setBorder(new TitledBorder(" Activity List : "));
		actListPanel.setLayout(new GridLayout(1, 1, 10, 10));
		
		JPanel sidePanel = new JPanel();
		sidePanel.setBorder(new TitledBorder(" Activity Description : "));
		sidePanel.setLayout(new GridLayout(1, 1, 10, 10));
		
		actListPanel.add(new JScrollPane(ACTList));
		sidePanel.add(new JScrollPane(ACTTextArea));

		JPanel btnPanel = new JPanel();
		btnPanel.setBorder(new LineBorder(Color.WHITE, 2));
		btnPanel.setLayout(new GridLayout(1, 3, 10, 10));

		JButton relTchSub = new JButton("Course - Invigilator");
		relTchSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actDialog = new JDialog(ets.getFrame(), "Course - Invigilator Assignment", true);
				actDialog.setSize(900, 600);
				actDialog.setResizable(false);
				actDialog.setLocation(100, 100);
				actDialog.setContentPane(getSubAssignPanel(getString("Activity.relTchSub"),0));
				actDialog.show();
			}
		});
		
		JButton relStudSub = new JButton("Course - Student");
		relStudSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actDialog = new JDialog(ets.getFrame(), "Course - Student Assignment", true);
				actDialog.setSize(900, 600);
				actDialog.setResizable(false);
				actDialog.setLocation(100, 100);
				actDialog.setContentPane(getSubAssignPanel(getString("Activity.relStudSub"),1));
				actDialog.show();
			}
		});

		JButton genAct = new JButton("Generate Activities");
		genAct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getETSData().isUptodate()){
					ETSGenerator tg=getETSGenerator();
					tg.GenerateActivities();
					if(tg.isActivitiesGenerated())
						JOptionPane.showMessageDialog(null, "Activities Generated ");
					else
						JOptionPane.showMessageDialog(null, "Error: No Activities Generated");
				}
				else
				JOptionPane.showMessageDialog(null, "Error: No Activities Generated");
				UploadListData(DataSet.ACTIVITY);
			}
		});
		
		btnPanel.add(relStudSub);
		btnPanel.add(relTchSub);
		btnPanel.add(genAct);

		JPanel ActContentBasePanel = new JPanel();
		ActContentBasePanel.setLayout(new BorderLayout());
		ActContentBasePanel.add(new JLabel(getString("Activity.description")),BorderLayout.NORTH);
		ActContentBasePanel.add(actListPanel, BorderLayout.CENTER);
		ActContentBasePanel.add(sidePanel, BorderLayout.LINE_END);
		ActContentBasePanel.add(btnPanel, BorderLayout.SOUTH);

		return ActContentBasePanel;
	}

	protected JPanel getActInnerPanel(String title,Component comp1,Component comp2,Component comp3,Component comp4) {

		JPanel activityInnerPanel = new JPanel();
		JScrollPane tmp;
		
		activityInnerPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx =1;
		gbc.gridy =20;
		if(title.contains("List")){
			tmp = new JScrollPane(comp1);
			tmp.setPreferredSize(new Dimension(200, 225));
			activityInnerPanel.add(tmp, gbc);
			
			gbc.gridx = 20;
			tmp = new JScrollPane(comp2);
			tmp.setPreferredSize(new Dimension(200, 225));
			activityInnerPanel.add(tmp, gbc);
		}else{
			gbc.gridx =1;
			gbc.gridy =20;
			activityInnerPanel.add(comp1, gbc);

			gbc.gridx = 20;
			activityInnerPanel.add(comp2, gbc);			
		}
		//comp1,comp2,comp3,comp4
		//jcbATag,jcbSub,jtfDura,jtfAPW		
		if(comp4!=null){
			gbc.gridx = 1;
			gbc.gridy = 30;
			activityInnerPanel.add(new JLabel("Duration"), gbc);
			gbc.gridx = 20;
			activityInnerPanel.add(comp3, gbc);			
			gbc.gridx = 1;
			gbc.gridy = 40;
			activityInnerPanel.add(new JLabel("Activity/Week"), gbc);
			gbc.gridx = 20;
			activityInnerPanel.add(comp4, gbc);
		}else {
			gbc.gridx = 20;
			gbc.gridy = 30;
			activityInnerPanel.add(comp3, gbc);			
		}

		JLabel tmplbl;
		if(title.contains("Teacher"))
			tmplbl=new JLabel(getString("Activity.tch_description"));
		else if(title.contains("Student"))
			tmplbl=new JLabel(getString("Activity.stud_description"));
		else
		    tmplbl=new JLabel(getString("Activity.subjtag_description"));
		
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new GridBagLayout());		
		tempPanel.setBorder(new TitledBorder(title));
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 0, 1, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx =1;
		gbc.gridy =1;
		tempPanel.add(tmplbl,gbc);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx =1;
		gbc.gridy =20;
		tempPanel.add(activityInnerPanel,gbc);
		
		return tempPanel;
	}

	protected DropTarget getDND(final Vector<String> selVList, final JList oList,
			final JList selList) {
		DropTarget dnd = new DropTarget(selList, new DropTargetListener() {
			public void dragEnter(DropTargetDragEvent arg0) {
			}

			public void dragExit(DropTargetEvent arg0) {
			}

			public void dragOver(DropTargetDragEvent arg0) {
			}

			public void drop(DropTargetDropEvent arg0) {
				try {
					if (!isDuplicate(selVList, oList.getSelectedValue().toString(), selList)) {
						selVList.add(oList.getSelectedValue().toString());
						selList.setListData(selVList);
					}
				} catch (Exception e) {
					System.out.println(arg0.getSource().toString() + ":"+ e.getMessage());
				}
				selList.clearSelection();
				oList.clearSelection();
			}

			private boolean isDuplicate(Vector<String> vList, String key,JList selList) {
				if (vList.isEmpty())
					return false;
				else {
					for (int i = 0; i < vList.size(); i++) {
						if (vList.get(i).equals(key))
							return true;
					}
				}
				return false;
			}
			public void dropActionChanged(DropTargetDragEvent arg0) {
			}
		});
		return dnd;
	}
}
