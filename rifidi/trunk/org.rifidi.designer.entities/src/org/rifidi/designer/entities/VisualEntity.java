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

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.GameTaskQueue;

/**
 * A VisualEntity is an entity with a visual representation (bright me).
 * 
 * @author Jochen Mader Oct 3, 2007
 * 
 */
public abstract class VisualEntity extends Entity {
	/**
	 * Queue that gets executed in the opengl context.
	 */
	private GameTaskQueue renderQueue;
	/**
	 * Queue that gets executed in the update context.
	 */
	private GameTaskQueue updateQueue;
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
	private static Matrix3f rotLeft = null;
	
	@XmlTransient
	private static Matrix3f rotRight = null;
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
	 * Rotate's the entity's visual representation to the left.
	 * 
	 */
	public void rotateLeft() {
		if (rotLeft == null) {
			Quaternion quat = new Quaternion();
			quat.fromAngles(0f, (float)Math.toRadians(90), 0f);
			rotLeft = quat.toRotationMatrix();
		}

		getNode().getLocalRotation().apply(rotLeft);
	}

	/**
	 * Rotate's the entity's visual representation to the right.
	 * 
	 */
	public void rotateRight() {
		if (rotRight == null) {
			Quaternion quat = new Quaternion();
			quat.fromAngles(0f, (float)Math.toRadians(-90), 0f);
			rotRight = quat.toRotationMatrix();
		}

		getNode().getLocalRotation().apply(rotRight);
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
	}

	@Override
	public void setEntityId(String entityId) {
		super.setEntityId(entityId);
		getNode().setName(entityId);
	}
	

	/**
	 * @param renderQueue the renderQueue to set
	 */
	public void setRenderQueue(GameTaskQueue renderQueue) {
		this.renderQueue = renderQueue;
	}

	/**
	 * @param updateQueue the updateQueue to set
	 */
	public void setUpdateQueue(GameTaskQueue updateQueue) {
		this.updateQueue = updateQueue;
	}
	
	/**
	 * Submit a callable to the update queue.
	 * @param callable
	 */
	public void update(Callable<Object> callable){
		updateQueue.enqueue(callable);
	}
	
	/**
	 * Submit a callable to the opengl queue.
	 * @param callable
	 */
	public void render(Callable<Object> callable){
		renderQueue.enqueue(callable);
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

	/**
	 * Set the current level of LOD.
	 * 
	 * @param lod
	 */
	public abstract void setLOD(int lod);

	/**
	 * Get a node containing standard boxes that describe the bounds of the
	 * model. NOTE: NOT BoundingBox
	 * 
	 * @return
	 */
	public abstract Node getBoundingNode();
}