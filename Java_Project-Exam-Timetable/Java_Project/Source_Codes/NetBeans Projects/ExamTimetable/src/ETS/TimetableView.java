package ETS;

/*
 * @(#)Timetable.java	
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ETS.Timetable.OutputGenerator;

import DataSourceKB.DataSet;

public class TimetableView extends ETSModule implements ActionListener {
	private JTabbedPane tabbedpane;
	private ViewPanel TchTablePanel, StudTablePanel, Stud1TablePanel, RoomTablePanel;
	private OutputGenerator og;
	/**
	 * TimetableView Constructor
	 */
	public TimetableView(ETS ets) {
		// Set the title for this demo, and an icon used to represent this
		// demo inside the com.ETS app.
		super(ets, "TimetableView", "toolbar/Time16.png");

		// create tab
		tabbedpane = new JTabbedPane();
		getDemoPanel().add(tabbedpane, BorderLayout.CENTER);

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Rules / Constraints Manager
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		String name = getString("TimetableView.tch");
		TchTablePanel = new ViewPanel(name);
		tabbedpane.add(name, TchTablePanel);

		name = getString("TimetableView.stud");
		StudTablePanel = new ViewPanel(name);
		tabbedpane.add(name, StudTablePanel);

		name = getString("TimetableView.stud1");
		Stud1TablePanel = new ViewPanel(name);
		tabbedpane.add(name, Stud1TablePanel);

		name = getString("TimetableView.room");
		RoomTablePanel = new ViewPanel(name);
		tabbedpane.add(name, RoomTablePanel);

		tabbedpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				switch (tabbedpane.getSelectedIndex()) {
				case 0:
					og = new OutputGenerator("");
					og.SetData(getETSData(), getETSGenerator());
					TchTablePanel.AddJCB(getJCB(DataSet.TEACHER), og,getETSGenerator().isAllActivitiesPlace());
					TchTablePanel.setTable();
					break;
				case 1:
					og = new OutputGenerator("");
					og.SetData(getETSData(), getETSGenerator());
					StudTablePanel.AddJCB(getJCB(DataSet.STUDENT), og,getETSGenerator().isAllActivitiesPlace());
					StudTablePanel.setTable();
					break;
				case 2:
					og = new OutputGenerator("");
					og.SetData(getETSData(), getETSGenerator());
					Stud1TablePanel.AddJCB(getJCB(0), og,getETSGenerator().isAllActivitiesPlace());
					Stud1TablePanel.setTable();
					break;
				case 3:
					og = new OutputGenerator("");
					og.SetData(getETSData(), getETSGenerator());
					RoomTablePanel.AddJCB(getJCB(DataSet.ROOM), og,getETSGenerator().isAllActivitiesPlace());
					RoomTablePanel.setTable();
					break;
				default:
					break;
				}
			}
		});
	}

	public JComboBox getJCB(int c) {
		JComboBox jcb = new JComboBox();
		switch (c) {
		case DataSet.TEACHER:
			jcb.setName("TEACHER");
			if (getETSData().getTeacherLength() == 0)
				jcb.addItem("No Teacher");
			else
				jcb.addItem("Select Teacher");
			for (int i = 0; i < getETSData().getTeacherLength(); i++)
				jcb.addItem(getETSData().getTeacherNames().get(i));
			break;
		case DataSet.STUDENT:
			jcb.setName("STUDENT");
			if (getETSData().getStudentLength() == 0)
				jcb.addItem("No Student");
			else
				jcb.addItem("Select Student");
			for (int i = 0; i < getETSData().getAllStudentLength(); i++)
				jcb.addItem(getETSData().getStudentNames().get(i));
			break;
		case DataSet.ROOM:
			jcb.setName("ROOM");
			if (getETSData().getRoomLength() == 0)
				jcb.addItem("No Room");
			else
				jcb.addItem("Select Room");
			for (int i = 0; i < getETSData().getRoomLength(); i++)
				jcb.addItem(getETSData().getRoomNames().get(i));
			break;
		case 0:
			jcb.setName("STUDENT1");
			int StudLen=getETSData().getStudentLength();
			if (StudLen == 0)
				jcb.addItem("No Student");
			else
				jcb.addItem("Select Student");
			for (int i = 0; i < StudLen; i++)
				jcb.addItem(getETSData().getStudent(i).getName());
			break;
		}
		return jcb;
	}

	public void actionPerformed(ActionEvent e) {
	}

}

class ViewPanel extends JPanel {
	private OutputGenerator og;
	private boolean isGenerate;
	private JScrollPane tableAggregate;
	private JPanel tablePanel, headPanel;
	private JComboBox jcb;
	private String title;
	private int whichTable;
	private JTable tabView;
	private JLabel jta;
	public ViewPanel(String title) {
		super();
		this.title = title;
		tableAggregate = new JScrollPane();
		jta=new JLabel();
		jta.setText("- No Selected Yet -");
		addViewPanel();
	}

	private void addViewPanel() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());

		headPanel = new JPanel();
		headPanel.setLayout(new GridLayout(1, 2, 10, 10));
		headPanel.setBackground(Color.WHITE);
		headPanel.add(jta);

		tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 1, 10, 10));
		tablePanel.setBackground(Color.DARK_GRAY);
		tablePanel.add(tableAggregate);

		add(headPanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
		updateUI();
	}

	public void setTable() {
		JScrollPane jspane;
		tabView=og.getTimeTable(-1, 0);
		jspane = new JScrollPane(tabView);
		tableAggregate=jspane;
		RefreshTable();
		tabView.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
				int r=tabView.getSelectedRow();
				int c=tabView.getSelectedColumn();
				String str=tabView.getValueAt(r, c).toString();
				if(c>0)
				jta.setText(str);
			}} );
	}

	public void RefreshTable() {
		tablePanel.removeAll();
		tablePanel.add(tableAggregate);
		tablePanel.updateUI();
	}

	public void AddJCB(JComboBox jcb_, OutputGenerator og_, boolean isGenerate_) {
		jcb = jcb_;
		og=og_;
		isGenerate=isGenerate_;
		
		headPanel.removeAll();
		headPanel.add(jcb);
		headPanel.add(jta);
		headPanel.updateUI();

		if (jcb.getName().equals("TEACHER"))
			whichTable = DataSet.TEACHER;
		if (jcb.getName().equals("STUDENT"))
			whichTable = DataSet.STUDENT;
		if (jcb.getName().equals("STUDENT1"))
			whichTable = 0;
		if (jcb.getName().equals("ROOM"))
			whichTable = DataSet.ROOM;

		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isGenerate){
					JScrollPane jspane;
					tabView=og.getTimeTable(jcb.getSelectedIndex()-1, whichTable);
					jspane = new JScrollPane(tabView);
					tableAggregate=jspane;
					RefreshTable();
					tabView.addMouseListener(new MouseListener(){
						public void mouseClicked(MouseEvent arg0) {
						}
						public void mouseEntered(MouseEvent arg0) {
						}
						public void mouseExited(MouseEvent arg0) {
						}
						public void mousePressed(MouseEvent arg0) {
						}
						public void mouseReleased(MouseEvent arg0) {
							int r=tabView.getSelectedRow();
							int c=tabView.getSelectedColumn();
							String str=tabView.getValueAt(r, c).toString();
							if(c>0)
							jta.setText(str);
						}} );
				}else
					setTable();
			}
		});
	}
}
