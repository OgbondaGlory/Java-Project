package ETS.Timetable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import DataSourceKB.DataSet;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

public class OutputGenerator {
	public static int PDF = 0;
	public static int HTML = 1;

	String path,cname,dname;
	String[] wDay = { "Monday", "Tuesday", "Wednesday", "Thrusday", "Friday" };
	String[] wHour = { "Hour 1", "Hour 2", "Hour 3", "Hour 4", "Hour 5",
			"Hour 6", "Hour 7", "Hour 8" };
	int ii;

	Vector<String> day;
	Vector<String> hour;
	Vector<String> teacher;
	Vector<String> room;
	Vector<String> student;

	DataSet ds;
	ETSGenerator ttgsGen;
	Matrix2D<String> TimeTable, empty;

	public OutputGenerator(String path) {
		this.path = path;
		empty = new Matrix2D<String>(wHour.length, wDay.length);
		empty.Init("\n---X---\n");
	}

	public String getJTEntry(String entry) {
		if (entry.contains("TUT")) {
			entry = entry.replace("TUT", "").replace("$", "<br>");
			entry = "<body text=#123ABC><b>" + entry + "</b></body>";
		} else if (entry.contains("PR")) {
			entry = entry.replace("PR", "").replace("$", "<br>");
			entry = "<body text=#123456><b>" + entry + "</b></body>";
		} else if (entry.contains("TH")) {
			entry = entry.replace("TH", "").replace("$", "<br>");
			entry = "<body text=blue><b>" + entry + "</b></body>";
		} else if (entry.contains("Break")) {
			entry = entry.replace("[", "<br>").replace("]", "<br>");
			entry = "<body text=red><b>" + entry + "</b></body>";
		} else {
			entry = entry.replace("[", "<br>").replace("]", "<br>");
			entry = "<body text=green><b>" + "--X--" + "</b></body>";
		}
		return entry;
	}

	public String getHTMLEntry(String entry) {
		if (entry.contains("TUT")) {
			entry = entry.replace("TUT", "").replace("$", "<br>");
			entry = "<body text=#123ABC><b>" + entry + "</b></body>";
		} else if (entry.contains("PR")) {
			entry = entry.replace("PR", "").replace("$", "<br>");
			entry = "<font color=orange><b>" + entry + "</b></font>";
		} else if (entry.contains("TH")) {
			entry = entry.replace("TH", "").replace("$", "<br>");
			entry = "<font color=blue><b>" + entry + "</b></font>";
		} else if (entry.contains("Break")) {
			entry = entry.replace("[", "<br>").replace("]", "<br>");
			entry = "<font color=red><b>" + entry + "</b></font>";
		} else {
			entry = entry.replace("[", "<br>").replace("]", "<br>");
			entry = "<font color=green><b>" + "--X--" + "</b></font>";
		}
		return entry;
	}

	public String getPlainEntry(String entry) {
		if (entry.contains("TUT")) {
			entry = entry.replace("TUT", "").replace("$", "");
		} else if (entry.contains("PR")) {
			entry = entry.replace("PR", "").replace("$", "");
		} else if (entry.contains("TH")) {
			entry = entry.replace("TH", "").replace("$", "");
		} else if (entry.contains("Break")) {
			entry = entry.replace("[", "").replace("]", "");
		} else {
			entry = entry.replace("[", "").replace("]", "");
		}
		return entry;
	}

	public JTable getTimeTable(int i, int whichTable) {
		ii = i;

		if (i == -1) {
			TimeTable = empty;
		} else {
			switch (whichTable) {
			case 0:
				TimeTable = ttgsGen.getStud1Timetable(i);
				break;
			case DataSet.STUDENT:
				TimeTable = ttgsGen.getStudTimetable(i);
				break;
			case DataSet.TEACHER:
				TimeTable = ttgsGen.getTchTimetable(i);
				break;
			case DataSet.ROOM:
				TimeTable = ttgsGen.getRoomTimetable(i);
				break;
			default:
				break;
			}
		}

		final JTable tableView;

		TableModel dataModel = new AbstractTableModel() {
			public int getColumnCount() {
				return TimeTable.getC() + 1;
			}

			public int getRowCount() {
				return TimeTable.getR();
			}

			public Object getValueAt(int row, int col) {
				if (col == 0) {
					if (ii < 0)
						return wHour[row];
					else
						return ds.getTimeSet().getHour().get(row);
				} else {
					String entry = TimeTable.getContent(row, col - 1);
					if (entry.contains("~")) {
						entry = entry.replace("~", "");
						entry = getJTEntry(entry);
					} else {
						entry = getJTEntry(entry);
					}
					entry = "<html>" + entry + "</html>";
					return entry;
				}
			}

			public String getColumnName(int column) {
				if (column == 0)
					return "Time\\Day";
				else {
					if (ii < 0)
						return wDay[column - 1];
					else
						return ds.getTimeSet().getDay().get(column - 1);
				}
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return col == -1;
			}

			public void setValueAt(Object aValue, int row, int column) {
				// TimeTableO.setContent(row,column,aValue.toString());
			}
		};

		tableView = new JTable(dataModel);
		tableView.setRowSelectionAllowed(false);
		tableView.setColumnSelectionAllowed(false);
		tableView.getTableHeader().setReorderingAllowed(false);
		tableView.getTableHeader().setBackground(Color.LIGHT_GRAY);
		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(75);
		return tableView;
	}

