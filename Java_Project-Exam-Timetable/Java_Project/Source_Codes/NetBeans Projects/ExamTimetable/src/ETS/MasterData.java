package ETS;

/*
 * @(#)MasterData.java	1.14 04/07/26
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import DataSourceKB.*;

/**
 * Internal Frames Demo
 */
public class MasterData extends ETSModule implements ActionListener {
	JTabbedPane tabbedpane;
	RulesPanel rp;
	MasterPanel mp;
	ActivityPanel ap;
	JPanel p;
	/**
	 * MasterData Constructor
	 */

	public MasterData(ETS ttgs) {
		super(ttgs, "MasterData", "toolbar/Mast16.png");

		// create tab
		tabbedpane = new JTabbedPane();
		getDemoPanel().add(tabbedpane, BorderLayout.CENTER);

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Data Entry
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		String name ="Step 1 >> "+ getString("MasterData.dept");
		mp = new MasterPanel(ttgs);
		tabbedpane.add(name, mp.getMasterPanel());

		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		// Activity Manager
		// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

		name ="Step 2 >> "+ getString("MasterData.activity");
		ap = new ActivityPanel(ttgs);
		tabbedpane.add(name,ap.getActivityPanel());
		
		name = "Step 3 >> "+ getString("MasterData.rule");
		rp = new RulesPanel(ttgs);
		tabbedpane.add(name, rp.getRulesPanel());
		
		tabbedpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedpane.getSelectedIndex() == 1) {
					ap.UploadListData(DataSet.TEACHER);
					ap.UploadListData(DataSet.STUDENT);
					ap.UploadListData(DataSet.ROOM);
					ap.UploadListData(DataSet.SUBJECT);
					ap.UploadListData(DataSet.ACTIVITY);
					ap.UploadListData(DataSet.ASSIGNSUBJECT);
				}	
			}
		});

	}

	public void actionPerformed(ActionEvent e) {

	}

}
