package org.rifidi.ui.streamer.executeDialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.rifidi.streamer.exceptions.NotInitializedException;
import org.rifidi.streamer.executers.TestUnitExecuter;
import org.rifidi.streamer.executers.listener.TestUnitStateListener;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.Activator;

public class ExecuteDialog implements TestUnitStateListener {

	// /private Log logger = LogFactory.getLog(ExecuteDialog.class);

	private Shell parentShell;
	private TestUnit testUnit;
	private Shell shell;
	private ProgressBar progressBar;
	private TestUnitExecuter testUnitExecuter;
	private TrayItem trayItem;
	private Button buttonCancel;
	private Button buttonMinimizeTray;
	private boolean testUnitExecuting;

	public ExecuteDialog(Shell parentShell, TestUnit testUnit,
			InputObjectRegistry inputObjectRegistry) {
		this.testUnit = testUnit;
		this.parentShell = parentShell;

		// Initialize the TestUnitExecuter
		testUnitExecuter = new TestUnitExecuter(testUnit, this,
				inputObjectRegistry);
		testUnitExecuter.createScenarios();

		// scenarioList = testUnitExecuter.getScenarioExecuters();
	}

	public void open() {
		shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Progress Monitor");
		shell.setLayout(new FillLayout());
		// System.out.println(shell.getSize().toString());

		Composite composite = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label dialogLabel = new Label(composite, SWT.NONE);
		GridData dialogLabelGridData = new GridData();
		dialogLabelGridData.horizontalSpan = 2;
		dialogLabel.setLayoutData(dialogLabelGridData);
		if (testUnit != null)
			dialogLabel.setText("Executing TestUnit #" + testUnit.getID());

		Label testUnitLabel = new Label(composite, SWT.NONE);
		testUnitLabel.setText("Progress :");

		progressBar = new ProgressBar(composite, SWT.SMOOTH);
		progressBar.setMaximum(testUnit.getIterations());
		progressBar.setToolTipText("Number of Iterations defined in the XML :" + testUnit.getIterations());

		TableViewer tableViewer = new TableViewer(composite, SWT.NONE);
		GridData gridDataTableViewer = new GridData();
		gridDataTableViewer.horizontalSpan = 2;
		gridDataTableViewer.horizontalAlignment = SWT.FILL;
		gridDataTableViewer.heightHint = 100;
		// gridDataTableViewer.grabExcessVerticalSpace = true;
		tableViewer.getTable().setLayoutData(gridDataTableViewer);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);

		String[] columnNames = new String[] { "Scenario", "status" };
		int[] columnWidths = new int[] { 100, 100 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };
		List<TableColumn> tableColumns = new ArrayList<TableColumn>();
		// create columns and add listeners to them
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(tableViewer.getTable(),
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
			// Add Listener
			// tableColumn.addListener(SWT.Selection, this.selChangeListener);
			tableColumns.add(tableColumn);
		}
		tableViewer.setContentProvider(new ExecuteDialogContentProvider());
		tableViewer.setLabelProvider(new ExecuteDialogLabelProvider());
		tableViewer.setInput(testUnitExecuter);

		buttonMinimizeTray = new Button(composite, SWT.PUSH);
		buttonMinimizeTray.setText("minimize to Tray");
		buttonMinimizeTray.addMouseListener(new MouseListener() {

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
				trayIcon();
				hideShell();
			}
		});

		buttonCancel = new Button(composite, SWT.PUSH);
		buttonCancel.setText("Cancel");
		GridData buttonCancelGridData = new GridData();
		buttonCancelGridData.horizontalAlignment = SWT.RIGHT;
		buttonCancel.setLayoutData(buttonCancelGridData);
		buttonCancel.addMouseListener(new MouseListener() {

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
				if (testUnitExecuting)
				{
					testUnitExecuter.stop();
					buttonCancel.setEnabled(false);
				}
				else
					shell.dispose();
			}
		});

		shell.pack();
		shell.open();
		
		// Move the Dialog in the center of the parent shell
		Rectangle parentSize = parentShell.getBounds();
		Rectangle mySize = shell.getBounds();
		int locationX, locationY;
		locationX = (parentSize.width - mySize.width)/2+parentSize.x;
		locationY = (parentSize.height - mySize.height)/2+parentSize.y;
		shell.setLocation(new Point(locationX, locationY));

		startTestUnit();
	}

	private void trayIcon() {
		if (trayItem == null) {
			Tray tray = shell.getDisplay().getSystemTray();
			if (tray == null) {
				MessageDialog.openError(shell, "System Tray",
						"This System is not supporting trays");
				buttonMinimizeTray.setEnabled(false);
			} else {

				trayItem = new TrayItem(tray, SWT.NONE);
				trayItem.setToolTipText("Rifidi TagStreamer 2.0");
				Image image = Activator.getImageDescriptor(
						"/icons/title_rifidi_16x16.gif").createImage();
				trayItem.setImage(image);
				trayItem.addListener(SWT.DefaultSelection, new Listener() {
					public void handleEvent(Event event) {
						if (!parentShell.isVisible() || !shell.isVisible()) {
							showShell();
							trayItem.setVisible(false);
						}
					}
				});
			}
		} else {
			trayItem.setVisible(true);
		}
	}

	private void showShell() {
		parentShell.setVisible(true);
		parentShell.setMinimized(false);
		shell.setVisible(true);
		shell.setMinimized(false);
	}

	private void hideShell() {
		shell.setVisible(false);
		shell.setMinimized(true);
		parentShell.setVisible(false);
		parentShell.setMinimized(true);
	}

	private void startTestUnit() {
		testUnitExecuter.addIterationListener(this);
		testUnitExecuting = true;
		try {
			testUnitExecuter.start();
		} catch (NotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void nextIteration(final int iteration) {
		if (!shell.isDisposed())
			shell.getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (progressBar.isDisposed())
						return;
					progressBar.setSelection(iteration);
				}
			});
	}

	@Override
	public void testUnitFinishedEvent() {
		// RifidiManager.stopManager();
		if (!shell.isDisposed()) {
			shell.getDisplay().syncExec(new Runnable() {
				public void run() {
					if (!buttonCancel.isDisposed()) {
						testUnitExecuting = false;
						buttonCancel.setEnabled(true);
						buttonCancel.setText("OK");
					}
				}
			});
		}
	}

}
