package ETS.Timetable;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import DataSourceKB.ActivitySet;
import DataSourceKB.AssigndSubjects;
import DataSourceKB.DataSet;
import DataSourceKB.SubjectSet;

public class ETSGenerator {
	private int p, r, c;
	private int tchLen, studLen, roomLen, subLen,actLen;
	private String Break, div, log = "";

	boolean ACTIVITY_SUCCESS = false;
	boolean GENERATE_SUCCESS = false;

	private ResourceBundle bundle = null;
	private DataSet ds;
	private ActivitySet activity;
	private ArrayList<AssigndSubjects> assignSub;

	Matrix3D<Boolean> room;
	Matrix3D<Boolean> tch;
	Matrix3D<Boolean> stud;

	Matrix3D<String> studTimetable;
	Matrix3D<String> stud1Timetable;
	Matrix3D<String> tchTimetable;
	Matrix3D<String> roomTimetable;

	Vector<Boolean> isActPlace;

	Matrix2D<Integer> THPlaced;
	Matrix3D<Integer> PRPlaced;
	Matrix2D<String> ACTPlaced;

	public ETSGenerator() {
	}

	public void SetData(DataSet ds) {
		this.ds = ds;
		this.assignSub = ds.getAssigndSubjectList();

		tchLen = ds.getTeacherLength();
		studLen = ds.getAllStudentLength();
		roomLen = ds.getRoomLength();
		subLen = ds.getSubjectLength();
		actLen = ds.getActivityLength();

		p = tchLen + studLen + roomLen;
		r = ds.getTimeSet().getHour().size();
		c = ds.getTimeSet().getDay().size();
		if (ds.getRule().getTeacher() == null)
			ds.getRule().setTeacher(new Matrix3D<Boolean>(tchLen, r, c));
		if (ds.getRule().getRoom() == null)
			ds.getRule().setRoom(new Matrix3D<Boolean>(roomLen, r, c));
		if (ds.getRule().getStudent() == null)
			ds.getRule().setStudent(new Matrix3D<Boolean>(studLen, r, c));
		// initMatrices();
	}

	public void initMatrices() {
	 System.out.println("Init is Start");
		GENERATE_SUCCESS = false;

		isActPlace = new Vector<Boolean>();
		for (int i = 0; i < ds.getActivityLength(); i++) {
			isActPlace.add(false);
		}

		THPlaced = new Matrix2D<Integer>(subLen, c);
		PRPlaced = new Matrix3D<Integer>(subLen, r, c);
		ACTPlaced=new Matrix2D<String>(r, c);
		
		THPlaced.Init(0);
		PRPlaced.Init(0);
		ACTPlaced.Init("#");

		stud = new Matrix3D<Boolean>(studLen, r, c);
		tch = new Matrix3D<Boolean>(tchLen, r, c);
		room = new Matrix3D<Boolean>(roomLen, r, c);
		stud.Init(true);
		tch.Init(true);
		room.Init(true);

		studTimetable = new Matrix3D<String>(studLen, r, c);
		tchTimetable = new Matrix3D<String>(tchLen, r, c);
		roomTimetable = new Matrix3D<String>(roomLen, r, c);

		// all resources available
		studTimetable.Init("[Empty]");
		tchTimetable.Init("[Empty]");
		roomTimetable.Init("[Empty]");

		Break = ds.getRule().getBreak();

		SetBusy(stud, ds.getRule().getStudent());
		SetBusy(tch, ds.getRule().getTeacher());
		SetBusy(room, ds.getRule().getRoom());

		SetBreaks(studTimetable, stud);
		SetBreaks(tchTimetable, tch);
		SetBreaks(roomTimetable, room);

		System.out.println("Init is over");
	}

	private void SetBusy(Matrix3D<Boolean> A, Matrix3D<Boolean> B) {
		if (A.getP() == B.getP() && A.getR() == B.getR()
				&& A.getC() == B.getC())
			for (int i = 0; i < A.getP(); i++)
				for (int j = 0; j < A.getR(); j++)
					for (int k = 0; k < A.getC(); k++) {
						boolean t = B.getContent(i, j, k);
						A.setContent(i, j, k, t);
					}
	}

