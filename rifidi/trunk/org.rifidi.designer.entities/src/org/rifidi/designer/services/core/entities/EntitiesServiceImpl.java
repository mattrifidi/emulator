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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.monklypse.core.SWTDefaultImplementor;
import org.rifidi.designer.entities.Activator;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.SceneData.Direction;
import org.rifidi.designer.entities.gpio.GPIPort;
import org.rifidi.designer.entities.gpio.GPOPort;
import org.rifidi.designer.entities.grouping.IChildEntity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.grouping.IParentEntity;
import org.rifidi.designer.entities.interfaces.IProducer;
import org.rifidi.designer.entities.interfaces.IInternalEntity;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;
import org.rifidi.designer.entities.interfaces.IContainer;
import org.rifidi.designer.entities.rifidi.RifidiEntity;
import org.rifidi.designer.library.EntityLibraryRegistry;
import org.rifidi.designer.octree.CollisionOctree;
import org.rifidi.designer.octree.RoomOctree;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.initializer.IInitService;
import org.rifidi.services.initializer.exceptions.InitializationException;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.impl.C0G1Tag;
import org.rifidi.services.tags.impl.C1G1Tag;
import org.rifidi.services.tags.impl.C1G2Tag;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

/**
 * 
 * 
 * @author Jochen Mader Jan 25, 2008
 * @tags
 * 
 */
