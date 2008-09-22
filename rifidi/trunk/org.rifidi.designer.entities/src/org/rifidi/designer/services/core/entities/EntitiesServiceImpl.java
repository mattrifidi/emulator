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
import org.eclipse.swt.widgets.Display;
import org.monklypse.core.SWTDefaultImplementor;
import org.rifidi.designer.entities.Activator;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.SceneData.Direction;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.interfaces.ChildEntity;
import org.rifidi.designer.entities.interfaces.InternalEntity;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.ParentEntity;
import org.rifidi.designer.entities.interfaces.RifidiEntity;
import org.rifidi.designer.entities.interfaces.VisualEntityHolder;
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

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.state.RenderState;
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
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(EntitiesServiceImpl.class);
	/**
	 * Reference to the current scene.
	 */
	private SceneData sceneData;
	/**
	 * Reference to the old scene to allow the deletion callable to work while
	 * the new scene is loaded..
	 */
	private SceneData sceneDataOld;
	/**
	 * Quickreference list to find entities by their nodes
	 */
	private Map<Node, VisualEntity> nodeToEntity;
	/**
	 * the file the scene was loaded from.
	 */
	private IFile fileOfCurrentScene;
	/**
	 * objects listening for scenedata events.
	 */
	private List<SceneDataChangedListener> listeners;
	/**
	 * Reference to the initservice.
	 */
	private IInitService iinitService;
	/**
	 * The collision tree.
	 */
	private CollisionOctree collisionOctree = null;
	/**
	 * Tree for checking collisions against the room.
	 */
	private RoomOctree roomTree = null;
	/** Default implementor. */
	private SWTDefaultImplementor implementor;

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
	 * org.rifidi.services.registry.core.entities.EntitiesService#addEntity(
	 * org.rifidi.designer.entities.Entity, java.lang.Boolean)
	 */
	@Override
	public void addEntity(Entity ent, Boolean center, NewEntityListener listener) {
		int namecount = 0;
		String orgName = ent.getName();
		while (sceneData.getEntityNames().contains(ent.getName())) {
			ent.setName(orgName + namecount++);
		}
		sceneData.getEntityNames().add(ent.getName());
		sceneData.getSyncedEntities().add(ent);
		if (!(ent instanceof InternalEntity)
				|| (ent instanceof InternalEntity && ((InternalEntity) ent)
						.isVisible())) {
			sceneData.getDefaultGroup().addEntity(ent);
		}
		initEntity(ent, sceneData, true);

		if (ent instanceof VisualEntity) {
			implementor.update(new UpdateCallable(sceneData.getRootNode(),
					(VisualEntity) ent, center, listener));
		} else {
			ent.setEntityId(sceneData.getNextID().toString());
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
			if (entity instanceof VisualEntity) {
				visuals.add((VisualEntity) entity);
				nodeToEntity.remove(((VisualEntity) entity).getNode());
				if (entity instanceof VisualEntityHolder) {
					for (VisualEntity ve : ((VisualEntityHolder) entity)
							.getVisualEntityList()) {
						// not all available spots are taken
						if (ve != null) {
							nodeToEntity.remove(ve.getNode());
							sceneData.getSyncedEntities().remove(ve);
						}
					}
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
		for (Entity entity : entityGroup.getEntities()) {
			sceneData.getDefaultGroup().removeEntity(entity);
		}
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
		for (Entity entity : entityGroup.getEntities()) {
			sceneData.getDefaultGroup().addEntity(entity);
		}
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
		if (ret instanceof ChildEntity) {
			return (VisualEntity) ((ChildEntity) ret).getParent();
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
					(VisualEntity) product, false, null));
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
			if (group.getEntities().contains(entity)) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataService#loadScene
	 * (org.eclipse.swt.widgets.Display, org.eclipse.core.resources.IFile)
	 */
	@SuppressWarnings("unchecked")
	public void loadScene(Display display, IFile file) {
		// invalidate the current sceneData
		for (SceneDataChangedListener listener : listeners) {
			listener.destroySceneData(this.sceneData);
		}
		TextureManager.clearCache();
		if (sceneData != null) {
			sceneDataOld = sceneData;
			implementor.update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					for (Entity entity : sceneDataOld.getEntities()) {
						try {
							entity.destroy();
						} catch (Exception e) {
							// THIS MUST RUN THROUGH
							logger.fatal(e);
						}
					}
					return null;
				}

			});
		}
		try {
			// initialize jaxb to know about the classes provided by the
			// libraries
			logger.debug("initializing jaxb");
			List<Class> classes = EntityLibraryRegistry.getInstance()
					.getEntityClasses();
			classes.add(org.rifidi.designer.entities.SceneData.class);
			classes.add(org.rifidi.designer.entities.VisualEntity.class);
			classes.add(C0G1Tag.class);
			classes.add(C1G1Tag.class);
			classes.add(C1G2Tag.class);
			classes.add(RifidiTag.class);
			JAXBContext context = JAXBContext.newInstance(classes
					.toArray(new Class[0]));
			logger.debug("loading");
			Unmarshaller unmarshaller = context.createUnmarshaller();

			try {
				Object unm = unmarshaller.unmarshal(file.getContents());
				sceneData = (SceneData) unm;
				sceneData.setDisplay(display);
				sceneData.setPhysicsSpace(PhysicsSpace.create());
				// sceneData.getPhysicsSpace().setAutoRestThreshold(2);
				sceneData.setCollisionHandler(new InputHandler());

				// initialize the JME importer to handle physics
				sceneData.getPhysicsSpace().setupBinaryClassLoader(
						BinaryImporter.getInstance());
				// if this is a new file create an empty room
				if (sceneData.getNodeBytes() != null) {
					// load the model from the stored bytes

					sceneData.setRootNode((Node) BinaryImporter.getInstance()
							.load(sceneData.getNodeBytes()));
					for (Entity entity : sceneData.getEntities()) {
						ServiceRegistry.getInstance().service(entity);
						sceneData.getEntityNames().add(entity.getName());
						initEntity(entity, sceneData, false);
					}
					for (Entity entity : sceneData.getProducedEntities()
							.getEntities()) {
						ServiceRegistry.getInstance().service(entity);
						sceneData.getEntityNames().add(entity.getName());
						initEntity(entity, sceneData, false);
					}
					for (EntityGroup entityGroup : sceneData.getEntityGroups()) {
						entityGroup.setSceneData(sceneData);
					}
				}

			} catch (IOException e) {
				logger.fatal("Unable to load file (IOException): " + e);
				return;
			} catch (CoreException e) {
				logger.fatal("Unable to load file (CoreException): " + e);
				return;
			}
			logger.debug("loading: done");
		} catch (JAXBException e) {
			logger.fatal("Unable to load file (JAXB): " + e);
			return;
		}
		Node roomnode = EntityLibraryRegistry.getInstance()
				.getFloorReferences().get(sceneData.getFloorId()).getNode();
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
		collisionOctree = new CollisionOctree(1f, (BoundingBox) roomnode
				.getWorldBound());
		roomTree = new RoomOctree(1f, (BoundingBox) roomnode.getWorldBound());
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
		for (SceneDataChangedListener listener : listeners) {
			listener.sceneDataChanged(sceneData);
		}
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
			((VisualEntity) entity).setUpdateQueue(implementor.getUpdateQueue());
			((VisualEntity) entity).setRenderQueue(implementor.getRenderQueue());
			if(!isNew){
				((VisualEntity) entity).setNode((Node) sceneData.getRootNode()
						.getChild(entity.getEntityId().toString()));	
			}
		}
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

		ServiceRegistry.getInstance().service(entity);
		// do custom initialization
		try {
			iinitService.init(entity);
		} catch (InitializationException e) {
			e.printStackTrace();
		}
		// has to be the last step!!!
		if (!isNew && entity instanceof VisualEntity) {
			((VisualEntity) entity).loaded();
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
	@SuppressWarnings("unchecked")
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
			List<Class> classes = EntityLibraryRegistry.getInstance()
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
		 * Center the camera on the object?
		 */
		private boolean center;
		/**
		 * Callback for new entities.
		 */
		private NewEntityListener listener;

		/**
		 * Constructor.
		 * 
		 * @param rootNode
		 *            scene root
		 * @param newentity
		 *            entity to add
		 * @param center
		 *            center on the entity
		 */
		public UpdateCallable(final Node rootNode, final Entity newentity,
				final boolean center, NewEntityListener listener) {
			this.newentity = newentity;
			this.rootNode = rootNode;
			this.center = center;
			this.listener = listener;
		}

		public Object call() throws Exception {
			prepareEntity((VisualEntity) newentity, rootNode);
			if (newentity instanceof ParentEntity) {
				for (VisualEntity child : ((ParentEntity) newentity)
						.getChildEntites()) {
					sceneData.getEntityNames().add(child.getName());
					initEntity(child, sceneData, true);
					sceneData.getSyncedEntities().add(child);
					((ChildEntity) child).setParent((VisualEntity) newentity);

					prepareEntity((VisualEntity) child,
							((VisualEntity) newentity).getNode());
				}
			}
			// center the object in the scene
			if (center) {
				((VisualEntity) newentity).getNode().setLocalTranslation(
						(float) Math.floor(((BoundingBox) sceneData
								.getRoomNode().getWorldBound()).xExtent),
						((VisualEntity) newentity).getNode()
								.getLocalTranslation().y,
						(float) Math.floor(((BoundingBox) sceneData
								.getRoomNode().getWorldBound()).zExtent));
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
			return new Object();
		}

		private void prepareEntity(VisualEntity entity, Node target) {
			entity.init();
			if (entity instanceof VisualEntityHolder) {
				for (VisualEntity vent : ((VisualEntityHolder) entity)
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
			String id = sceneData.getNextID().toString();
			entity.getNode().setName(id);
			entity.setEntityId(id);
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

}
