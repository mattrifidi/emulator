/*
 *  VisualEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities;

import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.swt.graphics.Point;
import org.rifidi.designer.entities.interfaces.Directional;
import org.rifidi.designer.entities.placement.BinaryPattern;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.FragmentProgramState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;

/**
 * A VisualEntity is an entity with a visual representation (bright me).
 * 
 * @author Jochen Mader Oct 3, 2007
 * 
 */
public abstract class VisualEntity extends Entity {

	/**
	 * The node that belongs to the VisualEntity.
	 */
	private Node node;
	/**
	 * The placement pattern for this entity.
	 */
	private BinaryPattern pattern;
	/**
	 * Whether or not the entity's footprint is collidable.
	 */
	private boolean collides = true;
	/**
	 * The footprint node of quads.
	 */ 
	@XmlTransient
	private Node footprint;
	/**
	 * The current color of the footprint.
	 */ 
	@XmlTransient
	private ColorRGBA footprintColor;
	/**
	 * Whether or not to show the footprint pattern for this entity.
	 */ 
	@XmlTransient
	private boolean showfootprint = false;
	/**
	 * 90 degree rotation matrix.
	 */
	@XmlTransient
	private static Matrix3f rotationMatrix90 = null;

	/**
	 * @return the node
	 */
	@XmlTransient
	public Node getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(final Node node) {
		this.node = node;
	}

	/**
	 * @return the pattern
	 */
	public BinaryPattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern(final BinaryPattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * Sets whether or not the footprint is actively being displayed Uses cyan
	 * as the default color if true is given for the flag
	 * 
	 * @param flag
	 *            boolean flag indicating the new state
	 * @author dan
	 */
	public void showFootprint(final boolean flag) {
		showFootprint(flag, ColorRGBA.magenta);
	}

	/**
	 * Sets whether or not the footprint is actively being displayed
	 * 
	 * @param flag
	 *            boolean flag indicating the new state
	 * @param color
	 *            the color to display the footprint
	 * @author dan
	 */
	public void showFootprint(final boolean flag, final ColorRGBA color) {
		if (flag)
			setFootprintColor(color);
		if (showfootprint != flag) {
			if (flag)
				attachFootprint();
			else
				detatchFootprint();
			showfootprint = flag;
		}
	}

	/**
	 * Sets the footprint to the given color
	 * 
	 * @param footprintColor
	 *            the new footprint color
	 */
	public void setFootprintColor(final ColorRGBA newFootprintColor) {
		footprintColor = newFootprintColor;
		if (footprint != null || showfootprint) {
			GameTaskQueueManager.getManager().update(new Callable<Object>() {
				public Object call() throws Exception {
					MaterialState ms = DisplaySystem.getDisplaySystem()
							.getRenderer().createMaterialState();
					ms.setDiffuse(footprintColor);
					footprint.setRenderState(ms);
					footprint.updateRenderState();
					return null;
				}
			});
		}
	}

	/**
	 * Attaches the entity's footprint to its visual representation
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#showFootprint(boolean)
	 * @see org.rifidi.designer.entities.VisualEntity#showFootprint(boolean,ColorRGBA)
	 * @author dan
	 */
	private void attachFootprint() {
		if (footprint == null) {
			footprint = new Node("footprintnode");
			float angle = (float) (Math.PI * -0.5);
			float offset = getNode().worldToLocal(new Vector3f(0, .099f, 0),
					new Vector3f()).y;
			footprint.setLocalTranslation(0, offset, 0);
			boolean[][] patternOrg = getPattern().getOriginalPattern();
			int hw = getPattern().getOriginalWidth() / 2;
			int hh = getPattern().getOriginalLength() / 2;
			for (int y = 0; y < getPattern().getOriginalLength(); y++) {
				for (int x = 0; x < getPattern().getOriginalWidth(); x++) {
					if (patternOrg[y][x]) {
						Quad quadZ = new Quad("quaddy", .9f, .9f);
						quadZ.getLocalRotation().fromAngleAxis(angle,
								Vector3f.UNIT_X);
						quadZ.setLocalTranslation(x + 0.5f - hw, 0, y + 0.5f
								- hh);
						footprint.attachChild(quadZ);
					}
				}
			}
		}

		GameTaskQueueManager.getManager().update(new Callable<Object>() {
			public Object call() throws Exception {
				getNode().attachChild(footprint);
				getNode().updateRenderState();
				return null;
			}
		});
	}

	/**
	 * Detatches the footprint from the entity's visual representation.
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#showFootprint(boolean)
	 * @see org.rifidi.designer.entities.VisualEntity#showFootprint(boolean,ColorRGBA)
	 * @author dan
	 */
	private void detatchFootprint() {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {
			public Object call() throws Exception {
				getNode().detachChild(footprint);
				return null;
			}
		});
	}

