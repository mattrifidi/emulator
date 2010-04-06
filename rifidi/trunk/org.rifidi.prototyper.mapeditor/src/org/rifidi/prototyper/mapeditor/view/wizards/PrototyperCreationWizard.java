/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * A wizard for creating a new prototype.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class PrototyperCreationWizard extends Wizard implements INewWizard {

	private PrototyperCreationWizardPage1 page1;
	private PrototyperCreationWizardPage2 page2;
	private PrototyperCreationWizardPage3 finalPage;
	private IStructuredSelection selection;
	private IWorkbench workbench;
	protected boolean custom = false;
	protected String projectName = "Example Project";
	protected ProjectType exampleProject = ProjectType.WAREHOUSE;
	protected String pathToFile = "";
	protected Float feetPer30Px = new Float(1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page1 = new PrototyperCreationWizardPage1("Project Type");
		addPage(page1);
		page2 = new PrototyperCreationWizardPage2("Select Map");
		addPage(page2);
		finalPage = new PrototyperCreationWizardPage3(workbench, selection);
		addPage(finalPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.
	 * IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == page1 && !custom) {
			return finalPage;
		}
		return super.getNextPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return finalPage.finish();
	}

	public enum ProjectType {
		HOSPITAL {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Enum#toString()
			 */
			@Override
			public String toString() {
				return "Hospital";
			}

		},
		WAREHOUSE {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Enum#toString()
			 */
			@Override
			public String toString() {
				return "Warehouse";
			}

		};

		public static ProjectType getProjectType(String type) {
			if (type.equalsIgnoreCase(HOSPITAL.toString())) {
				return HOSPITAL;
			} else if (type.equalsIgnoreCase(WAREHOUSE.toString())) {
				return WAREHOUSE;
			}
			return null;
		}

	}

}
