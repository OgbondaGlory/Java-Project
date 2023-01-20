package ETS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 * Internal Frames Demo
 */
public class RulesPanel extends ETSModule {
	private JPanel rulesPanel, mainRulesPanel;
	private Vector<ShowHideRulesPanel> SHRulePanel;	
	private ArrayList<RulesGUI> rg;
	
	/**
	 * MasterData Constructor
	 */
	public RulesPanel(ETS ttgs) {
		super(ttgs, "", "");

		rg=new ArrayList<RulesGUI>();
		

		SHRulePanel = new Vector<ShowHideRulesPanel>();
		mainRulesPanel = new JPanel();
		mainRulesPanel.setLayout(new BorderLayout());

		rulesPanel = new JPanel();
		rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.PAGE_AXIS));

		for (int i = 0; i < 15; i++)
			SHRulePanel.add(i, new ShowHideRulesPanel());
		
		for (int i = 0; i < 15; i++) {
			String rule = getString("Rules.rule" + String.valueOf(i+1));
			rulesPanel.add(SHRulePanel.get(i).AddJCB(i+1,rule));
			rulesPanel.add(SHRulePanel.get(i).AddRules(i+1,rule));
			rule=rule.replace("<html>", "").replace("</html>", "");
			rg.add(i, new RulesGUI(ttgs,rule));
		}
		JScrollPane tmp = new JScrollPane(rulesPanel);
		JPanel temp = new JPanel();
		temp.add(new JLabel(getString("Rules.description")));
		mainRulesPanel.add(temp,BorderLayout.NORTH);
		mainRulesPanel.add(tmp,BorderLayout.CENTER);
	}

	public JPanel getRulesPanel() {
		return mainRulesPanel;
	}
	
	class ShowHideRulesPanel {
		JPanel p, p1, p2;
		JCheckBox jcbRule;

		public ShowHideRulesPanel() {
			p = new JPanel();
			p1 = new JPanel();
			p2 = new JPanel();
		}

		public JPanel AddJCB(int i,String rule) {
			final int ci =i-1;
			jcbRule = new JCheckBox(rule);
			p.setLayout(new GridLayout());
			p.add(jcbRule, BorderLayout.LINE_START);
			jcbRule.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (jcbRule.isSelected())
						SHOWP(ci);
					else
						HIDEP(ci);
				}
			});
			if(i>2 && i<11)
				jcbRule.setToolTipText("<html>Default Hard & Soft constraints</html>");
			if(i>10 && i<14)
				jcbRule.setToolTipText("<html>Optional constraints</html>");
			if(i>13 && i<16)
				jcbRule.setToolTipText("<html>Default Soft constraints</html>");
			
			return p;
		}

		public JPanel AddRules(int i,String rule) {
			p1.setLayout(new BorderLayout());
			p2.setLayout(new BorderLayout());
			p2.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
			p2.add(new JLabel(rule), BorderLayout.LINE_START);
			return p1;
		}

		public void SHOWP(int i) {
			p2.removeAll();
			p2.add(rg.get(i).getPanel(i), BorderLayout.CENTER);
			p2.updateUI();
			p1.add(p2);
			p1.updateUI();
		}

		public void HIDEP(int i) {
			p1.remove(p2);
			p1.updateUI();
		}
	}

}
