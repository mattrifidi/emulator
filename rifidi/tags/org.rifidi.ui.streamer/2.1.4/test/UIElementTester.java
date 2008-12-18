import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.ui.streamer.composites.ScenarioComposite;

public class UIElementTester {

	private static Composite composite;
	private static Composite replacement;
	private static Shell shell;
	private static boolean show = false;
	private static Display display;

	public static void main(String[] args) {
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

	public static void initComposite(Composite parent) {
		composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout());
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Replace SWT Composite");
		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				replaceComposite();
			}

		});
	}

	private static void replaceComposite() {
		if (replacement != null) {
			replacement.dispose();
		}
		if (show == false) {
			replacement = new ScenarioComposite(composite, SWT.NONE, true);
			show = true;
		} else {
			// replacement = new WaitActionComposite(composite, SWT.NONE);
			show = false;
		}
		composite.layout();
	}
}