	private void SetBreaks(Matrix3D<String> A, Matrix3D<Boolean> B) {
		for (int T = 0; T < B.getP(); T++)
			for (int Days = 0; Days < B.getC(); Days++)
				for (int TimeSlot = 0; TimeSlot < B.getR(); TimeSlot++)
					if (ds.getTimeSet().getHour().get(TimeSlot)
							.equalsIgnoreCase(Break)) {
						A.setContent(T, TimeSlot, Days, "[ Break ]");
						B.setContent(T, TimeSlot, Days, false);
					}
	}

			String temp,s;
			
	public void MainAlgo(int itration) {
		if (GENERATE_SUCCESS) {
			System.out.println("FIN" + itration);
		} else {
			initMatrices();
			Random ActR = new Random(ds.getActivityLength());
			log += "\nStart MainAlgo >>" + "Iteration " + itration;
			for (int Days = 0; Days < c; Days++) {
				for (int TimeSlot = 0; TimeSlot < r; TimeSlot++) {
					for (int activity = 0; activity < ds.getActivityLength(); activity++) {
						int randAct = ActR.getRandValue(activity);
						if (!isActPlace.get(randAct).booleanValue()) {
							if (placeActivity(randAct, TimeSlot, Days)) {
								isActPlace.set(randAct, true);
								ActivitySet aa = ds.getActivity(randAct);
								log += "\n " + randAct + "[" + aa.getTeacher()
										+ "," + aa.getSubject() + ","
										+ aa.getStudent() + "," + aa.getTag()
										+ "]" + "(" + TimeSlot + ":" + Days
										+ ")";
								/*
								s=ACTPlaced.getContent(TimeSlot, Days);
								temp=s+String.valueOf(randAct)+",";
								ACTPlaced.setContent(TimeSlot, Days,temp);*/
							}
						}
					}
				}
			}
			log += "\nEnd MainAlgo >>";

			for (int i = 0; i < ds.getActivityLength(); i++) {
				int randAct = ActR.getRandValue(i);
				if (!isActPlace.get(randAct).booleanValue()) {
					GENERATE_SUCCESS = false;
					log += "\nInteruppted at >>" + randAct;
					break;
				} else {
					GENERATE_SUCCESS = true;
					createCombinedTT();
				}
			}
			
			if(GENERATE_SUCCESS) ACTPlaced.print();
		}

	}

	private void createCombinedTT() {
		int StudLen = ds.getStudentLength();
		stud1Timetable = new Matrix3D<String>(StudLen, r, c);
		stud1Timetable.Init("[Empty]");
		div = "&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; ";
		for (int i = 0; i < StudLen; i++) {
			String sname = ds.getStudent(i).getName();
			for (int Days = 0; Days < c; Days++)
				for (int TimeSlot = 0; TimeSlot < r; TimeSlot++) {
					int j = searchStudent(sname);
					int lim = j + ds.getStudent(i).getGroupList().size();
					String stemp = "<table border=0>";
					for (; j < lim + 1; j++) {
						String sname1 = ds.getStudentNames().get(j);
						String entry = studTimetable.getContent(j, TimeSlot,
								Days);
						if (entry.contains("Break")) {
							if (stemp.contains("Break"))
								break;
							stemp += "<tr><td align=center>" + entry
									+ "</td></tr>";
						} else {
							if (j == searchStudent(sname)) {
								if (entry.contains("Empty")) {
									stemp += "<tr><td align=center>Practical</td></tr>";
								} else {
									stemp += "<tr><td align=center>Theory</td></tr>";
									stemp += "<tr><td align=center>" + sname1
											+ div + entry + "</td></tr>";
									break;
								}
							} else {
								if (entry.contains("Empty")) {
									stemp += "<tr><td align=center>&nbsp;</td></tr>";
								} else {
									stemp += "<tr><td align=center>" + sname1
											+ div + entry + "</td></tr>";
								}
							}
						}
					}
					stemp += "</table>~";
					stud1Timetable.setContent(i, TimeSlot, Days, stemp);
				}
		}
	}

