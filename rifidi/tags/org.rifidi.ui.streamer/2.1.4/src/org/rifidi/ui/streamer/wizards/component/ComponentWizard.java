package org.rifidi.ui.streamer.wizards.component;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.wizards.reader.NewReaderDynamicWizardPage;
import org.rifidi.ui.common.wizards.reader.NewReaderGPIOWizardPage;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class ComponentWizard extends Wizard implements INewWizard {

	private NewReaderWizardStreamerPage newReaderWizardStreamerPage;
	private NewReaderDynamicWizardPage newReaderDynamicWizardPage;
	private NewReaderGPIOWizardPage newReaderGPIOWizardPage;

	private InputObjectRegistry registry;
	private IWorkbench workbench;
	private StreamerWizardData data;

	public ComponentWizard() {
		super();
		initializeComponent();
	}

	public void initializeComponent() {
		data = new StreamerWizardData();
		setWindowTitle("New Component Wizard");
	}

	@Override
	public boolean canFinish() {
		if (!newReaderDynamicWizardPage.enableGPIO()
				&& newReaderDynamicWizardPage.isPageComplete())
			return true;
		if (newReaderDynamicWizardPage.isPageComplete()
				&& newReaderGPIOWizardPage.isPageComplete())
			return true;
		return false;
	}

	@Override
	public boolean performFinish() {
		if (registry != null) {
			ReaderComponent component = new ReaderComponent();
			component.setReader(data.getGeneralReaderHolder());
			component.setID(data.getID());
			try {
				registry.registerComponent(component);
			} catch (DublicateObjectException e) {
				MessageDialog
						.openError(
								workbench.getActiveWorkbenchWindow().getShell(),
								"Error while creating Component!",
								"Couldn't create Component with ID "
										+ component.getID()
										+ " because there is already a Component with this ID.");
				return false;
			}
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		registry = ((TestSuiteView) workbench.getActiveWorkbenchWindow()
				.getActivePage().findView(TestSuiteView.ID))
				.getInputObjectRegistry();

	}

	@Override
	public void addPages() {

		if (ReaderRegistry.getInstance() == null)
			System.out.println("ReaderRegistry");
		if (ReaderRegistry.getInstance().getReaderBlueprints() == null)
			System.out.println("Blueprints");

		newReaderWizardStreamerPage = new NewReaderWizardStreamerPage(
				"readerSelectionPage", data, ReaderRegistry.getInstance()
						.getReaderBlueprints());
		addPage(newReaderWizardStreamerPage);

		newReaderDynamicWizardPage = new NewReaderDynamicWizardPage(
				"readerCreationPage", data, ReaderRegistry.getInstance()
						.getReaderBlueprints());
		addPage(newReaderDynamicWizardPage);

		newReaderGPIOWizardPage = new NewReaderGPIOWizardPage("gpioPage", data,
				ReaderRegistry.getInstance().getReaderBlueprints());
		addPage(newReaderGPIOWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer) {
		newReaderWizardStreamerPage.createControl(pageContainer);
	}

}
