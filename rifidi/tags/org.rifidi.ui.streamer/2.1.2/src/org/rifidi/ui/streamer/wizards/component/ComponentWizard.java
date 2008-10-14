package org.rifidi.ui.streamer.wizards.component;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.wizards.reader.pages.NewReaderDynamicWizardPage;
import org.rifidi.ui.common.wizards.reader.pages.NewReaderGPIOWizardPage;

import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class ComponentWizard extends Wizard implements INewWizard {

	private NewReaderWizardStreamerPage newReaderWizardStreamerPage;
	private NewReaderDynamicWizardPage newReaderDynamicWizardPage;
	private NewReaderGPIOWizardPage newReaderGPIOWizardPage;

	private ReaderComponent component;
	private UIReader reader;
	private InputObjectRegistry registry;
	private IWorkbench workbench;

	public ComponentWizard() {
		super();
		initializeComponent();
	}

	public void initializeComponent() {
		if (component == null) {
			component = new ReaderComponent();
			component.setReader(new GeneralReaderPropertyHolder());
			reader = component.getUIReader();
		}
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
				"readerSelectionPage", component, ReaderRegistry.getInstance()
						.getReaderBlueprints());
		addPage(newReaderWizardStreamerPage);

		newReaderDynamicWizardPage = new NewReaderDynamicWizardPage(
				"readerCreationPage", reader, ReaderRegistry.getInstance()
						.getReaderBlueprints());
		addPage(newReaderDynamicWizardPage);

		newReaderGPIOWizardPage = new NewReaderGPIOWizardPage("gpioPage",
				reader, ReaderRegistry.getInstance().getReaderBlueprints());
		addPage(newReaderGPIOWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer) {
		newReaderWizardStreamerPage.createControl(pageContainer);
	}

}
