import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class GridTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GridTester();
	}

	private Display display;
	private Shell shell;

	public GridTester() {
		display = new Display();
		shell = new Shell(display);

		shell.setText("TestShell");
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		Table table = new Table(shell, SWT.BORDER);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		shell.addListener(SWT.Move, new Listener() {

			@Override
			public void handleEvent(Event event) {
				System.out.println(shell.getBounds());
			}
		});

		shell.setBounds(150, 150, 300, 400);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
