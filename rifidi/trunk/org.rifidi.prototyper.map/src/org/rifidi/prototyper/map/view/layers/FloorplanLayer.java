/**
 * 
 */
package org.rifidi.prototyper.map.view.layers;

import org.eclipse.draw2d.Layer;
import org.rifidi.prototyper.map.view.figures.FloorplanFigure;
import org.rifidi.prototyper.model.FloorplanViewModel;

/**
 * @author kyle
 * 
 */
public class FloorplanLayer extends Layer {

	private FloorplanFigure floorplan;

	public FloorplanLayer(FloorplanViewModel floorplanModel) {
		this.floorplan = new FloorplanFigure(floorplanModel);
		add(floorplan);
	}
}
