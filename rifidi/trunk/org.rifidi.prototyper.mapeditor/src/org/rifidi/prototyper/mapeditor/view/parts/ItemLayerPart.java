/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.ItemElement;
import org.rifidi.prototyper.mapeditor.view.figures.MapLayer;
import org.rifidi.prototyper.mapeditor.view.parts.policies.ItemLayerLayoutPolicy;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemLayerPart extends AbstractMapPart<ElementSet<ItemElement>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Layer itemLayer = new MapLayer();
		itemLayer.setLayoutManager(new XYLayout());
		return itemLayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ItemLayerLayoutPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getChildren()
	 */
	@Override
	public List getModelChildren() {
		return getModelElement().getElements();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return false;
	}
	
	public List<ItemPart> getItemParts(){
		LinkedList<ItemPart> retVal = new LinkedList<ItemPart>();
		for(Object o : getChildren()){
			if(o instanceof ItemPart){
				retVal.add((ItemPart)o);
			}
		}
		return retVal;
	}
	
	public ItemPart getIntersectionItem(ItemPart part, Rectangle bounds){
		for(ItemPart p : getItemParts()){
			if(p.getFigure().intersects(bounds) && part!=p){
				return p;
			}
		}
		return null;
	}
	
	public ItemPart getItemPartAt(Point point){
		for(ItemPart p : getItemParts()){
			if(p.getFigure().containsPoint(point)){
				return p;
			}
		}
		return null;
	}

}
