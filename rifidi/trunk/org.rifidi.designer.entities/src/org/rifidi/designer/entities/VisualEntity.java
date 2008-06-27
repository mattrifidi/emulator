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

import javax.xml.bind.annotation.XmlTransient;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

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
	 * Whether or not the entity's footprint is collidable.
	 */
	private boolean collides = true;
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

	public abstract void setLOD(int lod);
}