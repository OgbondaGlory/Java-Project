package ETS;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import DataSourceKB.DataSet;
import ETS.Timetable.Matrix2D;

public class RulesGUI extends ETSModule {
	JComboBox jcb;
	JComboBox jcb1;
	JScrollPane jspane;
	JPanel tablePanel;
	JTable tableView;
	JTextField jtf;
	Matrix2D<Boolean> TimeTable;

	String[] wDay = { "Time\\Day", "Monday", "Tuesday", "Wednesday",
			"Thrusday", "Friday" };
	String[] wHour = { "Hour 1", "Hour 2", "Hour 3", "Hour 4", "Hour 5",
			"Hour 6", "Hour 7", "Hour 8" };

	Vector<String> VList, VList1;
	String rule,selRoom,selStud;
	int i, index, studindex;

	public RulesGUI(ETS ets, String rule) {
		super(ets);
		this.rule = rule;
	}

	public JPanel getPanel(int i_) {
		JPanel rulePanel = new JPanel();

		this.i = i_;
		String DEFAULT[] = { "<br> ",
				"<br> ", 
				"<br> Preference is 100%",
				"<br> Preference is 100%", 
				"<br> Preference is 100%",
				"<br> Preference is 100%", 
				"<br> Preference is 100%",
				"<br> Preference is 100%", 
				"<br> Na",
				"<br> Minimum 'n' days between Activities : 1 day", 
				"<br> Na",
				"<br> Na",
				"<br> Na", 
				"<br> All Room are available for all Students",
				"<br> All Labs are available for students" };

		jcb = new JComboBox();
		jcb1 = new JComboBox();
		jspane = new JScrollPane();
		tablePanel = new JPanel();
		JButton set;
		JPanel jpo = new JPanel();
		jpo.setLayout(new BorderLayout());

		JPanel jp = new JPanel();
		jp.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 0, 2, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		switch (i) {
		case 0:
			jtf=new JTextField();
			jtf.setColumns(20);
			jtf.setText("Enter University Name");
			set = new JButton("Set University ");
			set.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String str = jtf.getText();
					if(!str.equals(null)){
					getETSData().getRule().setCollageName(str);
					JOptionPane.showMessageDialog(null,"University Name Set Sucessfully!");
					}else
						JOptionPane.showMessageDialog(null,"University Name is [Empty] !");
				}
			});

			gbc.gridx = 1;
			gbc.gridy = 1;
			jp.add(jtf, gbc);
			gbc.gridx = 20;
			jp.add(set, gbc);

			jpo.add(jp, BorderLayout.NORTH);
			jpo.add(
					new JLabel("<html><hr>" + rule
							+ "<br><br>By User set to : " + getETSData().getRule().getCollageName()
							+ "<hr></html>"), BorderLayout.SOUTH);

			break;
		case 1:
			jtf=new JTextField();
			jtf.setColumns(20);
			jtf.setText("Enter Department Name");
			set = new JButton("Set Department ");
			set.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String str = jtf.getText();
					if(!str.equals(null)){
					getETSData().getRule().setDeptName(str);
					JOptionPane.showMessageDialog(null,"Department Name Set Sucessfully!");
					}else
						JOptionPane.showMessageDialog(null,"Department Name is [Empty] !");
				}
			});

			gbc.gridx = 1;
			gbc.gridy = 1;
			jp.add(jtf, gbc);
			gbc.gridx = 20;
			jp.add(set, gbc);

			jpo.add(jp, BorderLayout.NORTH);
			// jpo.add(jspane,BorderLayout.CENTER);
			jpo.add(
					new JLabel("<html><hr>" + rule
							+ "<br><br>By User set to : " + getETSData().getRule().getDeptName()
							+ "<hr></html>"), BorderLayout.SOUTH);

			break;
		case 8:
			set = new JButton("Set Break");
			set.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String str = VList.get(jcb.getSelectedIndex());
					getETSData().getRule().setBreak(str);
					JOptionPane.showMessageDialog(null,
							"Break Set Sucessfully!");
				}
			});

			VList = getList(DataSet.TIME);
			jcb.removeAll();
			jcb.setModel(new DefaultComboBoxModel(VList));

			gbc.gridx = 1;
			gbc.gridy = 1;
			jp.add(jcb, gbc);
			gbc.gridx = 20;
			jp.add(set, gbc);

			jpo.add(jp, BorderLayout.NORTH);
			// jpo.add(jspane,BorderLayout.CENTER);
			jpo.add(
					new JLabel("<html><hr>" + rule
							+ "<br><br>By User set to : " + getETSData().getRule().getBreak()
							+ "<hr></html>"), BorderLayout.SOUTH);

			break;
		case 10:
			set = new JButton("Set Rule");
			VList = getList(DataSet.TEACHER);
			jcb.removeAll();
			jcb.setModel(new DefaultComboBoxModel(VList));
			jcb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					index = jcb.getSelectedIndex() - 1;
					setTable(index, DataSet.TEACHER);
				}
			});
			set.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (index != -1) {
						getETSData().getRule().getTeacher().set2DPart(index,TimeTable);
						JOptionPane.showMessageDialog(null,
								"Rule Set Sucessfully!");
					} else {
						JOptionPane.showMessageDialog(null,	"Select Teacher First! ");
					}
				}
			});

			gbc.gridx = 1;gbc.gridy = 1;jp.add(jcb, gbc);
			gbc.gridx = 20;	jp.add(set, gbc);

			gbc.gridx = 1;gbc.gridy = 30;tablePanel.add(jspane);

			jpo.add(jp, BorderLayout.NORTH);
			jpo.add(tablePanel, BorderLayout.CENTER);
			jpo.add(
					new JLabel("<html><hr>" + rule
							+ "<br><br>By User set to : " + DEFAULT[i]
							+ "<hr></html>"), BorderLayout.SOUTH);

			break;
		case 11:
			set = new JButton("Set Rule");
			VList = getList(DataSet.STUDENT);
			jcb.removeAll();
			jcb.setModel(new DefaultComboBoxModel(VList));
			jcb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					index = jcb.getSelectedIndex() - 1;
					int s=-1;
					if (index != -1){
						s= getETSGenerator().searchStudent(jcb.getSelectedItem().toString());
					    setTable(s, DataSet.STUDENT);
					}
				}
			});
			set.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (index != -1) {
						int s= getETSGenerator().searchStudent(jcb.getSelectedItem().toString());
						int size=s+getETSData().getStudent(index).getGroupList().size();
						for (; s < size+1; s++)
							getETSData().getRule().getStudent().set2DPart(s,TimeTable);
						JOptionPane.showMessageDialog(null,"Rule Set Sucessfully!");
					} else {
						JOptionPane.showMessageDialog(null,"Select Student First! ");
					}
				}
			});

			gbc.gridx = 1;gbc.gridy = 1;jp.add(jcb, gbc);
			gbc.gridx = 20;	jp.add(set, gbc);

			gbc.gridx = 1;gbc.gridy = 30;tablePanel.add(jspane);

			jpo.add(jp, BorderLayout.NORTH);
			jpo.add(tablePanel, BorderLayout.CENTER);
			jpo.add(
					new JLabel("<html><hr>" + rule
							+ "<br><br>By User set to : " + DEFAULT[i]
							+ "<hr></html>"), BorderLayout.SOUTH);
			break;
		case 12:
			set = new JButton("Set Rule");
			VList = getList(DataSet.ROOM);
			jcb.removeAll();
			jcb.setModel(new DefaultComboBoxModel(VList));
			jcb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					index = jcb.getSelectedIndex() - 1;
					setTable(index, DataSet.ROOM);
				}
			});
			set.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (index != -1) {
						getETSData().getRule().getRoom().set2DPart(index,TimeTable);
						JOptionPane.showMessageDialog(null,	"Rule Set Sucessfully!");
					} else {
						JOptionPane.showMessageDialog(null,	"Select Room First! ");
					}
				}
			});

			gbc.gridx = 1;gbc.gridy = 1;jp.add(jcb, gbc);
			gbc.gridx = 20;	jp.add(set, gbc);

			gbc.gridx = 1;gbc.gridy = 30;tablePanel.add(jspane);

			jpo.add(jp, BorderLayout.NORTH);
			jpo.add(tablePanel, BorderLayout.CENTER);
			jpo.add(
					new JLabel("<html><hr>" + rule
							+ "<br><br>By User set to : " + DEFAULT[i]
							+ "<hr></html>"), BorderLayout.SOUTH);


			break;
		default:
			jpo.add(new JLabel("<html><hr>" + rule
					+ "<br><br>By Default set to : " + DEFAULT[i]
					+ "<hr></html>"), BorderLayout.NORTH);
		}
		
		rulePanel.removeAll();
		rulePanel.add(jpo);
		rulePanel.updateUI();

		return rulePanel;
	}

	public Vector<String> getList(int c) {
		Vector<String> RVList = new Vector<String>();
		RVList.clear();
		switch (c) {
		case DataSet.TEACHER:
			RVList.add(0, "Select Invigilator");
			for (int i = 1; i < getETSData().getTeacherLength() + 1; i++)
				RVList.add(i, getETSData().getTeacher(i - 1).getName());
			break;
		case DataSet.STUDENT:
			RVList.add(0, "Select Student");
			for (int i = 1; i < getETSData().getStudentLength() + 1; i++)
				RVList.add(i, getETSData().getStudent(i - 1).getName());
			break;
		case DataSet.ROOM:
			RVList.add(0, "Select Hall");
			for (int i = 1; i < getETSData().getRoomLength() + 1; i++)
				RVList.add(i, getETSData().getRoom(i - 1).getName());
			break;
		case DataSet.TIME:
			RVList.add(0, "Select Timeslot");
			for (int i = 1; i < getETSData().getTimeSet().getHour().size() + 1; i++)
				RVList.add(i, getETSData().getTimeSet().getHour().get(i - 1));
			break;
		}
		return RVList;
	}

	public void RefreshTable() {
		JScrollPane jpane;
		jpane = new JScrollPane(getTimeslotTable());
		jspane = jpane;

		tablePanel.removeAll();
		tablePanel.add(jspane);
		tablePanel.updateUI();
	}

	public void setTable(int i, int which) {
		if (i == -1) {

		} else {
			switch (which) {
			case DataSet.TEACHER:
				TimeTable = getETSData().getRule().getTeacher().get2DPart(i);
				break;
			case DataSet.STUDENT:
				TimeTable = getETSData().getRule().getStudent().get2DPart(i);
				break;
			case DataSet.ROOM:
				TimeTable = getETSData().getRule().getRoom().get2DPart(i);
				break;
			}
		}
		RefreshTable();
	}

	public JTable getTimeslotTable() {
		// Create a model of the data.
		TableModel dataModel = new AbstractTableModel() {
			public int getColumnCount() {
				return TimeTable.getC() + 1;
			}

			public int getRowCount() {
				return TimeTable.getR();
			}

			public Object getValueAt(int row, int col) {
				if (col == 0)
					return getETSData().getTimeSet().getHour().get(row);
				else
					return TimeTable.getContent(row, col - 1);
			}

			public String getColumnName(int column) {
				if (column == 0)
					return "Time\\Day";
				else
					return getETSData().getTimeSet().getDay().get(column - 1);
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return row != -1;
			}

			public void setValueAt(Object aValue, int row, int column) {
				if (column > 0) {
					if (aValue.equals(true))
						TimeTable.setContent(row, column - 1, true);
					else if (aValue.equals(false))
						TimeTable.setContent(row, column - 1, false);
				}
			}
		};

		// Create the table
		tableView = new JTable(dataModel);
		tableView.setRowHeight(30);
		return tableView;
	}
}
