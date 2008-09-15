/*
 *  GridNode.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.grid;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

/**
 * 
 * This node is used to create a grid. IT has to be removed before the scene
 * gets saved!
 * 
 * @author Jochen Mader Nov 1, 2007
 * 
 */
public class GridNode extends Node {

	private static final long serialVersionUID = 1L;

	private boolean show = true;

	public GridNode(String name, Integer width, Integer height, Float linewidth) {
		super(name);
		this.setLocalTranslation(width / 2, 0.02f, width / 2);
		String protoNameW = "Line: width";
		String protoNameH = "Line: height";
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		ms.setEmissive(new ColorRGBA(0, 0.75f, 0, 0.5f));
		ms.setDiffuse(new ColorRGBA(0, 1, 0, 0.5f));
		ms.setSpecular(new ColorRGBA(1, 0, 0, 0.5f));
		ms.setShininess(100);
		this.setRenderState(ms);

		CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		//TODO: needed?
		//cs.setCullHint(CullHint.);
		cs.setEnabled(true);
		this.setRenderState(cs);

		float hw = width / 2f;
		float hh = height / 2f;
		float angle = (float) (-Math.PI / 2);

		// rotate 90 degrees
		for (int i = 0; i <= width; i++) {
			Quad widthspan = new Quad(protoNameW + "_" + i, linewidth, height);
			widthspan.getLocalRotation().fromAngleAxis(angle, Vector3f.UNIT_X);
			widthspan.setLocalTranslation(i - hw, 0.02f, 0);
			this.attachChild(widthspan);
		}

		for (int i = 0; i <= height; i++) {
			Quad heightspan = new Quad(protoNameH + "_" + i, width, linewidth);
			heightspan.getLocalRotation().fromAngleAxis(angle, Vector3f.UNIT_X);
			heightspan.setLocalTranslation(0, 0.02f, i - hh);
			this.attachChild(heightspan);
		}
	}

	/**
	 * @return the show
	 */
	public boolean isShow() {
		return show;
	}

	/**
	 * @param show
	 *            the show to set
	 */
	public void setShow(boolean show) {
		this.show = show;
	}

}
