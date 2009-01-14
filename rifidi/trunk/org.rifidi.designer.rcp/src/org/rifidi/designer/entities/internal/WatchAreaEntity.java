/*
 *  WatchAreaEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.IField;
import org.rifidi.designer.entities.interfaces.IHasSwitch;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.services.annotations.Inject;

import com.jme.input.InputHandler;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * A simple cardboard box.
 * 
 * @author Jochen Mader Oct 8, 2007
 * 
 */
@MonitoredProperties(names = { "name" })
public class WatchAreaEntity extends VisualEntity implements INeedsPhysics,
		IField, IHasSwitch, IAdaptable {
	/** Reference to the physicsspace. */
	@XmlTransient
	private PhysicsSpace physicsSpace;
	/** Stopped material state. */
	@XmlTransient
	private static MaterialState msStopped;
	/** Started material state. */
	@XmlTransient
	private static MaterialState msStarted;
	/** Shared alphastate. */
	@XmlTransient
	private static BlendState as;
	/** Running state of this entity. */
	private boolean running = false;
	/** Reference to the events service. */
	@XmlTransient
	private EventsService eventsService;
	/** Reference to the field service */
	@XmlTransient
	private FieldService fieldService;

	/**
	 * Constructor.
	 */
	public WatchAreaEntity() {
		setName("Watch Area");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		if (!(getNode() instanceof StaticPhysicsNode)) {
			StaticPhysicsNode phys = physicsSpace.createStaticNode();
			phys.setLocalTranslation(getNode().getWorldTranslation());
			for (Spatial spatial : new ArrayList<Spatial>(getNode()
					.getChildren())) {
				phys.attachChild(spatial);
			}

			getNode().removeFromParent();
			setNode(phys);
			phys.generatePhysicsGeometry();
			loaded();
			fieldService.registerField(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		if (msStopped == null) {
			msStopped = DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
			msStopped.setDiffuse(new ColorRGBA(1, 0, 0, .5f));
			msStopped.setEmissive(new ColorRGBA(1, 0, 0, .3f));
			msStopped.setShininess(1);
			msStopped.setSpecular(new ColorRGBA(1, 0, 0, .3f));
		}
		if (as == null) {
			as = DisplaySystem.getDisplaySystem().getRenderer()
					.createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.One);
			as.setEnabled(true);
		}

		getNode().clearRenderState(RenderState.RS_BLEND);
		getNode().clearRenderState(RenderState.RS_MATERIAL);
		getNode().setRenderState(as);
		getNode().updateRenderState();
		((PhysicsNode) getNode()).setMaterial(Material.GHOST);
		if (msStarted == null) {
			msStarted = DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
			msStarted.setDiffuse(new ColorRGBA(0, 1, 0, .5f));
			msStarted.setEmissive(new ColorRGBA(0, 1, 0, .3f));
			msStarted.setShininess(1);
			msStarted.setSpecular(new ColorRGBA(0, 1, 0, .3f));
		}
		if (running) {
			turnOn();
		} else {
			turnOff();
		}
		fieldService.registerField(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		fieldService.unregisterField(this);
		((PhysicsNode) getNode()).delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.INeedsPhysics#setCollisionHandler
	 * (com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.INeedsPhysics#setPhysicsSpace(
	 * com.jmex.physics.PhysicsSpace)
	 */
	@Override
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#turnOff()
	 */
	@Override
	public void turnOff() {
		update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getNode().clearRenderState(RenderState.RS_MATERIAL);
				getNode().setRenderState(msStopped);
				getNode().updateRenderState();
				return null;
			}

		});
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#turnOn()
	 */
	@Override
	public void turnOn() {
		update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getNode().clearRenderState(RenderState.RS_MATERIAL);
				getNode().setRenderState(msStarted);
				getNode().updateRenderState();
				return null;
			}

		});
		running = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IField#fieldEntered(org.rifidi
	 * .designer.entities.Entity)
	 */
	@Override
	public void fieldEntered(Entity entity) {
		eventsService.publish(new WatchAreaEvent(true, this, entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IField#fieldLeft(org.rifidi.designer
	 * .entities.Entity)
	 */
	@Override
	public void fieldLeft(Entity entity) {
		eventsService.publish(new WatchAreaEvent(false, this, entity));
	}

	/**
	 * @param eventsService
	 *            the eventsService to set
	 */
	@Inject
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	/**
	 * @param fieldService
	 *            the fieldService to set
	 */
	@Inject
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
		// No LOD for this one.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#getBoundingNode()
	 */
	@Override
	public Node getBoundingNode() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if (IWorkbenchAdapter.class.equals(adapter)) {
			return new WatchAreaWorkbenchAdapter();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#pause()
	 */
	@Override
	public void pause() {
		((PhysicsNode) getNode()).setMaterial(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#start()
	 */
	@Override
	public void start() {
		((PhysicsNode) getNode()).setMaterial(Material.GHOST);
	}

}