public class EntitiesServiceImpl implements EntitiesService, ProductService,
		FinderService, SceneDataService {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(EntitiesServiceImpl.class);
	/** Reference to the current scene. */
	private SceneData sceneData;
	/**
	 * Reference to the old scene to allow the deletion callable to work while
	 * the new scene is loaded..
	 */
	private SceneData sceneDataOld;
	/** Quickreference list to find entities by their nodes */
	private Map<Node, VisualEntity> nodeToEntity;
	/** the file the scene was loaded from. */
	private IFile fileOfCurrentScene;
	/** objects listening for scenedata events. */
	private List<SceneDataChangedListener> listeners;
	/** Reference to the initservice. */
	private IInitService iinitService;
	/** The collision tree. */
	private CollisionOctree collisionOctree = null;
	/** Tree for checking collisions against the room. */
	private RoomOctree roomTree = null;
	/** Default implementor. */
	private SWTDefaultImplementor implementor;
	/** Reference to the tag registry. */
	private ITagRegistry tagRegistry;

	/**
	 * Constructor.
	 */
	public EntitiesServiceImpl() {
		logger.debug("EntitiesService created");
		listeners = new ArrayList<SceneDataChangedListener>();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.EntitiesService#addEntity(
	 * org.rifidi.designer.entities.Entity,
	 * org.rifidi.designer.services.core.entities.NewEntityListener,
	 * org.eclipse.swt.graphics.Point)
	 */
	@Override
	public void addEntity(Entity ent, NewEntityListener newEntityListener,
			Point screenPos) {
		int namecount = 0;
		String orgName = ent.getName();
		while (sceneData.getEntityNames().contains(ent.getName())) {
			ent.setName(orgName + namecount++);
		}
		initEntity(ent, sceneData, true);

		if (ent instanceof VisualEntity) {
			implementor.update(new UpdateCallable(sceneData.getRootNode(),
					(VisualEntity) ent, screenPos, newEntityListener));
		} else {
			ent.setEntityId(sceneData.getNextID().toString());
			sceneData.getEntityNames().add(ent.getName());
			sceneData.getSyncedEntities().add(ent);
			if (!(ent instanceof IInternalEntity)
					|| (ent instanceof IInternalEntity && ((IInternalEntity) ent)
							.isVisible())) {
				sceneData.getDefaultGroup().addEntity(ent);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#deleteEntities
	 * (java.util.List)
	 */
	@Override
	public void deleteEntities(final List<Entity> entities) {
		List<VisualEntity> visuals = new ArrayList<VisualEntity>();
		for (Entity entity : entities) {
			entity.setDeleted(true);
			if (entity instanceof VisualEntity) {
				visuals.add((VisualEntity) entity);
				nodeToEntity.remove(((VisualEntity) entity).getNode());
				if (entity instanceof IContainer) {
					for (VisualEntity ve : ((IContainer) entity)
							.getVisualEntityList()) {
						// not all available spots are taken
						if (ve != null) {
							nodeToEntity.remove(ve.getNode());
							sceneData.getSyncedEntities().remove(ve);
							collisionOctree.removeEntity((VisualEntity) entity);
						}
					}
				}
				collisionOctree.removeEntity((VisualEntity) entity);
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
			implementor.update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				public Object call() throws Exception {
					for (Entity entity : entities) {
						if (entity instanceof VisualEntity) {
							((VisualEntity) entity).getNode()
									.removeFromParent();
						}
						entity.destroy();
					}
					return new Object();
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#getEntityNames
	 * ()
	 */
	@Override
	public List<String> getEntityNames() {
		return new ArrayList<String>(sceneData.getEntityNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#addEntitiesToGroup
	 * (org.rifidi.designer.entities.grouping.EntityGroup, java.lang.String)
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
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#addEntityGroup
	 * (org.rifidi.designer.entities.EntityGroup)
	 */
	@Override
	public void addEntityGroup(final EntityGroup entityGroup) {
		sceneData.getDefaultGroup().removeEntities(entityGroup.getEntities());
		entityGroup.setSceneData(sceneData);
		sceneData.getEntityGroups().add(entityGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.FinderService#entityGroupExists
	 * (org.rifidi.designer.entities.EntityGroup)
	 */
	@Override
	public boolean entityGroupExists(final EntityGroup entityGroup) {
		return sceneData.getEntityGroups().contains(entityGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#removeEntityGroup
	 * (org.rifidi.designer.entities.EntityGroup)
	 */
	@Override
	public void removeEntityGroup(final EntityGroup entityGroup) {
		sceneData.getDefaultGroup().addEntities(entityGroup.getEntities());
		sceneData.getEntityGroups().remove(entityGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#getEntityGroups
	 * ()
	 */
	@Override
	public List<EntityGroup> getEntityGroups() {
		return Collections.unmodifiableList(sceneData.getEntityGroups());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.services.registry.core.entities.FinderService#
	 * getVisualEntityByNode(com.jme.scene.Node)
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
		if (ret instanceof IChildEntity) {
			return (VisualEntity) ((IChildEntity) ret).getParent();
		}
		return (VisualEntity) ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.ProductService#addProduct(
	 * org.rifidi.designer.entities.Entity)
	 */
	@Override
	public void addProduct(final Entity product) {
		sceneData.getEntityNames().add(product.getName());
		initEntity(product, sceneData, true);
		sceneData.getSyncedEntities().add(product);
		sceneData.getProducedEntities().addEntity(product);
		if (product instanceof VisualEntity) {
			implementor.update(new UpdateCallable(sceneData.getRootNode(),
					(VisualEntity) product, null, null));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.entities.ProductService#deleteProducts
	 * (java.util.List)
	 */
	@Override
	public void deleteProducts(final List<Entity> product) {
		implementor.update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			public Object call() throws Exception {
				for (Entity entity : product) {
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
	 * @see
	 * org.rifidi.services.registry.core.entities.FinderService#isEntityGrouped
	 * (org.rifidi.designer.entities.Entity)
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
	 * @see
	 * org.rifidi.services.registry.core.entities.EntitiesService#ungroupEntity
	 * (org.rifidi.designer.entities.Entity)
	 */
	@Override
	public void ungroupEntity(final Entity entity) {
		for (EntityGroup group : sceneData.getEntityGroups()) {
			if (group.contains(entity)) {
				group.removeEntity(entity);
				sceneData.getDefaultGroup().addEntity(entity);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.FinderService#getEntitiesByType
	 * (java.lang.Class)
	 */
	@Override
	public List<Entity> getEntitiesByType(Class<?> type) {
		List<Entity> entities = new ArrayList<Entity>();
		if (sceneData != null) {
			synchronized (sceneData.getEntities()) {
				for (Entity ent : sceneData.getEntities()) {
					if (ent.getClass().equals(type)) {
						entities.add(ent);
					} else {
						for (Class<?> iface : ent.getClass().getInterfaces()) {
							if (iface.equals(type)) {
								entities.add(ent);
								break;
							}
						}
					}
				}
			}
		}
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#loadScene
	 * (org.eclipse.swt.widgets.Display, org.eclipse.core.resources.IFile)
	 */
	public void loadScene(final Display display, final IFile file) {
		tagRegistry.initialize();
		// invalidate the current sceneData
		for (SceneDataChangedListener listener : listeners) {
			listener.destroySceneData(this.sceneData);
		}
		// clean up
		TextureManager.clearCache();
		// destroy old entities
		if (sceneData != null) {
			sceneDataOld = sceneData;
		}
		Job loadJob = new Job("Load scene") {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime
			 * .IProgressMonitor)
			 */
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				monitor.beginTask("Load new scene", 100);
				display.syncExec(new Runnable() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						try {
							// destroy the old scene
							if (sceneDataOld != null) {
								for (Entity entity : sceneDataOld.getEntities()) {
									try {
										entity.destroy();
									} catch (Exception e) {
										// THIS MUST RUN THROUGH
										logger.fatal(e);
									}
								}
							}
							// initialize jaxb to know about the classes
							// provided by
							// the
							// libraries
							logger.debug("initializing jaxb");
							List<Class<?>> classes = EntityLibraryRegistry
									.getInstance().getEntityClasses();
							classes
									.add(org.rifidi.designer.entities.SceneData.class);
							classes
									.add(org.rifidi.designer.entities.VisualEntity.class);
							classes.add(C0G1Tag.class);
							classes.add(C1G1Tag.class);
							classes.add(C1G2Tag.class);
							classes.add(RifidiTag.class);
							classes.add(GPIPort.class);
							classes.add(GPOPort.class);
							JAXBContext context = JAXBContext
									.newInstance(classes.toArray(new Class[0]));
							Unmarshaller unmarshaller = context
									.createUnmarshaller();
							Object unm = unmarshaller.unmarshal(file
									.getContents());
							sceneData = (SceneData) unm;
							tagRegistry.initialize(sceneData.getTags());

						} catch (JAXBException e) {
							logger.fatal("Unable to load file (JAXB): " + e);
						} catch (CoreException e) {
							logger.fatal("Unable to load file (JAXB): " + e);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});

				monitor.worked(10);
				monitor.subTask("Convert to scene");
				sceneData.setDisplay(display);
				sceneData.setPhysicsSpace(PhysicsSpace.create());
				sceneData.setCollisionHandler(new InputHandler());

				// initialize the JME importer to handle physics
				sceneData.getPhysicsSpace().setupBinaryClassLoader(
						BinaryImporter.getInstance());
				// if this is a new file create an empty room
				if (sceneData.getNodeBytes() != null) {
					// load the model from the stored bytes
					try {
						sceneData.setRootNode((Node) BinaryImporter
								.getInstance().load(sceneData.getNodeBytes()));
					} catch (IOException e) {
						logger.warn("failed loading " + e);
					}
					monitor.worked(10);
					monitor.subTask("Registering entities");
					for (Entity entity : sceneData.getEntities()) {
						ServiceRegistry.getInstance().service(entity);
						sceneData.getEntityNames().add(entity.getName());
						initEntity(entity, sceneData, false);
					}
					monitor.worked(10);
					monitor.subTask("Registering products");
					display.syncExec(new Runnable() {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						@Override
						public void run() {
							for (Entity entity : sceneData
									.getProducedEntities().getEntities()) {
								ServiceRegistry.getInstance().service(entity);
								sceneData.getEntityNames()
										.add(entity.getName());
								initEntity(entity, sceneData, false);
							}
							for (EntityGroup entityGroup : sceneData
									.getEntityGroups()) {
								entityGroup.setSceneData(sceneData);
							}
							monitor.worked(10);
							monitor.subTask("Initialize visual entities");
							// has to be the last step!!!
							for (Entity entity : sceneData.getEntities()) {
								if (entity instanceof VisualEntity) {
									((VisualEntity) entity).loaded();
								}
							}
						}

					});
				}
				logger.debug("loading: done");
				monitor.worked(10);
				monitor.setTaskName("Load room");
				Node roomnode = EntityLibraryRegistry.getInstance()
						.getFloorReferences().get(sceneData.getFloorId())
						.getNode();
				ArrayList<Spatial> spatlist = new ArrayList<Spatial>(roomnode
						.getChildren());
				// turn the geometry into a set of physicsnodes
				for (Spatial spatial : spatlist) {
					spatial.removeFromParent();
					StaticPhysicsNode staticNode = sceneData.getPhysicsSpace()
							.createStaticNode();
					staticNode.attachChild(spatial);
					staticNode.setName(spatial.getName());
					staticNode.generatePhysicsGeometry();
					roomnode.attachChild(staticNode);
				}
				roomnode.updateWorldBound();
				roomnode.updateModelBound();
				roomnode.clearRenderState(RenderState.RS_LIGHT);
				roomnode.updateRenderState();
				collisionOctree = new CollisionOctree(1f,
						(BoundingBox) roomnode.getWorldBound());
				roomTree = new RoomOctree(1f, (BoundingBox) roomnode
						.getWorldBound());
				for (Spatial spatial : spatlist) {
					if (!"floor".equals(spatial.getName())) {
						for (Spatial spat : ((Node) spatial).getChildren()) {
							roomTree.insertMesh((TriMesh) spat);
						}
					}
				}
				sceneData.setRoomNode(roomnode);
				fileOfCurrentScene = file;
				nodeToEntity = Collections
						.synchronizedMap(new HashMap<Node, VisualEntity>());
				sceneData.getRootNode().updateGeometricState(0f, true);
				// Box marker=new Box("marker",Vector3f.ZERO.clone(),3,3,3);
				// sceneData.getRootNode().attachChild(marker);
				for (Entity entity : sceneData.getSearchableEntities()) {
					if (entity instanceof VisualEntity) {
						nodeToEntity.put(((VisualEntity) entity).getNode(),
								(VisualEntity) entity);
						collisionOctree.insertEntity((VisualEntity) entity);
					}
				}
				display.syncExec(new Runnable() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						for (SceneDataChangedListener listener : listeners) {
							listener.sceneDataChanged(sceneData);
						}
					}

				});
				monitor.worked(60);
				monitor.worked(100);
				sceneDataOld = null;
				return Status.OK_STATUS;
			}
		};
		loadJob.setUser(true);
		loadJob.schedule();

	}

	/**
	 * Initialze the entity TODO: REDUNDANT!!!
	 * 
	 * @param entity
	 * @param sceneData
	 * @param isNew
	 *            set to true if the entity was just created (not loaded)
	 */
	private void initEntity(Entity entity, SceneData sceneData, boolean isNew) {
		// reassociate the entities with their nodes if it is loaded, skip if it
		// is a new one
		if (entity instanceof VisualEntity) {
			((VisualEntity) entity)
					.setUpdateQueue(implementor.getUpdateQueue());
			((VisualEntity) entity)
					.setRenderQueue(implementor.getRenderQueue());
			if (!isNew) {
				((VisualEntity) entity).setNode((Node) sceneData.getRootNode()
						.getChild(entity.getEntityId().toString()));
			}
		}
		if (entity instanceof RifidiEntity) {
			((RifidiEntity) entity)
					.setRMIManager(Activator.getDefault().rifidiManager);
		}
		if (entity instanceof INeedsPhysics) {
			((INeedsPhysics) entity)
					.setPhysicsSpace(sceneData.getPhysicsSpace());
			((INeedsPhysics) entity).setCollisionHandler(sceneData
					.getCollisionHandler());
		}
		ServiceRegistry.getInstance().service(entity);
		if (entity instanceof IProducer) {
			List<Entity> prods = new ArrayList<Entity>();
			prods.addAll(((IProducer) entity).getProducts());
			sceneData.getProducedEntities().addEntities(prods);
		}
		// do custom initialization
		try {
			iinitService.init(entity);
		} catch (InitializationException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#saveScene
	 * (org.eclipse.core.resources.IFile)
	 */
	@Override
	public void saveScene(IFile file) {
		fileOfCurrentScene = file;
		saveScene();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#saveScene()
	 */
	@Override
	public void saveScene() {
		implementor.update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				Node parent = sceneData.getRootNode().getParent();
				sceneData.getRootNode().removeFromParent();
				sceneData.getRoomNode().removeFromParent();
				sceneData.setTags(tagRegistry.getTags());
				sceneData.getDisplay().syncExec(new Runnable() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {

						try {
							fileOfCurrentScene.setContents(
									new ByteArrayInputStream(
											toByteArray(sceneData)),
									IFile.FORCE, null);
						} catch (CoreException e) {
							logger.error("Error while saving: " + e);
						}
					}

				});
				sceneData.getRootNode().attachChild(sceneData.getRoomNode());
				parent.attachChild(sceneData.getRootNode());
				return null;
			}

		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.services.registry.core.scenedata.SceneDataService#
	 * addSceneDataChangedListener
	 * (org.rifidi.services.registry.core.scenedata.SceneDataChangedListener)
	 */
	@Override
	public void addSceneDataChangedListener(SceneDataChangedListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.services.registry.core.scenedata.SceneDataService#
	 * removeSceneDataChangedListener
	 * (org.rifidi.services.registry.core.scenedata.SceneDataChangedListener)
	 */
	@Override
	public void removeSceneDataChangedListener(SceneDataChangedListener listener) {
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#getWalls()
	 */
	@Override
	public Map<Direction, Node> getWalls() {
		return sceneData.getWalls();
	}

	/**
	 * Get the currently loaded SceneData.
	 * 
	 * @return currently loaded SceneData or null
	 */
	public SceneData getCurrentSceneData() {
		return sceneData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#getName()
	 */
	@Override
	public String getName() {
		return sceneData.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#setName(
	 * java.lang.String)
	 */
	@Override
	public void setName(String name) {
		sceneData.setName(name);
	}

	/**
	 * Helper method to convert the given SceneData into a saveable byte array.
	 * 
	 * @return
	 */
	private byte[] toByteArray(SceneData sceneData) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		BinaryExporter jmee = new BinaryExporter();
		ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
		try {
			jmee.save(sceneData.getRootNode(), bo);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			sceneData.setNodeBytes(bo.toByteArray());
			EntityLibraryRegistry.getInstance().getLibraries();
			List<Class<?>> classes = EntityLibraryRegistry.getInstance()
					.getEntityClasses();
			classes.add(org.rifidi.designer.entities.SceneData.class);
			classes.add(org.rifidi.designer.entities.VisualEntity.class);
			classes.add(C0G1Tag.class);
			classes.add(C1G1Tag.class);
			classes.add(C1G2Tag.class);
			classes.add(RifidiTag.class);
			JAXBContext context = JAXBContext.newInstance(classes
					.toArray(new Class[0]));
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(sceneData, fileOutput);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return fileOutput.toByteArray();
	}

	/**
	 * @param initService
	 *            the initService to set
	 */
	@Inject
	public void setInitService(IInitService iinitService) {
		this.iinitService = iinitService;
	}

	/**
	 * @param initService
	 *            the initService to unset
	 */
	public void unsetIInitService(IInitService initService) {
		this.iinitService = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.EntitiesService#getColliders
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public Set<VisualEntity> getColliders(VisualEntity visualEntity) {
		Set<VisualEntity> colliders = new HashSet<VisualEntity>();
		for (Entity entity : sceneData.getEntities()) {
			if (!entity.equals(visualEntity) && entity instanceof VisualEntity) {
				// safeguard against those who are too lazy initialize all
				// their entities
				if (((VisualEntity) entity).getNode() != null
						&& ((VisualEntity) entity).getNode().getWorldBound() != null) {
					if (((VisualEntity) entity).getNode().getWorldBound()
							.intersects(visualEntity.getNode().getWorldBound())) {
						colliders.add((VisualEntity) entity);
					}
				}
			}
		}
		return colliders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.EntitiesService#getCollisionOctree
	 * ()
	 */
	@Override
	public CollisionOctree getCollisionOctree() {
		return collisionOctree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.EntitiesService#getRoomOctree
	 * ()
	 */
	@Override
	public RoomOctree getRoomOctree() {
		return roomTree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.EntitiesService#collidesWithScene
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public boolean collidesWithScene(VisualEntity visualEntity) {
		return roomTree.findCollisions(visualEntity);
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
		private Entity newentity;
		/**
		 * Root of the scenegraph.
		 */
		private Node rootNode;
		/**
		 * Callback for new entities.
		 */
		private NewEntityListener listener;
		/**
		 * Position of the entity on the screen.
		 */
		private Point screenPos;

		/**
		 * Constructor.
		 * 
		 * @param rootNode
		 *            scene root
		 * @param newentity
		 *            entity to add
		 * @param screenPos
		 *            screen position where the entity should be put
		 * 
		 */
		public UpdateCallable(final Node rootNode, final Entity newentity,
				final Point screenPos, final NewEntityListener listener) {
			this.newentity = newentity;
			this.rootNode = rootNode;
			this.listener = listener;
			this.screenPos = screenPos;
		}

		public Object call() throws Exception {
			prepareEntity((VisualEntity) newentity, rootNode);
			if (newentity instanceof IParentEntity) {
				for (VisualEntity child : ((IParentEntity) newentity)
						.getChildEntites()) {
					sceneData.getEntityNames().add(child.getName());
					initEntity(child, sceneData, true);
					sceneData.getSyncedEntities().add(child);
					((IChildEntity) child).setParent((VisualEntity) newentity);

					prepareEntity((VisualEntity) child,
							((VisualEntity) newentity).getNode());
				}
			}
			// center the object in the scene

			if (screenPos != null) {
				Vector3f coords = DisplaySystem.getDisplaySystem()
						.getRenderer().getCamera().getWorldCoordinates(
								new Vector2f(screenPos.x, screenPos.y), 0);
				Vector3f coords2 = DisplaySystem.getDisplaySystem()
						.getRenderer().getCamera().getWorldCoordinates(
								new Vector2f(screenPos.x, screenPos.y), 1);
				Vector3f direction = coords.subtract(coords2).normalizeLocal();
				coords.subtractLocal(direction.mult(coords.y / direction.y));
				coords.setY(0);
				// round the values to place it on the grid
				coords.x = (float) Math.floor(coords.x);
				coords.z = (float) Math.floor(coords.z);
				((VisualEntity) newentity).getNode()
						.setLocalTranslation(coords);
			}
			if (listener != null) {
				sceneData.getDisplay().asyncExec(new Runnable() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						listener.entityAdded((VisualEntity) newentity);
					}

				});
			}
			sceneData.getEntityNames().add(newentity.getName());
			sceneData.getSyncedEntities().add(newentity);
			if (!(newentity instanceof IInternalEntity)
					|| (newentity instanceof IInternalEntity && ((IInternalEntity) newentity)
							.isVisible())) {
				sceneData.getDefaultGroup().addEntity(newentity);
			}
			return new Object();
		}

		private void prepareEntity(VisualEntity entity, Node target) {
			String id = sceneData.getNextID().toString();
			entity.setEntityId(id);
			entity.init();
			if (entity instanceof IContainer) {
				for (VisualEntity vent : ((IContainer) entity)
						.getVisualEntityList()) {
					initEntity(vent, sceneData, true);
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
			target.attachChild(entity.getNode());
			entity.getNode().updateWorldBound();
			entity.getNode().setName(id);
			if (entity.getNode() instanceof PhysicsNode) {
				((PhysicsNode) entity.getNode()).generatePhysicsGeometry();
			}

			entity.getNode().updateRenderState();

			nodeToEntity.put(((VisualEntity) entity).getNode(),
					(VisualEntity) entity);
		}
	}

	/**
	 * @param implementor
	 *            the implementor to set
	 */
	@Inject
	public void setImplementor(SWTDefaultImplementor implementor) {
		this.implementor = implementor;
	}

	/**
	 * @param tagRegistry
	 *            the tagRegistry to set
	 */
	@Inject
	public void setTagRegistry(ITagRegistry tagRegistry) {
		this.tagRegistry = tagRegistry;
	}

}
