// {{{ imports
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JToggleButton;
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

// {{{ Aithon_Plugin class
/**
 * 
 * Aithon Plugin
 *
 */
public class Aithon extends JPanel
implements ActionListener, EBComponent, AithonActions, 
           DefaultFocusComponent {

  // {{{ Instance Variables
  private String filename;
  private String defaultFilename;
  private View view;
  private boolean floating;

  private JButton detectButton;
  private JButton uploadButton;
  private JButton compileButton;
  private JToggleButton showSerialTerminal;
  private JTextArea console_area;
  private Runtime r;
  private Process p;
  private Thread t;
  private BufferedWriter out;
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
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.view = view;
    this.floating = position.equals(DockableWindowManager.FLOATING);

    if (floating)
      this.setPreferredSize(new Dimension(500, 250));

    JPanel buttons = new JPanel();
    buttons.setLayout(new FlowLayout());
    buttons.setPreferredSize(new Dimension(150,180));
    buttons.setMaximumSize(new Dimension(150, 180));
    detectButton = new JButton("Detect\nBoard");
    detectButton.setText("<html><center>"+"Detect"+"<br>"+"Board"+"</center></html>");
    detectButton.addActionListener(this);
    compileButton = new JButton("Compile");
    compileButton.setText("<html><center>"+"Compile"+"<br>"+"Code"+"</center></html>");
    compileButton.addActionListener(this);
    uploadButton = new JButton("Upload");
    uploadButton.setText("<html><center>"+"Upload"+"<br>"+"HEX File"+"</center></html>");
    uploadButton.addActionListener(this);
    showSerialTerminal = new JToggleButton("Serial Terminal");
    
    buttons.add(detectButton);
    buttons.add(compileButton);
    buttons.add(uploadButton);
    buttons.add(showSerialTerminal);

    add(buttons);

    console_area = new JTextArea("This is the console area"); //create the console area
    console_area.setLineWrap(true);
    console_area.setWrapStyleWord(true);

    JScrollPane scrollbars = new JScrollPane (console_area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    Color color=new Color(0,0,0); //set background to black
    console_area.setBackground(color);

    Color color2=new Color(180,180,180); //set foreground to gray
    console_area.setForeground(color2);

    add(scrollbars);

    //start console process
    String[] args = {"/usr/bin/python", "-i"};
    r = Runtime.getRuntime();
    try {
      p = r.exec(args);
      inputStreamToOutputStream(p.getInputStream());
      out = new BufferedWriter( new OutputStreamWriter(p.getOutputStream()) );
    } catch (IOException e) {
      System.err.println("Caught IOException: " + e.getMessage());
    }

  }

  void inputStreamToOutputStream(final InputStream inputStream) {
    t = new Thread(new Runnable() {
          public void run() {
            try {
              int d;
              while ((d = inputStream.read()) != -1) {
                console_area.append(Character.toString((char)d));
              }
            } catch (IOException e) {
              System.err.println("Caught IOException: " + e.getMessage());
            }
          }
        });
    t.setDaemon(true);
    t.start();
  }

  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    if (src == uploadButton) {
      try {
        out.write("print\"upload\"\n");
        out.flush();
        console_area.setCaretPosition (console_area.getDocument().getLength());
      } catch (IOException e) {
        System.err.println("Caught IOException: " + e.getMessage());
      }
    } else if (src == detectButton) {
      try {
        out.write("print\"detect\"\n");
        out.flush();
        console_area.setCaretPosition (console_area.getDocument().getLength());
      } catch (IOException e) {
        System.err.println("Caught IOException: " + e.getMessage());
      }
    } else if (src == compileButton) {
      try {
        out.write("print\"compile\"\n");
        out.flush();
        console_area.setCaretPosition (console_area.getDocument().getLength());
      } catch (IOException e) {
        System.err.println("Caught IOException: " + e.getMessage());
      }
    }
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
			//toolPanel.propertiesChanged();
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
			//toolPanel.propertiesChanged();
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
