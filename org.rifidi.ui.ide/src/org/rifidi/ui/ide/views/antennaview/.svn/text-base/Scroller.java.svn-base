package org.rifidi.ui.ide.views.antennaview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * This class provides the functionality necessary for scrolling a Composite by
 * encasing it in a Composite with a scrollbar
 * 
 * @author Dan West - dan@pramari.com
 */
public class Scroller extends Composite { 
	/**
	 * The composite containing the content of the scroller
	 */
	private Composite widget = null;

	/**
	 * Create scroller composite with a functional scroll bar and no content
	 * 
	 * @param parent
	 *            Composite parent of the scroller
	 * @param style
	 *            Flags specifying the style of the scroller
	 */
	public Scroller(Composite parent, Integer style) {
		super(parent, style); // definately scrollable

		// Listener event for sliding the scroller
		this.getHorizontalBar().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Point location = widget.getLocation();
				location.x = -getHorizontalBar().getSelection();
				widget.setLocation(location);
			}
		});

		// Listener event for resizing the scroller
		this.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				Point size = widget.getSize();
				Rectangle rect = getClientArea();
				getHorizontalBar().setMaximum(size.x);
				getHorizontalBar().setThumb(Math.min(size.x, rect.width));
				int vPage = size.x - rect.width;
				int vSelection = getHorizontalBar().getSelection();
				Point location = widget.getLocation();
				if (vSelection >= vPage) {
					if (vPage <= 0) {
						vSelection = 0;
					}
					location.x = -vSelection;
				}
				widget.setLocation(location);
			}
		});
	}

	/**
	 * Set the composite containing the content of the scroller
	 * 
	 * @param widget
	 *            The widget containing the content of the scroller
	 */
	public void setWidget(Composite widget) {
		if (this.widget == null && widget != null) {
			this.widget = widget;
		}
	}

	/**
	 * @return The content of the scroller
	 */
	public Composite getWidget() {
		return widget;
	}
}