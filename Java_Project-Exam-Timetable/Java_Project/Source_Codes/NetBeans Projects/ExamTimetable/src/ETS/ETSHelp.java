package ETS;

//ETSHelp.java	

//This Class is refer from the 
//MetalWorks application of java (Help.java)

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import javax.swing.text.*;
import javax.swing.event.*;

//Class : ETSHelp
//Use   : Displays Help

public class ETSHelp extends JDialog {

	public ETSHelp(JFrame f) {
		super(f, "Help : com.ETS	", true);
		setBounds(200, 100, 550, 400);
		HtmlPane html = new HtmlPane();
		setContentPane(html);
	}
}

class HtmlPane extends JScrollPane implements HyperlinkListener {
	JEditorPane html;

	public HtmlPane() {
		try {
			URL url = getClass().getResource("/resources/HelpFiles/index.html");
			html = new JEditorPane(url);
			html.setEditable(false);
			html.addHyperlinkListener(this);
			JViewport vp = getViewport();
			vp.add(html);
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}

	// Notification of a change relative to a
	// hyperlink.

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			linkActivated(e.getURL());
		}
	}

	// Follows the reference in an
	// link. The given url is the requested reference.
	// By default this calls <a href="#setPage">setPage</a>,
	// and if an exception is thrown the original previous
	// document is restored and a beep sounded. If an
	// attempt was made to follow a link, but it represented
	// a malformed url, this method will be called with a
	// null argument.

	// @param u the URL to follow

	protected void linkActivated(URL u) {
		Cursor c = html.getCursor();
		Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		html.setCursor(waitCursor);
		SwingUtilities.invokeLater(new PageLoader(u, c));
	}

	// temporary class that loads synchronously (although
	// later than the request so that a cursor change
	// can be done).

	class PageLoader implements Runnable {

		PageLoader(URL u, Cursor c) {
			url = u;
			cursor = c;
		}

		public void run() {
			if (url == null) {

				// restore the original cursor

				html.setCursor(cursor);

				// PENDING(prinz) remove this hack when
				// automatic validation is activated.

				Container parent = html.getParent();
				parent.repaint();
			} else {
				Document doc = html.getDocument();
				try {
					html.setPage(url);
				} catch (IOException ioe) {
					html.setDocument(doc);
					getToolkit().beep();
				} finally {

					// schedule the cursor to revert after
					// the paint has happended.

					url = null;
					SwingUtilities.invokeLater(this);
				}
			}
		}

		URL url;
		Cursor cursor;
	}

}
