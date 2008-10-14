package org.rifidi.ui.streamer.wizards.batch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.ui.streamer.composites.BatchComposite;
import org.rifidi.ui.streamer.data.BatchEventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class BatchWizardPage extends WizardPage {

	// private Log logger = LogFactory.getLog(BatchPage.class);
	private Batch batch;

	/**
	 * @param pageName
	 */
	protected BatchWizardPage(String pageName, Batch batch) {
		super(pageName);
		this.batch = batch;
		setMessage("Create a new Batch by setting at least the Batch ID");
		setTitle("New Batch Wizard");
		setPreviousPage(null);

		setPageComplete(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		BatchComposite batchComposite = new BatchComposite(
				parent, SWT.NONE, true);
		batchComposite.setBatch(new BatchEventAwareWrapper(
				batch));
		batchComposite.setLayoutData(GridData.FILL_BOTH);

		// This is essential otherwise the Page cannot be shown (Strange
		// Eclipse!!!)
		setControl(parent);
	}

}
