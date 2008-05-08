/*
 *  FieldServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.Field;
import org.rifidi.designer.entities.interfaces.ParentEntity;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.scenedata.SceneDataService;
import org.rifidi.services.annotations.Inject;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.scene.Node;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.contact.ContactInfo;

/**
 * Standard implementation of the field service.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 4, 2008
 * 
 */
public class FieldServiceImpl implements FieldService {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(FieldServiceImpl.class);
	/**
	 * Map of currently registered fields and their associated handlers..
	 */
	private Map<Field, InputAction> fields;
	/**
	 * This map maps entities to the fields they are colliding with.
	 */
	private Map<Field, Set<VisualEntity>> collisions;
	/**
	 * Currently loaded scene.
	 */
	private SceneData sceneData;
	/**
	 * Reference to the finderservice
	 */
	private FinderService finderService;
	/**
	 * Reference to the scenedataservice.
	 */
	private SceneDataService sceneDataService;
	
	/**
	 * Constructor.
	 */
	public FieldServiceImpl() {
		logger.debug("FieldService created");
		fields = new HashMap<Field, InputAction>();
		collisions = new HashMap<Field, Set<VisualEntity>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.collision.FieldService#registerField(org.rifidi.designer.entities.interfaces.Field)
	 */
	@Override
	public void registerField(Field field) {
		fields.put(field, new ColliderInputAction(field));
		SyntheticButton intersect = ((PhysicsNode) ((VisualEntity) field)
				.getNode()).getCollisionEventHandler();
		sceneData.getCollisionHandler().addAction(fields.get(field), intersect,
				false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.collision.FieldService#unregisterField(org.rifidi.designer.entities.interfaces.Field)
	 */
	@Override
	public void unregisterField(Field field) {
		sceneData.getCollisionHandler().removeAction(fields.get(field));
		fields.remove(field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.collision.FieldService#getCurrentFieldsList()
	 */
	@Override
	public List<Field> getCurrentFieldsList() {
		return new ArrayList<Field>(fields.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.collision.FieldService#checkFields()
	 */
	@Override
	public void checkFields() {
		for (Field field : collisions.keySet()) {
			Set<VisualEntity> entities = collisions.get(field);
			entities.remove(null);
			List<VisualEntity> entitiesFixed = new ArrayList<VisualEntity>(
					entities);
			for (VisualEntity ent : entitiesFixed) {
				if (ent != null
						&& !sceneData.getPhysicsSpace().collide(
								(PhysicsNode) ((VisualEntity) field).getNode(),
								(PhysicsNode) ent.getNode())) {
					entities.remove(ent);
					field.fieldLeft(ent);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		fields.clear();
		collisions.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
		this.sceneData = sceneData;
		for (Entity entity : sceneData.getSearchableEntities()) {
			if (entity instanceof Field) {
				registerField((Field) entity);
			}
			else if (entity instanceof ParentEntity) {
				for(VisualEntity ve:((ParentEntity)entity).getChildEntites()){
					if (ve instanceof Field) {
						registerField((Field) ve);
					}		
				}
			}
		}
	}

	/**
	 * If a collision occured this method gets executed.
	 * 
	 * @param field
	 *            the field the collision occured in
	 * @param collider
	 *            the colliding entity
	 */
	private void collisionDetected(Field field, Node collider) {
		if (!collisions.containsKey(field)) {
			collisions.put(field, new HashSet<VisualEntity>());
		}
		Entity coll = finderService.getVisualEntityByNode(collider);
		if (coll != null
				&& !collisions.get(field).contains((VisualEntity) coll)) {
			collisions.get(field).add((VisualEntity) coll);
			field.fieldEntered(coll);
		}
	}

	/**
	 * @param finderService
	 *            the finderService to set
	 */
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}
	
	/**
	 * @param finderService
	 *            the finderService to unset
	 */
	public void unsetFinderService(FinderService finderService) {
		this.finderService = null;
	}
	
	/**
	 * @param sceneDataService the sceneDataService to set
	 */
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
	}

	/**
	 * @param sceneDataService the sceneDataService to unset
	 */
	public void unsetSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = null;
	}

	/**
	 * An InputAction that is responsible for handling collision events.
	 * 
	 * 
	 * @author Jochen Mader - jochen@pramari.com - Feb 6, 2008
	 * 
	 */
	private class ColliderInputAction extends InputAction {
		/**
		 * The field this action is bound to.
		 */
		private Field field;

		/**
		 * Constructor
		 * 
		 * @param field
		 *            the Field this action is bound to
		 */
		public ColliderInputAction(Field field) {
			super();
			this.field = field;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
		 */
		@Override
		public void performAction(InputActionEvent evt) {
			Node collider = ((ContactInfo) evt.getTriggerData()).getNode1()
					.equals(((VisualEntity) field).getNode()) ? ((ContactInfo) evt
					.getTriggerData()).getNode2()
					: ((ContactInfo) evt.getTriggerData()).getNode1();
			collisionDetected(field, collider);
		}

	}
}
