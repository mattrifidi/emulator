/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.TreeSearch;

/**
 * A layer that overrides containsPoint and findfigureAt so that it behaves like
 * a Figure.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapLayer extends Layer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Layer#containsPoint(int, int)
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		return getBounds().contains(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Layer#findFigureAt(int, int,
	 * org.eclipse.draw2d.TreeSearch)
	 */
	@Override
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		if (!containsPoint(x, y))
			return null;
		if (search.prune(this))
			return null;
		IFigure child = findDescendantAtExcluding(x, y, search);
		if (child != null)
			return child;
		if (search.accept(this))
			return this;
		return null;
	}

}
