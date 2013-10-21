package aithon.shell;

// {{{ imports
import console.Console;
import console.Output;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

// {{{ AithonTools class
/**
 * 
 * AithonTools - a dockable JPanel, a demonstration of a jEdit plugin.
 *
 */
public class AithonTools extends JPanel
    implements EBComponent, AithonToolsActions, DefaultFocusComponent,
    ActionListener {

	private String filename;
	private String defaultFilename;
	private View view;
	private boolean floating;
  private JButton detectButton;
  private JButton uploadButton;
  private JButton compileButton;

    // {{{ Constructor
	public AithonTools(View view) {
    this.view=view;
		this.floating = true;
    setLayout(new BorderLayout());

    JToolBar buttons = new JToolBar();
    detectButton = new JButton("Detect\nBoard"); 
    detectButton.setText("<html><center>"+"Detect"+"<br>"+"Board"+"</center></html>");
    detectButton.addActionListener(this);

    compileButton = new JButton("Compile");
    compileButton.setText("<html><center>"+"Compile"+"<br>"+"Code"+"</center></html>");
    compileButton.addActionListener(this);

    uploadButton = new JButton("Upload");
    uploadButton.setText("<html><center>"+"Upload"+"<br>"+"HEX File"+"</center></html>");
    uploadButton.addActionListener(this);

    buttons.add(detectButton);
    buttons.add(compileButton);
    buttons.add(uploadButton);


    add(buttons, BorderLayout.CENTER);

    JLabel l = new JLabel("test label");
    add(l, BorderLayout.NORTH);

    this.setPreferredSize(new Dimension(500, 250));
  }

	/**
	 * 
	 * @param view the current jedit window
	 * @param position a variable passed in from the script in actions.xml,
	 * 	which can be DockableWindowManager.FLOATING, TOP, BOTTOM, LEFT, RIGHT, etc.
	 * 	see @ref DockableWindowManager for possible values.
	 */
	public AithonTools(View view, String position) {
		super(new BorderLayout());
		this.view = view;
		this.floating = position.equals(DockableWindowManager.FLOATING);

		if (jEdit.getSettingsDirectory() != null) {
			this.filename = "";
			if (this.filename == null || this.filename.length() == 0) {
				this.filename = new String(jEdit.getSettingsDirectory()
						+ File.separator + "qn.txt");
			}
			this.defaultFilename = this.filename;
		}

		if (floating)
			this.setPreferredSize(new Dimension(500, 250));

		readFile();
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
		String propertyFilename = "";
		if (!StandardUtilities.objectsEqual(defaultFilename, propertyFilename)) {
			saveFile();
			//toolPanel.propertiesChanged();
			defaultFilename = propertyFilename;
			filename = defaultFilename;
			readFile();
		}
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
    
  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    if (src == detectButton) {
      JComponent console = view.getDockableWindowManager().getDockableWindow("console");
    } else if (src == uploadButton) {
    } else if (src == compileButton) {
    }
  }

  // QuickNotepadActions implementation

    // {{{
	public void saveFile() {
	}
    // }}}
    
    // {{{ chooseFile
	public void chooseFile() {
	}
    // }}}

    // {{{ copyToBuffer
	public void copyToBuffer() {
	}
    // }}}
    // {{{ readFile()
	/**
	 * Helper method
	 */
	private void readFile() {
	}
    // }}}
    // }}}
}
// }}}
