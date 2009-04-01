package org.rifidi.ui.ide.views.antennaview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.nebula.widgets.pgroup.PGroup;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.ui.common.reader.UIAntenna;
import org.rifidi.ui.common.reader.callback.UIReaderCallbackManager;

/**
 * Class to actually display the antenna and the tags in it.
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public class TagPGroup extends PGroup {

	// the viewer that holds the tags
	private TagViewer viewer;

	public TagPGroup(Composite parent, int style, UIAntenna antenna,
			UIReaderCallbackManager callbackManager) {
		super(parent, style);
		FillLayout layout = new FillLayout();
		// layout.type = SWT.HORIZONTAL;
		setLayout(layout);
		setText("Antenna " + antenna.getId());
		setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		viewer = new TagViewer(this, SWT.MULTI | SWT.FULL_SELECTION, antenna,
				callbackManager);
		
		/* WORKAROUND:
		 * This is a workaround for eclipse bug 270890
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=270890
		 */
		addExpandListener(new ExpandListener () {

			@Override
			public void itemCollapsed(ExpandEvent e) {
				TagPGroup.this.setExpanded(true);
			}

			@Override
			public void itemExpanded(ExpandEvent e) {
				TagPGroup.this.setExpanded(true);
			}
			
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		viewer.dispose();
	}

}
