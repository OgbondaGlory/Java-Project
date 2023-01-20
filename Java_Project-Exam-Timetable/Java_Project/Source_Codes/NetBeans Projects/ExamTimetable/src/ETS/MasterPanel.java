package ETS;

/*
 * @(#)MasterData.java
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import DataSourceKB.*;

public class MasterPanel extends ETSModule {
	ArrayList<JTextField> jtfDay = new ArrayList<JTextField>();
	ArrayList<JTextField> jtfHour = new ArrayList<JTextField>();

	JComboBox jcbStud;
	JTextField name = new JTextField();
	JTextField gname = new JTextField();
	JTextField type = new JTextField();
	JTextField pppw = new JTextField();
	JTextField tppw = new JTextField();
	JTextField strength = new JTextField();
	JTextField capacity = new JTextField();
	JSpinner daySpin = new JSpinner();
	JSpinner hourSpin = new JSpinner();

	JPanel gPanel, masterPanel, deptPanel, buttonPanel;
	JList commonList;
	JTextArea commonListDisplay;

	JButton showBtn, cancelBtn, createBtn, addBtn, editBtn, removeBtn;
	JTabbedPane tabbedpane;
	JTable tableView;
	JScrollPane scrollpane, tableAggregate;

	final int INITIAL_ROWHEIGHT = 20;
	String[] tchnames = { "Invigilator Name", "Invigilator Type" };
	String[] studnames = { "Student's level", "No. of Students" };
	String[] roomnames = { "Hall Name", "Hall Type", "Hall Capacity" };
	String[] subjnames = { "Course Name", "No.of Theory/Week","No.of Practical/Week" };

	Vector<String> tmpList = new Vector<String>();
	Vector<String> dataList = new Vector<String>();
	Vector<Object[]> dataTable = new Vector<Object[]>();

	/**
	 * MasterData Constructor
	 */
	public MasterPanel(ETS ets) {
		super(ets, "", "");
		JButton tchB, subjB, studB, roomB, timeB;

		InitJTF();
		InitTimeJTF();
		tmpList = new Vector<String>();
		dataList = new Vector<String>();
		dataTable = new Vector<Object[]>();

		commonList = new JList();
		commonListDisplay = new JTextArea();
		commonListDisplay.setColumns(20);
		commonListDisplay.setEditable(false);
		commonListDisplay.setSize(100, 200);
		
		commonList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(commonList.getSelectedIndex()>-1)
				commonListDisplay.setText(dataList.get(commonList.getSelectedIndex()));
			}
		});
		

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Data Entry
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		masterPanel = new JPanel();
		deptPanel = new JPanel();
		buttonPanel = new JPanel();

		masterPanel.setLayout(new BorderLayout());
		deptPanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new GridLayout(1, 4));
		masterPanel.add(buttonPanel, BorderLayout.NORTH);
		masterPanel.add(deptPanel, BorderLayout.CENTER);

		tchB = new JButton(getString("MasterData.tch"));
		subjB = new JButton(getString("MasterData.subj"));
		studB = new JButton(getString("MasterData.stud"));
		roomB = new JButton(getString("MasterData.room"));
		timeB = new JButton(getString("MasterData.time"));

		buttonPanel.add(tchB);
		buttonPanel.add(subjB);
		buttonPanel.add(studB);
		buttonPanel.add(roomB);
		buttonPanel.add(timeB);

		tchB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getTeacharContent(getString("MasterData.tch")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.TEACHER, tchnames);
			}
		});

		subjB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getSubjectContent(getString("MasterData.subj")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.SUBJECT, subjnames);
			}
		});

		studB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getStudentContent(getString("MasterData.stud")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.STUDENT, studnames);
			}
		});

		roomB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitJTF();
				deptPanel.removeAll();
				deptPanel.add(getRoomContent(getString("MasterData.room")),BorderLayout.CENTER);
				deptPanel.updateUI();
				createTable(DataSet.ROOM, roomnames);
			}
		});

		timeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitTimeJTF();
				deptPanel.removeAll();
				deptPanel.add(getTimeContent(getString("MasterData.time")),BorderLayout.CENTER);
				deptPanel.updateUI();
				ValidateTime();
			}
		});
	}

	public JPanel getMasterPanel() {
		return masterPanel;
	}

	public JPanel getTimeContent(String title) {
		JPanel TimeContentPanel = new JPanel();

		JPanel btnPanel;
		JButton addBtn = new JButton("Add");
		JPanel dayPanel = new JPanel();
		JPanel hourPanel = new JPanel();
		JPanel spinPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		dayPanel.setBorder(new TitledBorder(" Days : "));
		hourPanel.setBorder(new TitledBorder(" Hours : "));
		spinPanel.setBorder(new LineBorder(Color.WHITE, 2));
		TimeContentPanel.setLayout(new GridBagLayout());
		dayPanel.setLayout(new GridLayout(3, 2, 10, 10));
		hourPanel.setLayout(new GridLayout(4, 3, 10, 10));
		spinPanel.setLayout(new GridBagLayout());

		InitTimeJTF();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 10, 20, 10);
		for (int i = 0; i < DataSet.MAXDAY; i++) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridBagLayout());
			btnPanel.add(new JLabel("Day " + (i + 1) + ": "), c);
			btnPanel.add(jtfDay.get(i), c);
			dayPanel.add(btnPanel);
		}
		c.insets = new Insets(2, 1, 2, 1);
		for (int i = 0; i < DataSet.MAXHOUR; i++) {
			btnPanel = new JPanel();
			btnPanel.setLayout(new GridBagLayout());
			btnPanel.add(new JLabel("Hour " + (i + 1) + ": "), c);
			btnPanel.add(jtfHour.get(i), c);
			hourPanel.add(btnPanel);
		}

		// new SpinnerNumberModel(value, min, max, step);
		daySpin.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),new Integer(DataSet.MAXDAY), new Integer(1)));
		hourSpin.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(DataSet.MAXHOUR), new Integer(1)));

		daySpin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				EnableJTF(jtfDay, Integer.parseInt(daySpin.getValue().toString()));
			}
		});

		hourSpin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				EnableJTF(jtfHour, Integer.parseInt(hourSpin.getValue().toString()));
			}
		});

		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpadateTime();
			}
		});

		c.gridx = 1;
		c.gridy = 1;
		spinPanel.add(new JLabel("No.of Days/Week : "), c);
		c.gridy = 10;
		spinPanel.add(daySpin, c);
		c.gridy = 20;
		spinPanel.add(new JLabel("No.of Hours/Day : "), c);
		c.gridy = 30;
		spinPanel.add(hourSpin, c);
		c.gridy = 40;
		spinPanel.add(addBtn, c);

		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		TimeContentPanel.add(dayPanel, c);
		c.anchor = GridBagConstraints.LINE_END;
		TimeContentPanel.add(spinPanel, c);
		c.gridx = 1;
		c.gridy = 10;
		c.anchor = GridBagConstraints.WEST;
		TimeContentPanel.add(hourPanel, c);

		return TimeContentPanel;
	}

	public void ValidateTime() {
		if (!getETSData().getTimeSet().getDay().isEmpty()) {
			for (int i = 0; i < getETSData().getTimeSet().getDay().size(); i++) {
				jtfDay.get(i).setEnabled(true);
				jtfDay.get(i).setBackground(Color.WHITE);
				jtfDay.get(i).setText(getETSData().getTimeSet().getDay().get(i));
			}
			daySpin.setValue(getETSData().getTimeSet().getDay().size());
		}
		if (!getETSData().getTimeSet().getHour().isEmpty()) {
			for (int i = 0; i < getETSData().getTimeSet().getHour().size(); i++) {
				jtfHour.get(i).setEnabled(true);
				jtfHour.get(i).setBackground(Color.WHITE);
				jtfHour.get(i).setText(getETSData().getTimeSet().getHour().get(i));
			}
			hourSpin.setValue(getETSData().getTimeSet().getHour().size());
		}
	}

	public void UpadateTime() {
		getETSData().getTimeSet().getDay().removeAllElements();
		getETSData().getTimeSet().getHour().removeAllElements();
		for (int i = 0; i < jtfDay.size(); i++)
			if (jtfDay.get(i).isEnabled())
				getETSData().getTimeSet().getDay().addElement(jtfDay.get(i).getText());
		for (int i = 0; i < jtfHour.size(); i++)
			if (jtfHour.get(i).isEnabled())
				getETSData().getTimeSet().getHour().addElement(jtfHour.get(i).getText());
	}

	private void InitTimeJTF() {
		for (int i = 0; i < DataSet.MAXDAY; i++) {
			jtfDay.add(new JTextField());
			jtfDay.get(i).setColumns(10);
			jtfDay.get(i).setBackground(Color.LIGHT_GRAY);
			jtfDay.get(i).setEnabled(false);
		}

		for (int i = 0; i < DataSet.MAXHOUR; i++) {
			jtfHour.add(new JTextField());
			jtfHour.get(i).setColumns(10);
			jtfHour.get(i).setBackground(Color.LIGHT_GRAY);
			jtfHour.get(i).setEnabled(false);
		}
	}

	public void EnableJTF(ArrayList<JTextField> jtf, int value) {
		for (int i = 0; i < jtf.size(); i++) {
			if (i <= value - 1) {
				jtf.get(i).setEnabled(true);
				jtf.get(i).setBackground(Color.WHITE);
			} else {
				jtf.get(i).setText("");
				jtf.get(i).setEnabled(false);
				jtf.get(i).setBackground(Color.LIGHT_GRAY);
			}
		}
	}

	private void InitJTF() {
		name = new JTextField();
		gname = new JTextField();
		type = new JTextField();
		pppw = new JTextField();
		tppw = new JTextField();
		strength = new JTextField();
		capacity = new JTextField();

		name.setColumns(20);
		gname.setColumns(20);
		type.setColumns(20);
		pppw.setColumns(20);
		tppw.setColumns(20);
		strength.setColumns(20);
		capacity.setColumns(20);

		jcbStud = new JComboBox();
		if (getETSData().getStudentLength() == 0)
			jcbStud.addItem("No Student");
		else
			jcbStud.addItem("Select Student");

		jcbStud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jcbStud.getItemCount() > 0)
					if (!jcbStud.getSelectedItem().toString().contains("Student"))
						name.setText(jcbStud.getSelectedItem().toString());
			}
		});

	}

	public void RefreshTable(ShowHidePanel P0,int c,String[] s){
		P0.getPanelSet(1).removeAll();
		P0.SHOWP();
		createTable(c, s);
		P0.getPanelSet(1).add(tableAggregate);
		P0.getPanelSet(1).updateUI();				
	}
	
	public JPanel getTeacharContent(String title) {
		final ShowHidePanel P0 = new ShowHidePanel();
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Teacher
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Invigilator");
		editBtn = new JButton("Edit Invigilator");
		removeBtn = new JButton("Remove Invigilator");
		showBtn = new JButton("Show");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Invigilator Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);
		gbc.gridy = 40;

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(new JLabel("Invigilator Type : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(type, gbc);
		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(showBtn, gbc);

		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0, DataSet.TEACHER,tchnames);
			}
		});
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(P0.validate(name, 0) && P0.validate(type, 0))) {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields!","Empty", JOptionPane.INFORMATION_MESSAGE);
				} else
					getETSData().addTeacher(name.getText(), type.getText());
				RefreshTable(P0, DataSet.TEACHER,tchnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Invigilator")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,
								"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0, DataSet.TEACHER,tchnames);
							editBtn.setText("Edit Invigilator");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (JOptionPane.showConfirmDialog(null,"Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getETSData().removeTeacher(tableView.getSelectedRow());
						RefreshTable(P0, DataSet.TEACHER,tchnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(), "Remove",JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Invigilator");
				addBtn.setEnabled(true);
			}
		});

		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	public JPanel getSubjectContent(String title) {
		final ShowHidePanel P0 = new ShowHidePanel();
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Subject
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Course");
		editBtn = new JButton("Edit Course");
		removeBtn = new JButton("Remove Course");
		showBtn = new JButton("Show");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Course Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(new JLabel("No.Theory Period/Week : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(tppw, gbc);

		gbc.gridx = 1;
		gbc.gridy = 20;
		P0.getPanelSet(0).add(new JLabel("No.Practical Period/Week : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(pppw, gbc);
		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(showBtn, gbc);
		
		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0,DataSet.SUBJECT, subjnames);
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(P0.validate(name, 0) && P0.validate(tppw, 0) && P0.validate(pppw, 0))) {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields!","Empty", JOptionPane.INFORMATION_MESSAGE);
				} else if (!(P0.validate(tppw, 1) && P0.validate(pppw, 1))) {
					JOptionPane.showMessageDialog(null, "Field is a numeric!","Number", JOptionPane.ERROR_MESSAGE);
				} else {
					getETSData().addSubject(name.getText(),Integer.parseInt(tppw.getText()),Integer.parseInt(pppw.getText()));
				}
				RefreshTable(P0,DataSet.SUBJECT, subjnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Course")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0,DataSet.SUBJECT, subjnames);
							editBtn.setText("Edit Course");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (JOptionPane.showConfirmDialog(null,"Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getETSData().removeSubject(tableView.getSelectedRow());
						RefreshTable(P0,DataSet.SUBJECT, subjnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(), "Remove",JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Course");
				addBtn.setEnabled(true);
			}
		});

		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	public JPanel getStudentContent(String title) {
		gPanel = new JPanel();
		gPanel.setLayout(new GridBagLayout());
		gPanel.setVisible(false);
		gname.setVisible(false);
		jcbStud.setVisible(false);

		final ShowHidePanel P0 = new ShowHidePanel();
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");
		createBtn = new JButton("Create Group");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Student
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Student");
		editBtn = new JButton("Edit Student");
		removeBtn = new JButton("Remove Student");
		showBtn = new JButton("Show");

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gPanel.add(new JLabel("Student Group Name : "), gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Student'S Level : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);
		P0.getPanelSet(0).add(jcbStud, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(gPanel, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(gname, gbc);

		gbc.gridx = 1;
		gbc.gridy = 20;
		P0.getPanelSet(0).add(new JLabel("No.of Students : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(strength, gbc);

		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(createBtn, gbc);
		gbc.gridx = 30;
		P0.getPanelSet(0).add(showBtn, gbc);

		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				name.setText("");
				gname.setText("");
				strength.setText("");
				if (createBtn.getText().equals("Create Group")) {
					gPanel.setVisible(true);
					jcbStud.setVisible(true);
					gname.setVisible(true);
					name.setVisible(false);
					createBtn.setText("Cancel");
					addBtn.setText("Add Group");
				} else if (createBtn.getText().equals("Cancel")) {
					addBtn.setText("Add Student");
					createBtn.setText("Create Group");
					gPanel.setVisible(false);
					jcbStud.setVisible(false);
					gname.setVisible(false);
					name.setVisible(true);
				}
			}
		});

		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0,DataSet.STUDENT, studnames);
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addBtn.getText().equals("Add Student")) {
					if (!(P0.validate(name, 0) && P0.validate(strength, 0))) {
						JOptionPane.showMessageDialog(null,"Fill Empty Fields!", "Empty",JOptionPane.INFORMATION_MESSAGE);
					} else if (!P0.validate(strength, 1)) {
						JOptionPane.showMessageDialog(null,"Field is a numeric!", "Number",JOptionPane.ERROR_MESSAGE);
					} else {
						getETSData().addStudent(name.getText(),Integer.parseInt(strength.getText()));
						name.setText("");
						strength.setText("");
					}
				} else if (addBtn.getText().equals("Add Group")) {
					if (!(P0.validate(name, 0) && P0.validate(gname, 0) && P0.validate(strength, 0))) {
						JOptionPane.showMessageDialog(null,"Fill Empty Fields!", "Empty",JOptionPane.INFORMATION_MESSAGE);
					} else if (!P0.validate(strength, 1)) {
						JOptionPane.showMessageDialog(null,"Field is a numeric!", "Number",JOptionPane.ERROR_MESSAGE);
					} else {
						if (getETSData().addGroup(name.getText(),gname.getText(),Integer.parseInt(strength.getText()))) {
							name.setText("");
							gname.setText("");
							strength.setText("");

							addBtn.setText("Add Student");
							createBtn.setText("Create Group");
							gPanel.setVisible(false);
							jcbStud.setVisible(false);
							gname.setVisible(false);
							name.setVisible(true);
						}
					}
				}
				RefreshTable(P0,DataSet.STUDENT, studnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Student")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0,DataSet.STUDENT, studnames);
							editBtn.setText("Edit Student");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (RemoveStudent(tableView.getSelectedRow(), tmp)) {
						RefreshTable(P0,DataSet.STUDENT, studnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(),"Remove", JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}

			private boolean RemoveStudent(int index, String tmp) {
				String str = tmpList.get(index);
				if (str.indexOf(":") > 0) {
					int i = Integer.parseInt(str.substring(0, str.indexOf(":")));
					int j = Integer.parseInt(str.substring(str.indexOf(":") + 1));
					if (JOptionPane.showConfirmDialog(null, "Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getETSData().removeGroup(i, j);
						return true;
					}
				} else {
					int i = Integer.parseInt(str);
					if (JOptionPane.showConfirmDialog(null, "Removing " + tmp + " cause for deletion of it's groups & "	+ getETSData().getStudent(i).getName() + "\n Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getETSData().removeStudent(i);
						return true;
					}
				}
				return false;
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Student");
				addBtn.setEnabled(true);
			}
		});

		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	public JPanel getRoomContent(String title) {
		final ShowHidePanel P0 = new ShowHidePanel();
		
		final JComboBox jcbATag = new JComboBox();
		jcbATag.setModel(new DefaultComboBoxModel(new String[] { "Select Hall", "CLASS", "LAB"}));
		
		jcbATag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!jcbATag.getSelectedItem().toString().contains("Hall"))
					type.setText(jcbATag.getSelectedItem().toString());
				else 
					type.setText("");
			}
		});
		
		P0.getPanelSet(0).setLayout(new GridBagLayout());
		P0.getPanelSet(1).setLayout(new BorderLayout());
		P0.getPanelSet(2).setLayout(new BorderLayout(10, 10));
		P0.getPanelSet(2).setBorder(new TitledBorder(title + " List : "));
		cancelBtn = new JButton("Cancel");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 2, 5, 2);
		gbc.anchor = GridBagConstraints.LINE_END;

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Room
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		P0.getPanelSet(3).setLayout(new GridBagLayout());
		addBtn = new JButton("Add Hall");
		editBtn = new JButton("Edit Hall");
		removeBtn = new JButton("Remove Hall");
		showBtn = new JButton("Show");
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(3).add(editBtn, gbc);
		gbc.gridx = 10;
		P0.getPanelSet(3).add(removeBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(3).add(cancelBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		P0.getPanelSet(0).add(new JLabel("Hall Name : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(name, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		P0.getPanelSet(0).add(new JLabel("Hall Type : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(jcbATag, gbc);

		gbc.gridx = 1;
		gbc.gridy = 20;
		P0.getPanelSet(0).add(new JLabel("Hall Capacity : "), gbc);
		gbc.gridx = 10;
		P0.getPanelSet(0).add(capacity, gbc);
		gbc.gridy = 40;
		P0.getPanelSet(0).add(addBtn, gbc);
		gbc.gridx = 20;
		P0.getPanelSet(0).add(showBtn, gbc);

		showBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTable(P0,DataSet.ROOM, roomnames);
			}
		});
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(P0.validate(name, 0) && P0.validate(type, 0) && P0.validate(capacity, 0))) {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields!","Empty", JOptionPane.INFORMATION_MESSAGE);
				} else if (!P0.validate(capacity, 1)) {
					JOptionPane.showMessageDialog(null, "Field is a numeric!","Number", JOptionPane.ERROR_MESSAGE);
				} else {
					getETSData().addRoom(name.getText(),Integer.parseInt(capacity.getText()),type.getText());
				}
				RefreshTable(P0,DataSet.ROOM, roomnames);
			}
		});
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (editBtn.getText().equalsIgnoreCase("Edit Hall")) {
					if (tableView.getSelectedRow() != -1) {
						EditRow(tableView.getSelectedRow());
						editBtn.setText("Save");
						addBtn.setEnabled(false);
						JOptionPane.showMessageDialog(null,"You have selected Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Edit", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Edit",JOptionPane.INFORMATION_MESSAGE);
				} else if (editBtn.getText().equalsIgnoreCase("Save")) {
					if (tableView.getSelectedRow() != -1) {
						if (SaveRow(tableView.getSelectedRow())) {
							RefreshTable(P0,DataSet.ROOM, roomnames);
							editBtn.setText("Edit Hall");
							addBtn.setEnabled(true);
							JOptionPane.showMessageDialog(null,"You have successfully saved Row[" + tableView.getSelectedRow() + "] from " + tableView.getName(),"Save", JOptionPane.INFORMATION_MESSAGE);
						}
					} else
						JOptionPane.showMessageDialog(null,"First You have to select ", "Save",JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tableView.getSelectedRow() != -1) {
					String tmp = tableView.getValueAt(tableView.getSelectedRow(), 0).toString();
					if (JOptionPane.showConfirmDialog(null,"Want to remove Row Element? " + tmp) == JOptionPane.YES_OPTION) {
						getETSData().removeRoom(tableView.getSelectedRow());
						RefreshTable(P0,DataSet.ROOM, roomnames);
						JOptionPane.showMessageDialog(null,"You have successfully removed Row Element " + tmp + " from " + tableView.getName(), "Remove",JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(null,"First You have to select ", "Remove",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editBtn.setText("Edit Hall");
				addBtn.setEnabled(true);
			}
		});
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		commonListDisplay.setText("");
		P0.getPanelSet(2).add(new JScrollPane(commonList), BorderLayout.CENTER);
		P0.getPanelSet(2).add(new JScrollPane(commonListDisplay),BorderLayout.LINE_END);

		return P0.addPanelSet(title);

	}

	protected void EditRow(int row) {
		if (tableView.getName().equalsIgnoreCase("invigilator")) {
			name.setText(tableView.getModel().getValueAt(row, 0).toString());
			type.setText(tableView.getModel().getValueAt(row, 1).toString());
		} else if (tableView.getName().equalsIgnoreCase("student")) {
			String str = tmpList.get(row);
			if (str.indexOf(":") > 0) {
				int i = Integer.parseInt(str.substring(0, str.indexOf(":")));
				jcbStud.setSelectedIndex(i+1);
				gname.setText(tableView.getModel().getValueAt(row, 0).toString());
				gPanel.setVisible(true);
				jcbStud.setVisible(true);
				gname.setVisible(true);
				name.setVisible(false);
			} else {
				name.setText(tableView.getModel().getValueAt(row, 0).toString());
				gPanel.setVisible(false);
				jcbStud.setVisible(false);
				gname.setVisible(false);
				name.setVisible(true);
			}
			strength.setText(tableView.getModel().getValueAt(row, 1).toString());
		} else if (tableView.getName().equalsIgnoreCase("course")) {
			name.setText(tableView.getModel().getValueAt(row, 0).toString());
			tppw.setText(tableView.getModel().getValueAt(row, 1).toString());
			pppw.setText(tableView.getModel().getValueAt(row, 2).toString());
		} else if (tableView.getName().equalsIgnoreCase("hall")) {
			name.setText(tableView.getModel().getValueAt(row, 0).toString());
			type.setText(tableView.getModel().getValueAt(row, 1).toString());
			capacity.setText(tableView.getModel().getValueAt(row, 2).toString());
		}

	}

	protected boolean SaveRow(int row) {
		String dup = "";
		if (tableView.getName().equalsIgnoreCase("invigilator")) {
			if (getETSData().searchTeacher(name.getText())==row ||
				getETSData().searchTeacher(name.getText())==-1) {
				getETSData().getTeacher(row).setName(name.getText());
				getETSData().getTeacher(row).setType(type.getText());
			} else
				dup = "Duplicate Entry for " + name.getText();
		} else if (tableView.getName().equalsIgnoreCase("student")) {
			String str = tmpList.get(row);
			if (str.indexOf(":") > 0) {
				int i = Integer.parseInt(str.substring(0, str.indexOf(":")));
				int j = Integer.parseInt(str.substring(str.indexOf(":") + 1));
				if (getETSData().searchGroupStudent(i,gname.getText())==j ||
					getETSData().searchGroupStudent(i,gname.getText())==-1) {
					getETSData().getStudent(i).setGroupName(j, gname.getText());
					getETSData().getStudent(i).setGroupSize(j, 
							Integer.parseInt(strength.getText()));
				} else
					dup = "Duplicate Entry for " + name.getText();
			} else {
				int i = Integer.parseInt(str);
				if (getETSData().searchStudent(name.getText())==i ||
					getETSData().searchStudent(name.getText())==-1) {
					getETSData().getStudent(i).setName(name.getText());
					getETSData().getStudent(i).setTotStud(
							Integer.parseInt(strength.getText()));
				} else
					dup = "Duplicate Entry for " + name.getText();
			}
			
			
		} else if (tableView.getName().equalsIgnoreCase("course")) {
			if (getETSData().searchTeacher(name.getText())==row ||
				getETSData().searchSubject(name.getText())==-1) {
				getETSData().getSubject(row).setName(name.getText());
				getETSData().getSubject(row).setTPPW(Integer.parseInt(tppw.getText()));
				getETSData().getSubject(row).setPPPW(Integer.parseInt(pppw.getText()));
			} else
				dup = "Duplicate Entry for " + name.getText();
		} else if (tableView.getName().equalsIgnoreCase("room")) {
			if (getETSData().searchTeacher(name.getText())==row ||
				getETSData().searchRoom(name.getText())==-1) {
				getETSData().getRoom(row).setName(name.getText());
				getETSData().getRoom(row).setType(type.getText());
				getETSData().getRoom(row).setCapacity(
						Integer.parseInt(capacity.getText()));
			} else
				dup = "Duplicate Entry for " + name.getText();
		}

		if (!dup.equals("")) {
			JOptionPane.showMessageDialog(null, dup, "Duplicate",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else
			tableView.clearSelection();

		return true;
	}

	@SuppressWarnings( { "serial" })
	public void createTable(int c, final String[] names) {
		String tableViewName = "";
		dataTable.clear();

		switch (c) {
		case DataSet.SUBJECT:
			tableViewName="COURSE";
			for (int i = 0; i < getETSData().getSubjectLength(); i++)
				dataTable.add(getETSData().getSubjectRecord(i));
			break;
		case DataSet.STUDENT:
			tableViewName="STUDENT";
			for (int i = 0; i < getETSData().getStudentLength(); i++) {
				dataTable.add(getETSData().getStudentRecord(i));
				for (int j = 0; j < getETSData().getStudent(i).getGroupList().size(); j++)
					dataTable.add(getETSData().getGroupStudentRecord(i, j));
			}
			break;
		case DataSet.TEACHER:
			tableViewName="INVIGILATOR";
			for (int i = 0; i < getETSData().getTeacherLength(); i++)
				dataTable.add(getETSData().getTeacherRecord(i));
			break;
		case DataSet.ROOM:
			tableViewName="HALL";
			for (int i = 0; i < getETSData().getRoomLength(); i++)
				dataTable.add(getETSData().getRoomRecord(i));
			break;
		}

		// final
		// Create a model of the data.
		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return dataTable.size();
			}

			public Object getValueAt(int row, int col) {
				return dataTable.get(row)[col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return col == -1;
			}

			public void setValueAt(Object aValue, int row, int column) {
				dataTable.get(row)[column] = aValue;
			}
		};

		// Create the table
		tableView = new JTable(dataModel);
		tableView.setRowHeight(INITIAL_ROWHEIGHT);
		tableView.setName(tableViewName);

		scrollpane = new JScrollPane(tableView);
		tableAggregate = scrollpane;
		updateData(c);
	}

	public void updateData(int c) {
		dataList.clear();
		tmpList.clear();
		commonList.removeAll();
		
		switch (c) {
		case DataSet.SUBJECT:
			for (int i = 0; i < getETSData().getSubjectLength(); i++)
				dataList.add(i, i + "]" + getETSData().getSubject(i).getName());
			break;
		case DataSet.STUDENT:
			jcbStud.removeAllItems();
			jcbStud.updateUI();
			if (getETSData().getStudentLength() == 0)
				jcbStud.addItem("No Student");
			else
				jcbStud.addItem("Select Student");

			for (int i = 0; i < getETSData().getStudentLength(); i++)
				jcbStud.addItem(getETSData().getStudent(i).getName());

			for (int i = 0,k=0; i < getETSData().getStudentLength(); i++) {
				String s = getETSData().getStudent(i).getName();
				int size=getETSData().getStudent(i).getGroupList().size();
				tmpList.add(String.valueOf(i));
				dataList.add(k++,"Class:"+ (i+1) + " " +s);
				for (int j = 0; j < size ; j++) {
					tmpList.add(String.valueOf(i) + ":" + String.valueOf(j));
					dataList.add(k++, "Batch:"+ (j+1) + " " + getETSData().getStudent(i).getGroupName(j));
				}
			}
			break;
		case DataSet.TEACHER:
			for (int i = 0; i < getETSData().getTeacherLength(); i++)
				dataList.add(i, i + "]" + getETSData().getTeacher(i).getName());
			break;
		case DataSet.ROOM:
			for (int i = 0; i < getETSData().getRoomLength(); i++)
				dataList.add(i, i + "]" + getETSData().getRoom(i).getName());
			break;
		}
		
		commonList.setListData(dataList);
		commonList.updateUI();
	}
}
