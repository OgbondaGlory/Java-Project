package ETS;

/*
 * @(#)ETSModule.java	1.21 05/03/25
 */

import java.awt.BorderLayout;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import DataSourceKB.DataSet;
import ETS.Timetable.ETSGenerator;

/**
 * A generic com.ETS demo module
 * 
 * @version 1.21 03/25/05
 * @author Jeff Dinkins
 */
public class ETSModule{
 
	protected ETS ets = null;
	private JPanel panel = null;
	private String resourceName = null;
	private String iconPath = null;

	// Resource bundle for internationalized and accessible text
	private ResourceBundle bundle = null;

	public ETSModule(ETS ets) {
		this(ets, null, null);
	}

	public ETSModule(ETS ets, String resourceName, String iconPath) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.resourceName = resourceName;
		this.iconPath = iconPath;
		this.ets = ets;
	}

	public String getResourceName() {
		return resourceName;
	}

	public JPanel getDemoPanel() {
		return panel;
	}

	public ETS getETS() {
		return ets;
	}

	public DataSet getETSData() {
		return ets.MAIN;
	}

	public ETSGenerator getETSGenerator() {
		ets.etsGen.SetData(getETSData());
		return ets.etsGen;
	}

	public void setETSData() {
		ets.MAIN = new DataSet();
	}

	public String getString(String key) {
		String value = "nada";
		if (bundle == null) {
			if (getETS() != null) {
				bundle = getETS().getResourceBundle();
			} else {
				bundle = ResourceBundle.getBundle("ETS.resources.ets");
			}
		}
		try {
			value = bundle.getString(key);
		} catch (MissingResourceException e) {
			System.out.println("java.util.MissingResourceException: Couldn't find value for: "
							+ key);
		}
		return value;
	}

	public char getMnemonic(String key) {
		return (getString(key)).charAt(0);
	}

	public String getName() {
		return getString(getResourceName() + ".name");
	};

	public Icon getIcon() {
		return getETS().createImageIcon(iconPath, getResourceName() + ".name");
	};

	public String getToolTip() {
		return getString(getResourceName() + ".tooltip");
	};

	void updateDragEnabled(boolean dragEnabled) {
	}
	
	public void MSGBOX(String title, String msg,int type){
		JOptionPane.showMessageDialog(null,  msg, title, type);		
	}
}
