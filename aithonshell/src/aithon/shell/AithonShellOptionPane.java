package aithon.shell;
/**
 * @author Aithon Development Team
 * class AithonShellOptionPane
 * Option pane under 'Plugins->Options'
 */
//{{{ Imports
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.browser.VFSFileChooserDialog;
import org.gjt.sp.jedit.jEdit;
//}}}
public class AithonShellOptionPane extends AbstractOptionPane {
	private JTextField exec;
	public AithonShellOptionPane() {
		super("aithon-shell");
	}

	protected void _init() {
		exec = new JTextField(
			jEdit.getProperty("options.aithon-shell.exec"));
		JButton browse = new JButton(
			jEdit.getProperty("vfs.browser.browse.label"));
		browse.addActionListener(new BrowseHandler());

		JPanel comp = new JPanel();
		comp.setLayout(new BoxLayout(comp, BoxLayout.X_AXIS));
		comp.add(exec);
		comp.add(browse);

		addComponent(jEdit.getProperty("options.aithon-shell.textfield-label"),
			comp);
	}

	protected void _save() {
		jEdit.setProperty("options.aithon-shell.exec", exec.getText());
	}

	class BrowseHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			VFSFileChooserDialog dialog = new VFSFileChooserDialog(
				jEdit.getActiveView(), System.getProperty("user.dir")+File.separator,
				VFSBrowser.OPEN_DIALOG, false, true);
			String[] files = dialog.getSelectedFiles();
			if (files != null) {
				exec.setText(files[0]);
			}
		}
	}

}
