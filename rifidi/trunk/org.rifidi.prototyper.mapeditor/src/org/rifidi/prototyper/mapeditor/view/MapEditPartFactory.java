/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.FloorplanElement;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;
import org.rifidi.prototyper.mapeditor.model.ItemElement;
import org.rifidi.prototyper.mapeditor.model.MapModel;
import org.rifidi.prototyper.mapeditor.view.parts.FloorplanPart;
import org.rifidi.prototyper.mapeditor.view.parts.HotspotLayerPart;
import org.rifidi.prototyper.mapeditor.view.parts.HotspotPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemLayerPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;
import org.rifidi.prototyper.mapeditor.view.parts.MapPart;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		System.out.println("CREATE PART FOR " + model);
		EditPart part = null;
		if (model instanceof MapModel) {
			part = new MapPart();
		} else if (model instanceof FloorplanElement) {
			part = new FloorplanPart();
		} else if (model instanceof ElementSet<?>) {
			ElementSet<?> set = (ElementSet<?>) model;
			if (set.getType().equals(ItemElement.class)) {
				part = new ItemLayerPart();
			} else if (set.getType().equals(HotspotElement.class)) {
				part = new HotspotLayerPart();
			}
		} else if (model instanceof ItemElement) {
			part = new ItemPart();
		} else if (model instanceof HotspotElement) {
			part = new HotspotPart();
		}
		part.setModel(model);
		return part;
	}

}
