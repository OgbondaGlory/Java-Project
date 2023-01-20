package ETS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ShowHidePanel extends JPanel {
	JPanel Panel0, Panel1, Panel2, Panel3, p, p1;
	JButton hideBtn;
	String btnStr;
	JLabel nrecStatus;

	public ShowHidePanel() {
		hideBtn = new JButton("[ X ]");
		hideBtn.setBorderPainted(false);
		hideBtn.setContentAreaFilled(false);
		Panel0 = new JPanel();
		Panel1 = new JPanel();
		Panel2 = new JPanel();
		Panel3 = new JPanel();
		nrecStatus = new JLabel();
	}

	public JPanel addPanelSet(String title) {

		p = new JPanel();
		p1 = new JPanel();

		p.setLayout(new GridLayout(3, 1));
		p1.setLayout(new BorderLayout());
		p1.setBorder(new LineBorder(Color.WHITE, 10));

		JPanel p0 = new JPanel();
		p0.setLayout(new BorderLayout());
		Panel0.setBorder(new TitledBorder(title));

		p0.add(Panel0, BorderLayout.CENTER);

		p1.add(Panel1, BorderLayout.CENTER);
		p1.add(Panel3, BorderLayout.SOUTH);

		JPanel b0 = new JPanel();
		b0.setLayout(new FlowLayout(0, 0, FlowLayout.RIGHT));
		b0.setBorder(new LineBorder(Color.BLACK, 2));
		b0.add(hideBtn);
		b0.add(nrecStatus);
		p1.add(b0, BorderLayout.NORTH);

		p.add(Panel0);
		p.add(Panel2);

		hideBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HIDEP();
			}
		});

		return p;
	}

	public void SHOWP() {
		p.remove(Panel2);
		p.add(p1);
		p.add(Panel2);
		p.updateUI();
	}

	public void HIDEP() {
		p.remove(p1);
		p.updateUI();
	}

	public void SetStatusLabel(int no) {
		nrecStatus.setText("         No.of Records : " + no);
	}

	public JPanel getPanelSet(int pno) {
		switch (pno) {
		case 0:
			return Panel0;
		case 1:
			return Panel1;
		case 2:
			return Panel2;
		case 3:
			return Panel3;
		}
		return null;
	}

	public boolean validate(JTextField tf, int ei) {
		if (ei == 0) {//emptyness test
			if (tf.getText().equals(null) || tf.getText().equals("")) {
				return false;
			}
		} else if (ei == 1) {// integer test
			try {
				int i = Integer.parseInt(tf.getText());
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
}
