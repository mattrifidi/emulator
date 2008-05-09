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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.annotations.MonitoredProperties;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.Field;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.services.annotations.Inject;

import com.jme.input.InputHandler;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
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
public class WatchAreaEntity extends VisualEntity implements NeedsPhysics,
		Field, Switch {
	/**
	 * logger for this class.
	 */
	private static Log logger = LogFactory.getLog(WatchAreaEntity.class);
	/**
	 * Reference to the physicsspace.
	 */
	private PhysicsSpace physicsSpace;
	/**
	 * Stopped material state.
	 */
	private static MaterialState msStopped;
	/**
	 * Started material state.
	 */
	private static MaterialState msStarted;
	/**
	 * Shared alphastate.
	 */
	private static AlphaState as;
	/**
	 * Running state of this entity.
	 */
	private boolean running = false;
	/**
	 * Reference to the events service.
	 */
	private EventsService eventsService;
	/**
	 * Reference to the field service
	 */
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
					.createAlphaState();
			as.setBlendEnabled(true);
			as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
			as.setDstFunction(AlphaState.DB_ONE);
			as.setEnabled(true);
		}

		getNode().clearRenderState(RenderState.RS_ALPHA);
		getNode().clearRenderState(RenderState.RS_MATERIAL);
		getNode().setRenderState(as);
		getNode().updateRenderState();
		((PhysicsNode)getNode()).setMaterial(Material.GHOST);
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
		}
		else{
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
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler(com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setPhysicsSpace(com.jmex.physics.PhysicsSpace)
	 */
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOff()
	 */
	@Override
	public void turnOff() {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getNode().clearRenderState(RenderState.RS_MATERIAL);
				getNode().setRenderState(msStopped);
				return null;
			}

		});
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOn()
	 */
	@Override
	public void turnOn() {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getNode().clearRenderState(RenderState.RS_MATERIAL);
				getNode().setRenderState(msStarted);
				return null;
			}

		});
		running = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Field#fieldEntered(org.rifidi.designer.entities.Entity)
	 */
	@Override
	public void fieldEntered(Entity entity) {
		eventsService.publish(
				new WatchAreaEvent(true, this, entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Field#fieldLeft(org.rifidi.designer.entities.Entity)
	 */
	@Override
	public void fieldLeft(Entity entity) {
		eventsService.publish(
				new WatchAreaEvent(false, this, entity));
	}

	/**
	 * @param eventsService the eventsService to set
	 */
	@Inject
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	/**
	 * @param fieldService the fieldService to set
	 */
	@Inject
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}
	
}
