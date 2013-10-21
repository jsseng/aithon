package aithon.shell;
/**
 * @author Aithon Development Team
 * class AithonShell
 * Embeds an interactive Aithon session into the console
 */
//{{{ Imports
import console.Console;
import console.Output;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.text.AttributeSet;
import javax.swing.JPanel;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.ServiceManager;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DockableWindowManager;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.TextArea;
import org.gjt.sp.util.Log;
import procshell.ProcessShell;
import aithon.shell.AithonTools;
//}}}
public class AithonShell extends ProcessShell {
  private static AithonShell a = null;
  private AithonTools AithonWindow;
	
	private String prompt = ">>> ";
	
	/*
 	 * Constructor for AithonShell
 	 */
	public AithonShell() {
		super("Aithon");

    //System.out.println("Creating AithonShell");
    //from QuickNotepad
    //q = new QuickNotepad(,DockableWindowManager.FLOATING);
  }

  //{{{ init()
  /**
   * Start up Aithon
   */
	protected void init(ConsoleState state, String command) throws IOException {
		String exec = jEdit.getProperty("options.aithon-shell.exec");
		String exec_dir = jEdit.getProperty("options.aithon-shell.exec-dir");
		String exec_class = jEdit.getProperty("options.aithon-shell.exec-class");
    //String exec_command = exec + " -cp " + exec_dir + " " + exec_class;
    String exec_command = exec;
		Log.log(Log.DEBUG,this,"Attempting to start Aithon process: "+exec);
		//ProcessBuilder pb = new ProcessBuilder(exec, "-cp " + exec_dir + " " + exec_class);
		ProcessBuilder pb = new ProcessBuilder(exec_command, "");
		state.p = pb.start();
		Log.log(Log.DEBUG,this,"Aithon started.");
	}
	//}}}
	
	//{{{ eval()
	/**
	 * Evaluate text
	 */
	public void eval(Console console, String str) {
		send(console, str);
	} //}}}
	
	//{{{ evalBuffer()
	/**
	 * Evaluate a buffer
	 */
	public void evalBuffer(Console console, Buffer buffer) {
		send(console, "execfile(\""+buffer.getPath().replace("\\", "/")+"\")");
	} //}}}
	
	protected void onRead(ConsoleState state, String str, Output output) {
		if (str.indexOf("\n") != -1) {
			str = str.substring(str.lastIndexOf("\n")+1);
		}
		if (str.matches(prompt)) {
			state.waiting = false;
			output.commandDone();
		}
	}

	public void printInfoMessage(Output output) {
		output.print(null, jEdit.getProperty("msg.aithon-shell.info-message"));
	}
	
  public JPanel showWindow(View view) {
    if (AithonWindow == null)
      AithonWindow = new AithonTools(view);
    AithonWindow.setVisible(true);
    return AithonWindow;
  }

}
