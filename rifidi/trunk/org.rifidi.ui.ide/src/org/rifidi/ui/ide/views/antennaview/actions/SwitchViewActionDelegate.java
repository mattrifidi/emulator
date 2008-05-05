package org.rifidi.ui.ide.views.antennaview.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.rifidi.ui.ide.views.antennaview.AntennaView;

/**
 * Switch from the "Antenna" representation to the "GPIO" representation and
 * vice versa
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class SwitchViewActionDelegate implements IViewActionDelegate {

	/*
	 * The view associated with this button
	 */ 
	private AntennaView antennaView;
	/*
	 * Status variable to keep track in which view we are currently
	 */
	private boolean isAntennaView = true;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		antennaView = (AntennaView) view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (isAntennaView) {
			action.setToolTipText("Antenna View");
			isAntennaView = false;
		} else {
			isAntennaView = true;
			action.setToolTipText("GPI/O View");
		}
		antennaView.toggle();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
