// {{{ imports
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.gui.DefaultFocusComponent;
import org.gjt.sp.jedit.gui.DockableWindowManager;
import org.gjt.sp.jedit.msg.PropertiesChanged;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.StandardUtilities;
// }}}

// {{{ QuickNotePad class
/**
 * 
 * QuickNotePad - a dockable JPanel, a demonstration of a jEdit plugin.
 *
 */
public class Aithon extends JPanel
    implements EBComponent, AithonActions, DefaultFocusComponent {

    // {{{ Instance Variables
	private static final long serialVersionUID = 6412255692894321789L;
	private String filename;
	private String defaultFilename;
	private View view;
	private boolean floating;

  private AithonToolPanel toolPanel;
  private JButton detectButton;
  private JButton uploadButton;
  private JButton compileButton;
  private JTextArea c;
    // }}}

    // {{{ Constructor
	/**
	 * 
	 * @param view the current jedit window
	 * @param position a variable passed in from the script in actions.xml,
	 * 	which can be DockableWindowManager.FLOATING, TOP, BOTTOM, LEFT, RIGHT, etc.
	 * 	see @ref DockableWindowManager for possible values.
	 */
	public Aithon(View view, String position) {
		//super(new BorderLayout());
		super(new FlowLayout());
		this.view = view;
		this.floating = position.equals(DockableWindowManager.FLOATING);

		//this.toolPanel = new AithonToolPanel(this);
		//add(BorderLayout.NORTH, this.toolPanel);

		if (floating)
			this.setPreferredSize(new Dimension(500, 250));

		//add(BorderLayout.CENTER, pane);
    JToolBar buttons = new JToolBar();
    detectButton = new JButton("Detect\nBoard");
    detectButton.setText("<html><center>"+"Detect"+"<br>"+"Board"+"</center></html>");
    compileButton = new JButton("Compile");
    compileButton.setText("<html><center>"+"Compile"+"<br>"+"Code"+"</center></html>");
    uploadButton = new JButton("Upload");
    uploadButton.setText("<html><center>"+"Upload"+"<br>"+"HEX File"+"</center></html>");
    buttons.add(detectButton);
    buttons.add(compileButton);
    buttons.add(uploadButton);

    add(buttons, BorderLayout.WEST);

    JTextArea c = new JTextArea("This is a test sentence. jdkslfdjalk klfd; asjfkdla ;fjdksla; fdjkslaf d;sal dfkj");

    JScrollPane scroll = new JScrollPane (c, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    Color color=new Color(0,0,0);
    c.setBackground(color);

    Color color2=new Color(180,180,180);
    c.setForeground(color2);

    add(scroll, BorderLayout.EAST);
	}
    // }}}

    // {{{ Member Functions
    
    // {{{ focusOnDefaultComponent
	public void focusOnDefaultComponent() {
	}
    // }}}

    // {{{ getFileName
	public String getFilename() {
		return filename;
	}
    // }}}

	// EBComponent implementation
	
    // {{{ handleMessage
	public void handleMessage(EBMessage message) {
		if (message instanceof PropertiesChanged) {
			propertiesChanged();
		}
	}
    // }}}
    
    // {{{ propertiesChanged
	private void propertiesChanged() {
		String propertyFilename = jEdit
				.getProperty(AithonPlugin.OPTION_PREFIX + "filepath");
		if (!StandardUtilities.objectsEqual(defaultFilename, propertyFilename)) {
			saveFile();
			toolPanel.propertiesChanged();
			defaultFilename = propertyFilename;
			filename = defaultFilename;
		}
		Font newFont = AithonOptionPane.makeFont();
		//if (!newFont.equals(textArea.getFont())) {
		//	textArea.setFont(newFont);
		//}
	}
    // }}}

	// These JComponent methods provide the appropriate points
	// to subscribe and unsubscribe this object to the EditBus.

    // {{{ addNotify
	public void addNotify() {
		super.addNotify();
		EditBus.addToBus(this);
	}
     // }}}
     
    // {{{ removeNotify
	public void removeNotify() {
		saveFile();
		super.removeNotify();
		EditBus.removeFromBus(this);
	}
    // }}}
    
	// QuickNotepadActions implementation

    // {{{
	public void saveFile() {
		if (filename == null || filename.length() == 0)
			return;
		try {
			FileWriter out = new FileWriter(filename);
			//out.write(textArea.getText());
			out.close();
		} catch (IOException ioe) {
			Log.log(Log.ERROR, Aithon.class,
					"Could not write notepad text to " + filename);
		}
	}
    // }}}
    
    // {{{ chooseFile
	public void chooseFile() {
		String[] paths = GUIUtilities.showVFSFileDialog(view, null,
				JFileChooser.OPEN_DIALOG, false);
		if (paths != null && !paths[0].equals(filename)) {
			saveFile();
			filename = paths[0];
			toolPanel.propertiesChanged();
		}
	}
    // }}}

    // {{{ copyToBuffer
	public void copyToBuffer() {
		jEdit.newFile(view);
	}
    // }}}
    // }}}
}
// }}}
