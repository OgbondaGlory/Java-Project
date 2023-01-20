package ETS;

/*
 * @(#)HomeScreen.java	1.8 04/07/26
 */

import javax.swing.*;
import javax.swing.border.*;

import ETS.Login.LOGIN;
import ETS.Login.User;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 * ToolTip Demo
 */
public class HomeScreen extends ETSModule {
	JDialog loginDialog;
	JPanel pl = new JPanel();
	JButton Login_Btn;
	JButton Help_Btn = CreateButton(createImageIcon("toolbar/Help24.png"));
	JButton Export_Btn = CreateButton(createImageIcon("toolbar/Expo24.png"));
	JButton About_Btn = CreateButton(createImageIcon("toolbar/Abo24.png"));
	User user;
	LOGIN lg;
	/**
	 * HomeScreen Constructor
     * @param ets
	 */
	public HomeScreen(ETS ets) {
		// Set the title for this demo, and an icon used to represent this
		// demo inside the com.ETS app.
		super(ets, "HomeScreen", "toolbar/Home16.png");
		// Set the layout manager.
		lg = new LOGIN();
		JPanel p = getDemoPanel();
		p.setBorder(new LineBorder(Color.DARK_GRAY));
		p.setLayout(new BorderLayout());
		p.setBackground(Color.white);
		
		changeButton("login");
		Help_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openHelpWindow();
			}
		});

		Export_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExportBox();
			}
		});

		About_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutBox();
			}
		});

		JPanel psub = new JPanel();
		psub.setBorder(new LineBorder(Color.LIGHT_GRAY, 10));
		psub.setBackground(Color.white);
		psub.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(20, 10, 20, 10);
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		psub.add(pl, c);
		c.gridx = 50;
		psub.add(Help_Btn, c);
		c.gridx = 10;
		psub.add(new JLabel(createImageIcon("Logo33.gif")), c);
		c.gridy = 20;
		c.anchor = GridBagConstraints.SOUTH;
		psub.add(Export_Btn, c);
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		psub.add(About_Btn, c);

		p.add(new JLabel(createImageIcon("toolbar/Title32.png")),BorderLayout.NORTH);
		p.add(psub, BorderLayout.CENTER);
	}

	private ImageIcon createImageIcon(String filename) {
		return getETS().createImageIcon(filename, "");
	}

	public JButton CreateButton(ImageIcon icn) {
		JButton jb = new JButton(icn);
		jb.setContentAreaFilled(false);
		jb.setBorderPainted(false);
		return jb;
	}

	public void changeButton(String type) {
		if (type.equalsIgnoreCase("Login")) {
			Login_Btn = CreateButton(createImageIcon("toolbar/Login24.png"));
			Export_Btn.setEnabled(false);
			Login_Btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showLoginBox();
				}
			});

		} else if (type.equalsIgnoreCase("logout")) {
			Login_Btn = CreateButton(createImageIcon("toolbar/Logout24.png"));
			Export_Btn.setEnabled(true);
			Login_Btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
									user.getName()+" Are You Sure You Want To Logout ?", user.getType()+" Logout!!!",
									JOptionPane.YES_NO_OPTION)){
						getETS().unloadDemos();
						changeButton("login");
					}
				}
			});
		}
		pl.removeAll();
		pl.setBackground(Color.WHITE);
		pl.add(Login_Btn);
	}

	public void openHelpWindow() {
		ETSHelp dialog = new ETSHelp(ets.getFrame());
		dialog.show();
	}

	public void showAboutBox() {
		ets.showAbout();
	}

	public void showLoginBox() {
		loginDialog = new JDialog(ets.getFrame(), "Admininstrator Login", true);
		loginDialog.setBounds(300, 300, 0, 0);

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(buildLoginMainPanel(), BorderLayout.CENTER);

		loginDialog.getContentPane().add(container);

		loginDialog.pack();
		loginDialog.setResizable(false);
		loginDialog.show();

	}

	public JPanel buildLoginMainPanel() {

		JPanel loginMainP = new JPanel();
		loginMainP.setLayout(new BorderLayout(20, 20));

		JPanel loginPanel = new JPanel();
		loginPanel.setBorder(new LineBorder(Color.BLUE, 5));
		loginPanel.setLayout(new BorderLayout(20, 20));

		JLabel unL = new JLabel("User Name :");
		final JTextField unT = new JTextField(10);
		JLabel pdL = new JLabel("Password :");
		final JPasswordField pdT = new JPasswordField(10);
		unT.setText("");
		pdT.setText("");

		JPanel entryp = new JPanel();
		entryp.setLayout(new GridLayout(2, 2));
		entryp.add(unL);
		entryp.add(unT);
		entryp.add(pdL);
		entryp.add(pdT);
		pdT.setEchoChar(' ');
		loginPanel.add(entryp);

		JButton loginB = new JButton("Login");
		JButton resetB = new JButton("Reset");

		JPanel buttonp = new JPanel();
		buttonp.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		buttonp.add(loginB);
		buttonp.add(resetB);

		loginB.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (unT.getText().equals("") && pdT.getText().equals(""))
					MSGBOX( "Error","Must Enter User Name & Password",
							JOptionPane.ERROR_MESSAGE);
				else if (unT.getText().equals(""))
					MSGBOX("Error","Must Enter User Name",JOptionPane.ERROR_MESSAGE);
				else if (pdT.getText().equals(""))
					MSGBOX("Error", "Must Enter Password",JOptionPane.ERROR_MESSAGE);
				else {
					User u = new User();
					u.setUserName(unT.getText());
					u.setPassword(pdT.getText());
					user=lg.CHECK(u);					
					if(lg.isValid()){
						MSGBOX("Welcome", lg.getMsg(),JOptionPane.INFORMATION_MESSAGE);
						loginDialog.setVisible(false);
						getETS().loadDemos(user);
						changeButton("logout");
					}else{
						unT.setText("");
						pdT.setText("");
						MSGBOX("Error", lg.getMsg(),JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		resetB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unT.setText("");
				pdT.setText("");
			}
		});

		loginMainP.add(loginPanel, BorderLayout.CENTER);
		loginMainP.add(buttonp, BorderLayout.SOUTH);

		return loginMainP;
	}

	public void showExportBox() {
		URL img = getClass().getResource("/resources/images/toolbar/Expo32.png");
		String s0 = "<html><body bgcolor=\"#ffffff\"><img src=\"" + img
				+ "\"></body></html>";
		MSGBOX("Export", s0,
				JOptionPane.PLAIN_MESSAGE);
	}

}
