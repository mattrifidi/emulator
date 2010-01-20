/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableLayeredPane;
import org.eclipse.gef.EditPolicy;
import org.rifidi.prototyper.mapeditor.model.MapModel;
import org.rifidi.prototyper.mapeditor.view.parts.policies.DNDDropPolicy;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapPart extends AbstractMapPart<MapModel> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		ScalableLayeredPane mapPane = new ScalableLayeredPane();
		return mapPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List getModelChildren() {
		List<Object> list = new ArrayList<Object>();
		MapModel model = getModelElement();
		list.add(model.getFloorplan());
		list.add(model.getHotspots());
		list.add(model.getItems());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#createEditPolicies
	 * ()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new DNDDropPolicy());
	}

	public HotspotLayerPart getHotspotLayerPart() {
		for (Object o : getChildren()) {
			if (o instanceof HotspotLayerPart) {
				return (HotspotLayerPart) o;
			}
		}
		return null;
	}
	
	public ItemLayerPart getItemLayerPart() {
		for (Object o : getChildren()) {
			if (o instanceof ItemLayerPart) {
				return (ItemLayerPart) o;
			}
		}
		return null;
	}
}
