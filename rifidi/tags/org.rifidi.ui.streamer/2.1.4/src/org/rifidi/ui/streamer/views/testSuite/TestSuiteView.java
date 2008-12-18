/**
 * 
 */
package org.rifidi.ui.streamer.views.testSuite;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.emulator.rmi.server.RifidiManager;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.LoadTestSuite;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.views.batch.BatchView;
import org.rifidi.ui.streamer.views.components.ComponentView;
import org.rifidi.ui.streamer.views.scenario.ScenarioView;
import org.rifidi.ui.streamer.views.testUnit.TestUnitView;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@SuppressWarnings("deprecation")
public class TestSuiteView extends ViewPart {

	public static final String ID = "org.rifidi.ui.streamer.views.TestSuiteView";

	private Log logger = LogFactory.getLog(TestSuiteView.class);

	private Composite composite;
	private TreeViewer treeViewer;

	/**
	 * 
	 */
	public TestSuiteView() {

		String hostname = "127.0.0.1";
		int port = 1198;

		if (!RifidiManager.isStarted()) {
			// RMI ReaderManagement startup and connection management
			try {
				// TODO RifidiManager is the only reason for having additional
				// dependency to emulator
				RifidiManager.startManager(hostname, port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		composite = new Composite(parent, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		composite.setLayout(fillLayout);

		treeViewer = new TreeViewer(composite);
		treeViewer.setContentProvider(new TestSuiteContentProvider());
		treeViewer.setLabelProvider(new TestSuiteLabelProvider());
		treeViewer.setAutoExpandLevel(1);

		treeViewer.getControl().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// logger.debug("Pressed Key is " + e.keyCode + " " +
				// e.character);
				if (e.character == SWT.DEL) {
					IHandlerService service = (IHandlerService) getSite()
							.getWorkbenchWindow().getWorkbench().getAdapter(
									IHandlerService.class);
					try {
						service.executeCommand(
								"org.rifidi.ui.streamer.commands.RemoveObject",
								null);
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotDefinedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotEnabledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotHandledException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.keyCode == SWT.ARROW_RIGHT) {
					treeViewer.expandToLevel(getSelectedObject(), 1);
				}
				if (e.keyCode == SWT.ARROW_LEFT) {
					treeViewer.collapseToLevel(getSelectedObject(), 1);
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		// Register DoubleClick Listener for opening new Views for Batch,
		// Scenario, TestUnit and Components
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				// logger.debug(selection.toList().get(0).getClass()
				// .getSimpleName());
				Object o = selection.getFirstElement();
				try {
					if (o instanceof Batch) {
						Batch batch = (Batch) o;
						logger.debug("Displaying BatchView");
						BatchView bacthView = (BatchView) PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(BatchView.ID,
										"Batch" + batch.getID(),
										IWorkbenchPage.VIEW_ACTIVATE);
						bacthView.setBatch(batch);
					}
					if (o instanceof Scenario) {
						Scenario scenario = (Scenario) o;
						logger.debug("Displaying ScenarioView");
						ScenarioView scenarioView = (ScenarioView) PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(ScenarioView.ID,
										"Scenario" + scenario.getID(),
										IWorkbenchPage.VIEW_ACTIVATE);
						scenarioView.setScenario(scenario);
					}
					if (o instanceof TestUnit) {
						TestUnit testUnit = (TestUnit) o;
						TestUnitView testUnitView = (TestUnitView) PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(TestUnitView.ID,
										"TestUnit" + testUnit.getID(),
										IWorkbenchPage.VIEW_ACTIVATE);
						testUnitView.setTestUnit(testUnit);
					}
					if (o instanceof ReaderComponent) {
						ReaderComponent reader = (ReaderComponent) o;
						ComponentView componentView = (ComponentView) PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(ComponentView.ID,
										"Component" + reader.getID(),
										IWorkbenchPage.VIEW_ACTIVATE);
						componentView.setComponent(reader);
					}
					if (o instanceof BatchSuite) {
						treeViewer.expandToLevel(o, 1);
					}
					if (o instanceof ScenarioSuite) {
						treeViewer.expandToLevel(o, 1);
					}
					if (o instanceof ComponentSuite) {
						treeViewer.expandToLevel(o, 1);
					}
					if (o instanceof LoadTestSuite) {
						treeViewer.expandToLevel(o, 1);
					}
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}

		});

		InputObjectRegistry inputObjectRegistry = new InputObjectRegistry();
		treeViewer.setInput(inputObjectRegistry);

		getSite().setSelectionProvider(treeViewer);
	}

	/**
	 * @return
	 */
	public Object getSelectedObject() {
		return ((IStructuredSelection) treeViewer.getSelection())
				.getFirstElement();
	}

	/**
	 * @return
	 */
	public TreeViewer getTreeView() {
		return treeViewer;
	}

	/**
	 * @param loadTestSuite
	 */
	public void setInputObjectRegistry(InputObjectRegistry inputObjectRegistry) {
		treeViewer.setInput(inputObjectRegistry);
	}

	/**
	 * @return
	 */
	public InputObjectRegistry getInputObjectRegistry() {
		return (InputObjectRegistry) treeViewer.getInput();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
