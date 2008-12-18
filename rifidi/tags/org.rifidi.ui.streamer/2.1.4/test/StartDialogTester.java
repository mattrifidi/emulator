import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class StartDialogTester {

	public static void main(String[] args) {
		new StartDialogTester();
	}

	private Display display;
	private Shell shell;

	public StartDialogTester() {
		display = new Display();
		shell = new Shell();

		shell.setLayout(new FillLayout());
		shell.setText("TESTING TOOL");

		initComposite(shell);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	public void initComposite(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText("open Dialog");
		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent e) {
				// ExecuteDialog dialog = new ExecuteDialog(shell, null);
				// dialog.open();
			}
		});
	}

}