	private boolean placeActivity(int actnum, int TimeSlot, int Days) {
		int T, S, R = -1, subno, dur;
		String tag, tname, sname, roomname, subname;
		String stemp, stemp1;
		String ttemp, ttemp1;
		String rtemp, rtemp1;

		dur = ds.getActivity(actnum).getDuration();
		tag = ds.getActivity(actnum).getTag();
		subname = ds.getActivity(actnum).getSubject();
		subno = ds.searchSubject(subname);

		tname = ds.getActivity(actnum).getTeacher();
		T = ds.searchTeacher(tname);

		sname = ds.getActivity(actnum).getStudent();
		S = searchStudent(sname);

		roomname = "";
		for (int i = 0; i < ds.getRoomLength(); i++) {
			roomname = ds.getRoom(i).getName();
			String type = ds.getRoom(i).getType();

			if (tag.equals("TH") && type.equals("CLASS")) {
				R = ds.searchRoom(roomname);
				if (isRoomAvail(R, TimeSlot, Days))
					break;
			} else if ((tag.equals("PR") || tag.equals("TUT"))
					&& type.equals("LAB")) {// Pract =2
				R = ds.searchRoom(roomname);
				if (isRoomAvail(R, TimeSlot, Days))
					break;
			}
		}

		div = "  ";
		stemp = roomname + "$(" + tname + div + subname + ")$" + tag;
		stemp1 = roomname + "$(" + tname + div + subname + ")$" + tag;

		ttemp = roomname + "$(" + sname + div + subname + ")$" + tag;
		ttemp1 = roomname + "$(" + sname + div + subname + ")$" + tag;

		rtemp = sname + "$(" + tname + div + subname + ")$" + tag;
		rtemp1 = sname + "$(" + tname + div + subname + ")$" + tag;

		boolean TSR = isTeachAvail(T, TimeSlot, Days)
				&& isStudAvail(S, TimeSlot, Days)
				&& isRoomAvail(R, TimeSlot, Days);

		if (TSR) {
			if (tag.equals("TH")) {
				int tH = THPlaced.getContent(subno, Days);
				if (tH < dur) {
					THPlaced.setContent(subno, Days, tH + dur);
				} else {
					return false;
				}
				setStudUnAvail(S, TimeSlot, Days, actnum);
				studTimetable.setContent(S, TimeSlot, Days, stemp);
				tch.setContent(T, TimeSlot, Days, false);
				tchTimetable.setContent(T, TimeSlot, Days, ttemp);
				room.setContent(R, TimeSlot, Days, false);
				roomTimetable.setContent(R, TimeSlot, Days, rtemp);

				return true;
			} else {// Pract =2
				if (!isLast(TimeSlot)) {
					boolean TSR1 = isTeachAvail(T, TimeSlot + 1, Days)
							&& isStudAvail(S, TimeSlot + 1, Days)
							&& isRoomAvail(R, TimeSlot + 1, Days);

					if (!isBreak(TimeSlot) && TSR1) {
						int pR = PRPlaced.getContent(subno,TimeSlot,Days);
						if (pR < dur) {
							PRPlaced.setContent(subno,TimeSlot,Days, pR + dur);
						} else {
							return false;
						}
						
						setStudUnAvail(S, TimeSlot, Days, actnum);
						studTimetable.setContent(S, TimeSlot, Days, stemp);
						studTimetable.setContent(S, TimeSlot + 1, Days, stemp1);

						tch.setContent(T, TimeSlot, Days, false);
						tch.setContent(T, TimeSlot + 1, Days, false);
						tchTimetable.setContent(T, TimeSlot, Days, ttemp);
						tchTimetable.setContent(T, TimeSlot + 1, Days, ttemp1);

						room.setContent(R, TimeSlot, Days, false);
						room.setContent(R, TimeSlot + 1, Days, false);
						roomTimetable.setContent(R, TimeSlot, Days, rtemp);
						roomTimetable.setContent(R, TimeSlot + 1, Days, rtemp1);
						
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else
			return false;

	}

	private boolean isLast(int timeSlot) {
		if (r - 1 == timeSlot)
			return true;
		else
			return false;
	}

	private boolean isBreak(int TimeSlot) {
		if (ds.getTimeSet().getHour().get(TimeSlot + 1).equalsIgnoreCase(Break))
			return true;
		else
			return false;
	}

	public int searchStudent(String sname) {
		int S = 0;
		for (int i = 0; i < ds.getAllStudentLength(); i++) {
			if (ds.getStudentNames().get(i).equals(sname)) {
				S = i;
				break;
			}
		}
		return S;
	}

	private void setStudUnAvail(int s, int timeSlot, int days, int actnum) {
		int size = 0;
		int gsize = 0;
		for (int i = 0; i < ds.getStudentLength(); i++) {
			gsize = ds.getStudent(i).getGroupList().size();
			size += gsize + 1;
			if (s < size) {
				if (ds.getActivity(actnum).getTag().equals("TH")) {
					for (; s < size; s++)
						stud.setContent(s, timeSlot, days, false);
					break;
				} else {
					int parent = size - gsize - 1;
					stud.setContent(parent, timeSlot, days, false);
					stud.setContent(parent, timeSlot + 1, days, false);
					stud.setContent(s, timeSlot, days, false);
					stud.setContent(s, timeSlot + 1, days, false);
					
					setOtherStudUnAvail(parent, timeSlot, days);
					break;
				}
			}
		}
	}

	private void setOtherStudUnAvail(int s, int timeSlot, int days) {
		int size = 0;
		int gsize = 0;
		for (int i = 0; i < ds.getStudentLength(); i++) {
			if (size == s) {
				gsize = ds.getStudent(i).getGroupList().size();
				size += gsize + 1;
			} else {
				gsize = ds.getStudent(i).getGroupList().size();
				for (int z = 0; z < gsize; z++) {
					int OtherS = size + z + 1;
					stud.setContent(OtherS, timeSlot, days, false);
					stud.setContent(OtherS, timeSlot + 1, days, false);
				}
				size += gsize + 1;
			}
		}
	}

	private boolean isAllBatchesInPractical(int s, int timeSlot, int days){
		int size = 0;
		int gsize = 0;
		for (int i = 0; i < ds.getStudentLength(); i++) {
			gsize = ds.getStudent(i).getGroupList().size();
			if (size == s) {
				for (int z = s; z < gsize; z++) {
					int OtherS = size + z + 1;
					stud.getContent(OtherS, timeSlot, days);
					stud.setContent(OtherS, timeSlot + 1, days, false);
				}				
			}
			size += gsize + 1;
		}
		return false;
	}
	
	private boolean isStudAvail(int i, int j, int k) {
		return stud.getContent(i, j, k).booleanValue();
	}

	private boolean isTeachAvail(int i, int j, int k) {
		return tch.getContent(i, j, k).booleanValue();
	}

	private boolean isRoomAvail(int i, int j, int k) {
		if (i >= 0) {
			return room.getContent(i, j, k);
		} else
			return false;
	}

	public String getString(String key) {
		String value = "nada";
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("resources.ttgs");
		}
		try {
			value = bundle.getString(key);
		} catch (MissingResourceException e) {
			System.out
					.println("java.util.MissingResourceException: Couldn't find value for: "
							+ key);
		}
		return value;
	}

	public Matrix2D<String> getStudTimetable(int i) {
		Matrix2D<String> TimeTable;
		TimeTable = studTimetable.get2DPart(i);
		return TimeTable;
	}

	public Matrix2D<String> getStud1Timetable(int i) {
		Matrix2D<String> TimeTable;
		TimeTable = stud1Timetable.get2DPart(i);
		return TimeTable;
	}

	public Matrix2D<String> getTchTimetable(int i) {
		Matrix2D<String> TimeTable;
		TimeTable = tchTimetable.get2DPart(i);
		return TimeTable;
	}

	public Matrix2D<String> getRoomTimetable(int i) {
		Matrix2D<String> TimeTable;
		TimeTable = roomTimetable.get2DPart(i);
		return TimeTable;
	}

	public boolean isAllActivitiesPlace() {
		return GENERATE_SUCCESS;
	}

	public boolean isActivitiesGenerated() {
		return ACTIVITY_SUCCESS;
	}

	public void GenerateActivities() {
		int i = 0, TH = 0, PR = 0;
		int thour = Integer.parseInt(getString("MasterData.Dur_Theory"));
		int phour = Integer.parseInt(getString("MasterData.Dur_Practical"));
		if (ds.getActivityList().size() > 0)
			ds.getActivityList().clear();

		for (int j = 0; j < assignSub.size(); j++) {
			SubjectSet temp = assignSub.get(j).getSubj();
			for (int ta = 0; ta < temp.getTPPW(); ta++, i++) {
				activity = new ActivitySet(i);
				activity.setTag("TH");
				activity.setStudent(assignSub.get(j).getStudTH());
				activity.setSubject(temp.getName());
				activity.setTeacher(assignSub.get(j).getTchTH());
				activity.setDuration(thour);
				TH++;
				ds.addActivity(i, activity);
			}

			int n = 0;
			int size = assignSub.get(j).getTchPR().size();

			for (int m = 0; m < assignSub.get(j).getStudPR().size(); m++, i++) {
				if (size == 1)
					n = 0;
				else
					n = m / size;
				activity = new ActivitySet(i);
				if (temp.getPPPW() > 1)
					activity.setTag("PR");
				else
					activity.setTag("TUT");
				activity.setStudent(assignSub.get(j).getStudPR().get(m));
				activity.setTeacher(assignSub.get(j).getTchPR().get(n));
				activity.setSubject(temp.getName());
				activity.setDuration(phour);
				PR++;
				ds.addActivity(i, activity);
			}
		}
		ds.setTHPR(TH, PR);

		if ((ds.getActivityList().size() == (ds.getPR() + ds.getTH()))
				&& (ds.getActivityList().size() != 0))
			ACTIVITY_SUCCESS = true;
		else
			ACTIVITY_SUCCESS = false;
	}

	public void MainAlgo(JFrame f) {
		initMatrices();
		ETSProgressBar tpb = new ETSProgressBar(ds.getActivityList().size()/2,f);
		File fo = new File("log.txt");
		try {
			OutputStream fos = new FileOutputStream(fo);
			fos.write(log.getBytes());
			fos.close();
			log = "";
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class ETSProgressBar {
		final static int interval = 200;
		int i, work;
		JLabel label;
		JProgressBar pb;
		Timer timer;
		JButton button;
		JDialog dlg;
		String title, str1;

		public ETSProgressBar(int work1, JFrame baseFrame) {
			this.work = work1;
			button = new JButton("Start");
			button.addActionListener(new ButtonListener());
			if (!isActivitiesGenerated()) {
				title = "No activity to generate timetable";
				button.setEnabled(false);
			} else
				title = "Timetable Generation !";

			pb = new JProgressBar(0, work);
			pb.setValue(0);
			pb.setStringPainted(true);

			label = new JLabel("Timetable Generation in progress :");

			// Create a timer.
			timer = new Timer(interval, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (i == work || isAllActivitiesPlace()) {
						Toolkit.getDefaultToolkit().beep();
						timer.stop();
						button.setEnabled(true);
						pb.setValue(0);
						if (isAllActivitiesPlace()) 
                                                {
							str1 = "<html><font color=\"#00FF00\"><b>Timetable Generation completed.</b>"
									+ "</font>" + "</html>";
						} 
                                                else 
                                                {
							str1 = "<html><font color=\"#FF0000\"><b>Timetable Generation not completed.</b></font></html>";
						}
						label.setText(str1);
						if (isAllActivitiesPlace()) 
                                                {
							pb.setValue(work);
							JOptionPane.showMessageDialog(null,
									"Timetable Generated Successfully");
							dlg.setVisible(false);
						} else {
							pb.setValue(work);
							JOptionPane.showMessageDialog(null,
									"Timetable not Generated !");
							dlg.setVisible(false);
						}
					}
					MainAlgo(i);
					i = i + 1;
					pb.setValue(i);
				}
			});

			JPanel panel = new JPanel();
			panel.add(button);
			panel.add(pb);

			JPanel panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.add(panel, BorderLayout.NORTH);
			panel1.add(label, BorderLayout.CENTER);
			panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			dlg = new JDialog(baseFrame, title, true);
			dlg.setContentPane(panel1);
			dlg.pack();
			dlg.setVisible(true);
			dlg.setBounds(200, 200, dlg.getWidth(), dlg.getHeight());
			dlg.setResizable(false);
		}

		class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				button.setEnabled(false);
				i = 0;
				String str = "<html>" + "<font color=\"#008000\">" + "<b>"
						+ "Timetable Generation in progress ........." + "</b>"
						+ "</font>" + "</html>";
				label.setText(str);
				timer.start();
			}
		}
	}

}
