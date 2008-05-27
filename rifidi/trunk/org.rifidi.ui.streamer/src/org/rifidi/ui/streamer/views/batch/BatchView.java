/**
 * 
 */
package org.rifidi.ui.streamer.views.batch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.ui.streamer.composites.BatchComposite;
import org.rifidi.ui.streamer.data.BatchEventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class BatchView extends ViewPart {

	public static final String ID = "org.rifidi.ui.streamer.views.batch.BatchView";
	private BatchEventAwareWrapper batch;
	private BatchComposite batchComposite;

	/**
	 * 
	 */
	public BatchView() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		batchComposite = new BatchComposite(parent,
				SWT.NONE, false);
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

	public void setBatch(Batch batch) {
		// Update UI Elements
		this.batch = new BatchEventAwareWrapper(batch);
		updateUI();
	}

	private void updateUI() {
		if (batch != null) {
			int batchID = batch.getBatch().getID();
			setPartName("Batch " + batchID);
			batchComposite.setBatch(batch);
		}
	}

}
