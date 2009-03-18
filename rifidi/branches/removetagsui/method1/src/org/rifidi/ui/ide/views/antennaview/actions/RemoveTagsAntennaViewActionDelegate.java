package org.rifidi.ui.ide.views.antennaview.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RemoveTagsAntennaViewActionDelegate implements IViewActionDelegate {
	
	private Log logger = LogFactory.getLog(RemoveTagsAntennaViewActionDelegate.class);
	
	@Override
	public void init(IViewPart arg0) {
		// TODO Auto-generated method stub
		logger.debug("Remove tag delegate init.");
	}

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		logger.debug("Remove tag delegate run.");
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		logger.debug("Remove tag delegate selection changed.");
	}

}
