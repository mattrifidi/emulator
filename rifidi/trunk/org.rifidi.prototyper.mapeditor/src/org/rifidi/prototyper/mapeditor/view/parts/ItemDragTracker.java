/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * We need to provide a custom DragEditPartsTracker that checks to see if we are
 * moving one item on top of another.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemDragTracker extends DragEditPartsTracker {

	/**
	 * @param sourceEditPart
	 */
	public ItemDragTracker(EditPart sourceEditPart) {
		super(sourceEditPart);
	}

	/*
	 * Override this method so that we can check to see if we are moving an item
	 * onto another item. This will cause an ORPHAN_CHILDREN_REQ to be issued
	 * instead of a MOVE_REQ.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#updateTargetUnderMouse()
	 */
	@Override
	protected boolean updateTargetUnderMouse() {
		if (!isTargetLocked()) {
			EditPart editPart = getCurrentViewer()
					.findObjectAtExcluding(getLocation(), getExclusionSet(),
							getTargetingConditional());
			// This if block checks to see if we are moving an item onto another
			// one.
			if (editPart instanceof ItemLayerPart) {
				ItemLayerPart itemLayer = (ItemLayerPart) editPart;
				ItemPart item = itemLayer.getItemPartAt(getLocation());
				if (item != null && item != getSourceEditPart()) {
					editPart = item;
				}
			}
			if (editPart != null)
				editPart = editPart.getTargetEditPart(getTargetRequest());
			boolean changed = getTargetEditPart() != editPart;
			setTargetEditPart(editPart);
			return changed;
		} else
			return false;
	}

}