	public void toPDF(String fname, Matrix2D<String> tt) {
		File f = new File(path + "/PDFTABLE/" + fname + ".pdf");
		File dir = new File(f.getParent());
		if (!dir.isDirectory()) {
			// System.out.println(dir.getPath() + "is Not Avail");
			if (dir.mkdir())
				System.out.println("Created");
		}
		try {
			Document document = new Document(PageSize._11X17, 50, 50, 50, 50);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(f));
			document.open();
			Paragraph title1 = new Paragraph(fname + " Timetable", FontFactory
					.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC,
							new Color(0, 0, 255)));

			Chapter chapter1 = new Chapter(title1, 1);
			chapter1.setNumberDepth(0);
			Section section1 = chapter1.addSection(title1);
			section1.add(HorizontalTable(tt));
			document.add(chapter1);
			document.close();
		} catch (Exception e2) {
			MSGBOX("Fail to create", e2.getMessage());
		}
	}

	public Table HorizontalTable(Matrix2D<String> tt) {
		Table t1;
		Table t2;
		Table tm;

		try {
			t1 = new Table(day.size() + 1, 1);
			t2 = new Table(day.size() + 1, hour.size());
			t1.setAlignment(Table.ALIGN_CENTER);
			t2.setAlignment(Table.ALIGN_CENTER);

			for (int j = 0; j < day.size() + 1; j++) {
				t1.setCellsFitPage(true);
				if (j == 0)
					t1.addCell(new Cell("Time/Day"));
				else
					t1.addCell(new Cell(day.get(j - 1)));
			}

			for (int j = 0; j < hour.size(); j++) {
				for (int i = 0; i < day.size() + 1; i++) {
					t2.setCellsFitPage(true);
					if (i == 0)
						t2.addCell(hour.get(j));
					else {
						String entry = tt.getContent(j, i - 1);
						if (entry.contains("~")) {
							entry = entry.replace("~", "");
							entry = getPlainEntry(entry);
						} else {
							entry = getPlainEntry(entry);
						}
						t2.addCell(entry);
					}
				}
			}

			tm = new Table(1, 3);
			tm.setAlignment(Table.ALIGN_CENTER);
			tm.setBorderWidth(1);
			tm.setPadding(5);
			tm.setSpacing(5);
			tm.addCell(new Cell(cname+"\n "+dname));
			tm.insertTable(t1);
			tm.insertTable(t2);
			return tm;
		} catch (BadElementException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void toHTML(String fname, Matrix2D<String> tt) {
		File f = new File(path + "/HTMLTABLE/" + fname + ".html");
		File dir = new File(f.getParent());
		if (!dir.isDirectory()) {
			// System.out.println(dir.getPath() + "is Not Avail");
			if (dir.mkdir())
				System.out.println("Created");
		}
		try {
			OutputStream fos = new FileOutputStream(f);

			String html = "<html>\n";
			html += "<title>" + fname + " Timetable</title>\n";
			html += "<body><h1>" + fname + " Timetable</h1>\n";
			html += HorizontalHTMLTable(tt);
			html += "</body>\n</html>";

			fos.write(html.getBytes());
			fos.close();
		} catch (Exception e2) {
			MSGBOX("Fail to create", e2.getMessage());
		}
	}

	public String HorizontalHTMLTable(Matrix2D<String> tt) {
		String htmlTable = "";
		htmlTable += "<table border=1>\n";
		htmlTable += "<tr>" + cname+"<br>"+ dname+ "</tr>\n";
		htmlTable += "<tr>";

		for (int j = 0; j < day.size() + 1; j++) {
			if (j == 0)
				htmlTable += "<td>" + "Time/Day" + "</td>\n";
			else
				htmlTable += "<td>" + day.get(j - 1) + "</td>\n";
		}
		htmlTable += "</tr>\n";

		for (int j = 0; j < hour.size(); j++) {
			htmlTable += "<tr>";
			for (int i = 0; i < day.size() + 1; i++) {
				if (i == 0)
					htmlTable += "<td>" + hour.get(j) + "</td>\n";
				else {
					String entry = tt.getContent(j, i - 1);
					if (entry.contains("~")) {
						entry = entry.replace("~", "");
						entry = getHTMLEntry(entry);
					} else {
						entry = getHTMLEntry(entry);
					}
					htmlTable += "<td>" + entry + "</td>\n";
				}
			}
			htmlTable += "</tr>\n";
		}

		htmlTable += "</table>\n";

		return htmlTable;
	}

	public String OnlyFormat() {
		String str = "";
		if (ttgsGen.isAllActivitiesPlace()) {
			if (ds.getTeacherLength() == 0)
				MSGBOX("Teacher Timetable", "No Teacher Timetable Generated");
			else
				str += "Teacher's ,";
			if (ds.getStudentLength() == 0)
				MSGBOX("Student Timetable", "No Student Timetable Generated");
			else
				str += " Student's ,";
			if (ds.getRoomLength() == 0)
				MSGBOX("Room Timetable", "No Room Timetable Generated");
			else
				str += " Room's ";
		} else
			JOptionPane.showMessageDialog(null, "Generate Timetable first !");

		return str;
	}

	public void OnlyFormat(int format) {
		if (ttgsGen.isAllActivitiesPlace()) {
			if (ds.getTeacherLength() == 0) {
			} else {
				for (int i = 0; i < teacher.size(); i++) {
					TimeTable = ttgsGen.getTchTimetable(i);
					if (format == PDF)
						toPDF(teacher.get(i), TimeTable);
					if (format == HTML)
						toHTML(teacher.get(i), TimeTable);
				}
			}
			if (ds.getStudentLength() == 0) {
			} else {
				for (int i = 0; i < student.size(); i++) {
					TimeTable = ttgsGen.getStudTimetable(i);
					if (format == PDF)
						toPDF(student.get(i), TimeTable);
					if (format == HTML)
						toHTML(student.get(i), TimeTable);
				}
				for (int i = 0; i < ds.getStudentLength(); i++) {
					TimeTable = ttgsGen.getStud1Timetable(i);
					String t = ds.getStudent(i).getName() + "Compact";
					if (format == PDF)
						toPDF(t, TimeTable);
					if (format == HTML)
						toHTML(t, TimeTable);
				}
			}

			if (ds.getRoomLength() == 0) {
			} else {
				for (int i = 0; i < room.size(); i++) {
					TimeTable = ttgsGen.getRoomTimetable(i);
					if (format == PDF)
						toPDF(room.get(i), TimeTable);
					if (format == HTML)
						toHTML(room.get(i), TimeTable);
				}
			}
		}
	}

	public void AllHTML(JFrame f) {
		String str = OnlyFormat();
		if (str != "") {
			ETSProgressBar tpb = new ETSProgressBar(str, "HTML Format", HTML,f);
		}
	}

	public void AllPDF(JFrame f) {
		String str = OnlyFormat();
		if (str != "") {
			ETSProgressBar tpb = new ETSProgressBar(str, "PDF Format", PDF, f);
		}
	}

	public void MSGBOX(String title, String msg) {
		JOptionPane.showMessageDialog(null, msg, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void SetData(DataSet ds, ETSGenerator ttgsGen) {
		this.ds = ds;
		this.ttgsGen = ttgsGen;

		day = ds.getTimeSet().getDay();
		hour = ds.getTimeSet().getHour();
		teacher = ds.getTeacherNames();
		student = ds.getStudentNames();
		room = ds.getRoomNames();
		cname=ds.getRule().getCollageName();
		dname=ds.getRule().getDeptName();
	}

	class ETSProgressBar {
		final static int interval = 200;
		int i, format;
		JLabel label;
		JProgressBar pb;
		Timer timer;
		JButton button;
		JDialog dlg;
		String str;

		public ETSProgressBar(String str1, String title, int format1,
				JFrame baseFrame) {
			this.format = format1;
			this.str = str1;
			button = new JButton("Start");
			button.addActionListener(new ButtonListener());
			if (str == "") {
				title = "No timetable to export in " + title;
				button.setEnabled(false);
			} else
				title = str + " timetable to export in " + title;

			pb = new JProgressBar(0, 20);
			pb.setValue(0);
			pb.setStringPainted(true);

			label = new JLabel("Export to :" + title);

			// Create a timer.
			timer = new Timer(interval, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (i == 20) {
						Toolkit.getDefaultToolkit().beep();
						timer.stop();
						//button.setEnabled(true);
						//pb.setValue(0);
						String str1 = "<html>" + "<font color=\"#FF0000\">"
								+ "<b>" + "Exporting completed." + "</b>"
								+ "</font>" + "</html>";
						label.setText(str1);
						if (format == PDF)
							MSGBOX("Timetables",str	+ " Timetables(in .PDF format) Generated Sucsessfully!");
						else
							MSGBOX("Timetables",str	+ " Timetables(in .HTML format) Generated Sucsessfully!");
						dlg.setVisible(false);
					}
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
				OnlyFormat(format);
				button.setEnabled(false);
				i = 0;
				String str = "<html>" + "<font color=\"#008000\">" + "<b>"
						+ "Exporting is in process......." + "</b>" + "</font>"
						+ "</html>";
				label.setText(str);
				timer.start();
			}
		}
	}
}
