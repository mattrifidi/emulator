/*
 *  EntitiesServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.swt.graphics.Point;
import org.rifidi.designer.entities.CableEntity;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.interfaces.ChildEntity;
import org.rifidi.designer.entities.interfaces.Field;
import org.rifidi.designer.entities.interfaces.GPO;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.ParentEntity;
import org.rifidi.designer.entities.interfaces.RifidiEntity;
import org.rifidi.designer.entities.interfaces.VisualEntityHolder;
import org.rifidi.designer.rcp.Activator;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.designer.services.core.scenedata.SceneDataChangedListener;
import org.rifidi.designer.services.core.scenedata.SceneDataService;
import org.rifidi.designer.utils.Helpers;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.GameTaskQueueManager;
import com.jmex.physics.PhysicsNode;

/**
 * 
 * 
 * @author Jochen Mader Jan 25, 2008
 * @tags
 * 
 */
public class EntitiesServiceImpl implements EntitiesService, ProductService,
		FinderService, SceneDataChangedListener {
	/**
	 * Reference to the current scene.
	 */
	private SceneData sceneData;
	/**
	 * Quickreference list to find entities by their nodes
	 */
	private Map<Node, VisualEntity> nodeToEntity;
	/**
	 * Reference to the field service
	 */
	private FieldService fieldService;
	/**
	 * Reference to the cabling service
	 */
	private CablingService cablingService;
	/**
	 * Reference to the events service
	 */
	private EventsService eventsService;
	/**
	 * Reference to the scenedataservice.
	 */
	private SceneDataService sceneDataService;

	/**
	 * Constructor.
	 */
	public EntitiesServiceImpl() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#addEntity(org.rifidi.designer.entities.Entity,
	 *      java.lang.Boolean)
	 */
	@Override
	public void addEntity(Entity ent, Boolean center) {
		int namecount = 0;
		String orgName = ent.getName();
		while (sceneData.getEntityNames().contains(ent.getName())) {
			ent.setName(orgName + namecount++);
		}
		sceneData.getEntityNames().add(ent.getName());
		initEntity(ent);
		sceneData.getSyncedEntities().add(ent);
		if (!(ent instanceof CableEntity)) {
			sceneData.getDefaultGroup().addEntity(ent);
		}
		if (ent instanceof VisualEntity) {
			Helpers.waitOnCallabel(new UpdateCallable(sceneData.getRootNode(),
					(VisualEntity) ent, center, null));
			if (ent instanceof Field) {
				fieldService.registerField((Field) ent);
			}
		}
		if (ent instanceof ParentEntity) {
			for (VisualEntity child : ((ParentEntity) ent).getChildEntites()) {
				sceneData.getEntityNames().add(child.getName());
				initEntity(child);
				sceneData.getSyncedEntities().add(child);
				((ChildEntity) child).setParent((VisualEntity) ent);
				if (child instanceof VisualEntity) {
					Helpers.waitOnCallabel(new UpdateCallable(
							((VisualEntity) ent).getNode(),
							(VisualEntity) child, false, null));
				}
				child.setEntityId(sceneData.getNextID().toString());
				if (child instanceof Field) {
					fieldService.registerField((Field) child);
				}
			}
		}
		if (ent instanceof GPO) {
			((GPO) ent).setCablingService(cablingService);
		}
		ent.setEntityId(sceneData.getNextID().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#deleteEntities(java.util.List)
	 */
	@Override
	public void deleteEntities(final List<Entity> entities) {
		List<VisualEntity> visuals = new ArrayList<VisualEntity>();
		for (Entity entity : entities) {
			if (entity instanceof VisualEntity) {
				visuals.add((VisualEntity) entity);
				nodeToEntity.remove(((VisualEntity) entity).getNode());
				if (entity instanceof VisualEntityHolder) {
					for (VisualEntity ve : ((VisualEntityHolder) entity)
							.getVisualEntityList()) {
						nodeToEntity.remove(ve.getNode());
						sceneData.getSyncedEntities().remove(ve);
					}
				}
				if (((VisualEntity) entity).getPattern() != null) {
					sceneData.getBitMap().removePattern(
							getPositionFromTranslation((VisualEntity) entity),
							((VisualEntity) entity).getPattern().getPattern());
				}
			}
			sceneData.getSyncedEntities().remove(entity);
			sceneData.getDefaultGroup().removeEntity(entity);
			sceneData.getProducedEntities().removeEntity(entity);
			for (EntityGroup entityGroup : sceneData.getEntityGroups()) {
				entityGroup.removeEntity(entity);
			}
			sceneData.getEntityNames().remove(entity.getName());
		}
		if (visuals.size() > 0) {
			Helpers.waitOnCallabel(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				public Object call() throws Exception {
					for (Entity entity : entities) {
						entity.destroy();
						if (entity instanceof VisualEntity) {
							((VisualEntity) entity).getNode()
									.removeFromParent();
						}
					}
					return new Object();
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#getEntityNames()
	 */
	@Override
	public List<String> getEntityNames() {
		return new ArrayList<String>(sceneData.getEntityNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#addEntitiesToGroup(org.rifidi.designer.entities.grouping.EntityGroup,
	 *      java.lang.String)
	 */
	@Override
	public void addEntitiesToGroup(final EntityGroup entityGroup,
			final String entityIds) {
		String[] ids = entityIds.split("\n");
		for (String id : ids) {
			for (Entity entity : sceneData.getSearchableEntities()) {
				if (entity.getEntityId().equals(id)) {
					sceneData.getDefaultGroup().removeEntity(entity);
					sceneData.getProducedEntities().removeEntity(entity);
					for (EntityGroup group : sceneData.getEntityGroups()) {
						group.removeEntity(entity);
					}
					entityGroup.addEntity(entity);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#addEntityGroup(org.rifidi.designer.entities.EntityGroup)
	 */
	@Override
	public void addEntityGroup(final EntityGroup entityGroup) {
		for (Entity entity : entityGroup.getEntities()) {
			sceneData.getDefaultGroup().removeEntity(entity);
		}
		entityGroup.setSceneData(sceneData);
		sceneData.getEntityGroups().add(entityGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.FinderService#entityGroupExists(org.rifidi.designer.entities.EntityGroup)
	 */
	@Override
	public boolean entityGroupExists(final EntityGroup entityGroup) {
		return sceneData.getEntityGroups().contains(entityGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#removeEntityGroup(org.rifidi.designer.entities.EntityGroup)
	 */
	@Override
	public void removeEntityGroup(final EntityGroup entityGroup) {
		for (Entity entity : entityGroup.getEntities()) {
			sceneData.getDefaultGroup().addEntity(entity);
		}
		sceneData.getEntityGroups().remove(entityGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#getEntityGroups()
	 */
	@Override
	public List<EntityGroup> getEntityGroups() {
		return Collections.unmodifiableList(sceneData.getEntityGroups());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(final SceneData sceneData) {
		this.sceneData = sceneData;
		nodeToEntity = Collections
				.synchronizedMap(new HashMap<Node, VisualEntity>());
		for (Entity entity : sceneData.getSearchableEntities()) {
			if (entity instanceof VisualEntity) {
				nodeToEntity.put(((VisualEntity) entity).getNode(),
						(VisualEntity) entity);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(final SceneData sceneData) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.FinderService#getVisualEntityByNode(com.jme.scene.Node)
	 */
	@Override
	public VisualEntity getVisualEntityByNode(final Node node) {
		Node searchNode = node;
		Entity ret = null;
		while (ret == null) {
			ret = nodeToEntity.get(searchNode);
			searchNode = searchNode.getParent();
			if (searchNode == null) {
				break;
			}
		}
		if (ret instanceof ChildEntity) {
			return (VisualEntity) ((ChildEntity) ret).getParent();
		}
		return (VisualEntity) ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.ProductService#addProduct(org.rifidi.designer.entities.Entity)
	 */
	@Override
	public void addProduct(final Entity product) {
		sceneData.getEntityNames().add(product.getName());
		initEntity(product);
		sceneData.getSyncedEntities().add(product);
		sceneData.getProducedEntities().addEntity(product);
		if (product instanceof VisualEntity) {
			GameTaskQueueManager.getManager().update(
					new UpdateCallable(sceneData.getRootNode(),
							(VisualEntity) product, false, sceneData
									.getNextID().toString()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.ProductService#deleteProducts(java.util.List)
	 */
	@Override
	public void deleteProducts(final List<Entity> product) {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			public Object call() throws Exception {
				for (Entity entity : product) {
					sceneData.getBitMap().removePattern(
							getPositionFromTranslation((VisualEntity) entity),
							((VisualEntity) entity).getPattern().getPattern());
					entity.destroy();
					sceneData.getSyncedEntities().remove(entity);
					sceneData.getDefaultGroup().removeEntity(entity);
					sceneData.getProducedEntities().removeEntity(entity);
					sceneData.getEntityNames().remove(entity.getName());
					if (entity instanceof VisualEntity) {
						nodeToEntity.remove(((VisualEntity) entity).getNode());
						((VisualEntity) entity).getNode().removeFromParent();
					}
				}
				return new Object();
			}
		});
		if (!((WritableList) sceneData.getEntityGroups()).getRealm().equals(
				Realm.getDefault())) {
			((WritableList) sceneData.getEntityGroups()).getRealm().asyncExec(
					new Runnable() {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						@Override
						public void run() {
							for (Entity entity : product) {
								for (EntityGroup entityGroup : sceneData
										.getEntityGroups()) {
									entityGroup.removeEntity(entity);
								}
							}
						}

					});
		}
		;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.FinderService#isEntityGrouped(org.rifidi.designer.entities.Entity)
	 */
	@Override
	public boolean isEntityGrouped(final Entity entity) {
		for (EntityGroup group : sceneData.getEntityGroups()) {
			if (group.getEntities().contains(entity)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.entities.EntitiesService#ungroupEntity(org.rifidi.designer.entities.Entity)
	 */
	@Override
	public void ungroupEntity(final Entity entity) {
		for (EntityGroup group : sceneData.getEntityGroups()) {
			if (group.getEntities().contains(entity)) {
				group.removeEntity(entity);
				sceneData.getDefaultGroup().addEntity(entity);
			}
		}
	}

	/**
	 * Initialize an entity
	 * 
	 * @param entity
	 */
	private void initEntity(Entity entity) {
		if (entity instanceof RifidiEntity) {
			((RifidiEntity) entity)
					.setRMIManager(Activator.getDefault().rifidiManager);
		}
		if (entity instanceof NeedsPhysics) {
			((NeedsPhysics) entity)
					.setPhysicsSpace(sceneData.getPhysicsSpace());
			((NeedsPhysics) entity).setCollisionHandler(sceneData
					.getCollisionHandler());
		}
		entity.setEventsService(eventsService);
		ServiceRegistry.getInstance().service(entity);
	}

	/**
	 * Convenience method to calculate the current position of the target on the
	 * grid. (upper-left-hand corner).
	 * 
	 * @return
	 */
	private Point getPositionFromTranslation(VisualEntity target) {
		Vector3f patternSize = new Vector3f(target.getPattern().getWidth() / 2,
				0, target.getPattern().getLength() / 2);
		Vector3f pos = target.getNode().getLocalTranslation().subtract(
				patternSize);
		return new Point((int) pos.x, (int) pos.z);
	}

	/**
	 * @param fieldService
	 *            the fieldService to set
	 */
	public void setCollisionService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.FinderService#getEntitiesByType(java.lang.Class)
	 */
	@Override
	public List<Entity> getEntitiesByType(Class type) {
		List<Entity> entities = new ArrayList<Entity>();
		synchronized (sceneData.getEntities()) {
			for (Entity ent : sceneData.getEntities()) {
				if (ent.getClass().equals(type)) {
					entities.add(ent);
				} else {
					for (Class iface : ent.getClass().getInterfaces()) {
						if (iface.equals(type)) {
							entities.add(ent);
							break;
						}
					}
				}
			}
		}
		return entities;
	}

	/**
	 * Used to do updates on the scene graph.
	 * 
	 * 
	 * @author Jochen Mader Jan 26, 2008
	 * @tags
	 * 
	 */
	private class UpdateCallable implements Callable<Object> {
		/**
		 * Entity that should be connected to the scenegraph.
		 */
		private VisualEntity entity;
		/**
		 * Root of the scenegraph.
		 */
		private Node rootNode;
		/**
		 * Center the camera on the object?
		 */
		private boolean center;
		/**
		 * The id for the node.
		 */
		private String id;

		/**
		 * Constructor.
		 * 
		 * @param rootNode
		 *            scene root
		 * @param entity
		 *            entity to add
		 * @param center
		 *            center on the entity
		 */
		public UpdateCallable(final Node rootNode, final VisualEntity entity,
				final boolean center, String id) {
			this.entity = entity;
			this.rootNode = rootNode;
			this.center = center;
			this.id = id;
		}

		public Object call() throws Exception {
			entity.init();

			if (entity instanceof VisualEntityHolder) {
				for (VisualEntity vent : ((VisualEntityHolder) entity)
						.getVisualEntityList()) {
					initEntity(vent);
					vent.init();
					vent.setEntityId(sceneData.getNextID().toString());
					sceneData.getSyncedEntities().add(vent);
					entity.getNode().attachChild(vent.getNode());
					nodeToEntity.put(((VisualEntity) vent).getNode(),
							(VisualEntity) vent);
				}
			}
			entity.getNode().updateModelBound();
			entity.getNode().updateWorldData(0);
			rootNode.attachChild(entity.getNode());
			entity.getNode().updateWorldBound();
			if (id != null) {
				entity.getNode().setName(id);
				entity.setEntityId(id);
			}
			if (entity.getNode() instanceof PhysicsNode) {
				((PhysicsNode) entity.getNode()).generatePhysicsGeometry();
			}

			// center the object in the scene
			if (center) {
				float halfWidth = sceneData.getWidth() / 2f;
				entity.getNode().setLocalTranslation(halfWidth,
						entity.getNode().getLocalTranslation().y, halfWidth);
				entity.getNode().updateRenderState();
			}

			nodeToEntity.put(((VisualEntity) entity).getNode(),
					(VisualEntity) entity);
			return new Object();
		}

	}

	/**
	 * @param eventsService the eventsService to set
	 */
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	/**
	 * @param eventsService the eventsService to unset
	 */
	public void unsetEventsService(EventsService eventsService) {
		this.eventsService = null;
	}

}
