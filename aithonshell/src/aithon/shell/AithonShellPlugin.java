package aithon.shell;
/**
 * @author Damien Radtke
 * class AithonReplPlugin
 * Main class for the AithonShell plugin
 */
//{{{ Imports
import console.Shell;
import org.gjt.sp.jedit.EditPlugin;
//}}}
public class AithonShellPlugin extends EditPlugin {
  public static final String NAME = "aithonshell";
  public static final String OPTION_PREFIX = "options.aithon-shell.";

	public void start() {}
	public void stop() {
		try {
			AithonShell shell = (AithonShell) Shell.getShell("Aithon");
			shell.stop();
		} catch (Exception e) {}
	}
}