	/**
	 * @return whether or not the footprint is actively being shown
	 */
	public boolean isShowFootprint() {
		return showfootprint;
	}

	/**
	 * Rotate's the entity's visual representation and its bitmap pattern for
	 * collision checking
	 * 
	 * @author jochen
	 * @author dan
	 */
	public void rotateRight() {
		if (rotationMatrix90 == null) {
			Quaternion quat = new Quaternion();
			quat.fromAngleAxis(FastMath.PI * 0.5f, Vector3f.UNIT_Y);
			rotationMatrix90 = quat.toRotationMatrix();
		}

		getNode().getLocalRotation().apply(rotationMatrix90);
		getPattern().rotateRight();
	}

	/**
	 * Calculate the entity's position in the bitmap using its current pattern
	 * 
	 * @return the entity's pattern's position in the bitmap
	 * @author dan
	 */
	public Point getPositionFromTranslation() {
		if(getPattern()==null){
			return new Point(0,0);
		}
		return getPositionFromTranslation(getPattern());
	}

	/**
	 * convenience method to calculate the current position of the target on the
	 * grid. (upper-left-hand corner)
	 * 
	 * @param pattern
	 *            the pattern to use for the calculations
	 * @return the entity's pattern's position in the bitmap
	 * @author jochen
	 */
	public Point getPositionFromTranslation(final BinaryPattern pattern) {
		Vector3f patternSize = new Vector3f(pattern.getWidth() / 2, 0, pattern
				.getLength() / 2);
		Vector3f pos = getNode().getLocalTranslation().subtract(patternSize);
		return new Point((int) pos.x, (int) pos.z);
	}

	/**
	 * Checks to see if the the given visual entity's footprint collides with
	 * this one's
	 * 
	 * @param ent
	 *            the entity to check for a collision with
	 * @return true if the entities collide, false otherwise
	 * @author Dan West - dan@pramari.com
	 */
	public boolean collidesWith(final VisualEntity ent) {
		if ( isCollides() ) {
			Point myloc = getPositionFromTranslation();
			Point loc2 = ent.getPositionFromTranslation();
			boolean[][] mypatt = getPattern().getPattern();
			BinaryPattern patt2 = ent.getPattern();
			if(mypatt !=null && patt2 !=null){
				for (int y = 0; y < mypatt.length; y++) {
					for (int x = 0; x < mypatt[y].length; x++) {
						if (mypatt[y][x]) {
							int y2 = myloc.y - loc2.y + y;
							int x2 = myloc.x - loc2.x + x;
							if (y2 >= 0 && y2 < patt2.getPattern().length && x2 >= 0
									&& x2 < patt2.getPattern()[y2].length)
								if (patt2.getPattern()[y2][x2])
									return true;
						}
					}
				}	
			}
		}
		return false;
	}

	/**
	 * @return the footprint color
	 */
	public ColorRGBA getFootprintColor() {
		return footprintColor;
	}

	/**
	 * @param fragmentProgramState
	 *            the fragment program to hilite with
	 * @param alphaState
	 *            the alpha state to hilite with
	 */
	public void hilite(final FragmentProgramState fragmentProgramState,
			final AlphaState alphaState) {
		for (Spatial s : getNode().getChildren()) {
			if (!s.equals(footprint)) {
				s.setRenderState(fragmentProgramState);
				s.setRenderState(alphaState);
			}
		}
		getNode().setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		getNode().updateRenderState();

		if (this instanceof Directional) {
			((Directional) this).showDirectionalIndicator();
		}
	}

	/**
	 * Clear the hilites on this entity
	 */
	public void clearHilite() {
		for (Spatial s : getNode().getChildren()) {
			if (!s.equals(footprint)) {
				s.clearRenderState(RenderState.RS_FRAGMENT_PROGRAM);
				s.clearRenderState(RenderState.RS_ALPHA);
			}
		}
		getNode().setRenderQueueMode(Renderer.QUEUE_SKIP);
		getNode().updateRenderState();

		if (this instanceof Directional) {
			((Directional) this).hideDirectionalIndicator();
		}
	}

	/**
	 * @return true if this entity can collide with other entities
	 */
	public boolean isCollides() {
		return collides;
	}

	/**
	 * @param collides
	 *            the new value indicating whether or not this entity can
	 *            collide with other entities
	 */
	public void setCollides(boolean collides) {
		this.collides = collides;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method is used after adding the entity to the project.
	 * 
	 */
	public abstract void init();

	/**
	 * This method is called after the entity was loaded.
	 */
	public abstract void loaded();

	@Override
	public void setEntityId(String entityId) {
		super.setEntityId(entityId);
		getNode().setName(entityId);
	}
	
}