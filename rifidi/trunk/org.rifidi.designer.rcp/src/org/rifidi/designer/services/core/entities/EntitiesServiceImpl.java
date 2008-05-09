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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.rifidi.designer.entities.placement.BitMap;
import org.rifidi.designer.library.EntityLibraryRegistry;
import org.rifidi.designer.rcp.Activator;
import org.rifidi.designer.rcp.views.view3d.View3D.Direction;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.utils.Helpers;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
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
	 * the file the scene was loaded from.
	 */
	private IFile fileOfCurrentScene;
	/**
	 * objects listening for scenedata events.
	 */
	private List<SceneDataChangedListener> listeners;
	/**
	 * Reference to the selection service.
	 */
	private SelectionService selectionService;

	/**
	 * Constructor.
	 */
	public EntitiesServiceImpl() {
		listeners = new ArrayList<SceneDataChangedListener>();
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
		sceneData.getSyncedEntities().add(ent);
		if (!(ent instanceof CableEntity)) {
			sceneData.getDefaultGroup().addEntity(ent);
		}
		initEntity(ent, sceneData, true);

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
				initEntity(child, sceneData, true);
				sceneData.getSyncedEntities().add(child);
				((ChildEntity) child).setParent((VisualEntity) ent);

				Helpers.waitOnCallabel(new UpdateCallable(((VisualEntity) ent)
						.getNode(), (VisualEntity) child, false, null));
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
		initEntity(product, sceneData, true);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#loadScene(org.eclipse.core.resources.IFile)
	 */
	@SuppressWarnings("unchecked")
	public void loadScene(IFile file) {
		TextureManager.clearCache();
		// invalidate the current sceneData
		for (SceneDataChangedListener listener : listeners) {
			listener.destroySceneData(this.sceneData);
		}
		try {
			// initialize jaxb to know about the classes provided by the
			// libraries
			logger.debug("initializing jaxb");
			List<Class> classes = EntityLibraryRegistry.getInstance()
					.getEntityClasses();
			classes.add(org.rifidi.designer.entities.SceneData.class);
			classes.add(org.rifidi.designer.entities.VisualEntity.class);
			classes.add(org.rifidi.designer.entities.CableEntity.class);
			classes.add(org.rifidi.emulator.tags.impl.C0G1Tag.class);
			classes.add(org.rifidi.emulator.tags.impl.C1G1Tag.class);
			classes.add(org.rifidi.emulator.tags.impl.C1G2Tag.class);
			classes.add(org.rifidi.emulator.tags.impl.RifidiTag.class);
			JAXBContext context = JAXBContext.newInstance(classes
					.toArray(new Class[0]));
			logger.debug("loading");
			Unmarshaller unmarshaller = context.createUnmarshaller();

			try {
				Object unm = unmarshaller.unmarshal(file.getContents());
				sceneData = (SceneData) unm;
				sceneData.setPhysicsSpace(PhysicsSpace.create());
				sceneData.setCollisionHandler(new InputHandler());

				// initialize the JME importer to handle physics
				sceneData.getPhysicsSpace().setupBinaryClassLoader(
						BinaryImporter.getInstance());

				// if this is a new file create an empty room
				if (sceneData.getNodeBytes() == null) {
					sceneData.setBitMap(new BitMap(sceneData.getWidth()));
					// sceneData.getRootNode().attachChild(createRoom());
				} else {// load the model from the stored bytes

					// let the textures load from the right spot
					try {
						URI dirpath = Activator.class.getClassLoader()
								.getResource("/").toURI();
						ResourceLocatorTool.addResourceLocator(
								ResourceLocatorTool.TYPE_TEXTURE,
								new SimpleResourceLocator(dirpath));
					} catch (URISyntaxException e) {
						logger
								.error("URI exception while setting texture path: "
										+ e);
					}

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
		createRoom(sceneData);
		fileOfCurrentScene = file;
		nodeToEntity = Collections
				.synchronizedMap(new HashMap<Node, VisualEntity>());
		for (Entity entity : sceneData.getSearchableEntities()) {
			if (entity instanceof VisualEntity) {
				nodeToEntity.put(((VisualEntity) entity).getNode(),
						(VisualEntity) entity);
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
		if (entity instanceof VisualEntity && !isNew) {
			((VisualEntity) entity).setNode((Node) sceneData.getRootNode()
					.getChild(entity.getEntityId().toString()));
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
		if (entity instanceof CableEntity) {
			cablingService.recreateCable((CableEntity) entity);
		}
		if (entity instanceof GPO) {
			((GPO) entity).setCablingService(cablingService);
		}
		if (entity instanceof ParentEntity) {
			for (Entity ent : ((ParentEntity) entity).getChildEntites()) {
				ServiceRegistry.getInstance().service(ent);
			}
		}
		// has to be the last step!!!
		if (!isNew && entity instanceof VisualEntity) {
			((VisualEntity) entity).loaded();
		}
		entity.setEventsService(eventsService);
		ServiceRegistry.getInstance().service(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#saveScene(org.eclipse.core.resources.IFile)
	 */
	public void saveScene(IFile file) {
		selectionService.clearSelection();
		fileOfCurrentScene = file;
		try {
			file.setContents(new ByteArrayInputStream(toByteArray(sceneData)),
					IFile.FORCE, null);
		} catch (CoreException e) {
			logger.error("Error while saving: " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#saveScene()
	 */
	public void saveScene() {
		selectionService.clearSelection();
		try {
			fileOfCurrentScene.setContents(new ByteArrayInputStream(
					toByteArray(sceneData)), IFile.FORCE, null);
		} catch (CoreException e) {
			logger.error("Error while saving: " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#addSceneDataChangedListener(org.rifidi.services.registry.core.scenedata.SceneDataChangedListener)
	 */
	@Override
	public void addSceneDataChangedListener(SceneDataChangedListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#removeSceneDataChangedListener(org.rifidi.services.registry.core.scenedata.SceneDataChangedListener)
	 */
	@Override
	public void removeSceneDataChangedListener(SceneDataChangedListener listener) {
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getRoomNode()
	 */
	@Override
	public Node getRoomNode() {
		return sceneData.getRootNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getRootNode()
	 */
	@Override
	public Node getRootNode() {
		return sceneData.getRoomNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getWidth()
	 */
	@Override
	public Integer getWidth() {
		return sceneData.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getWalls()
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
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getName()
	 */
	@Override
	public String getName() {
		return sceneData.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#setName(java.lang.String)
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
			classes.add(org.rifidi.designer.entities.CableEntity.class);
			classes.add(org.rifidi.emulator.tags.impl.C0G1Tag.class);
			classes.add(org.rifidi.emulator.tags.impl.C1G1Tag.class);
			classes.add(org.rifidi.emulator.tags.impl.C1G2Tag.class);
			classes.add(org.rifidi.emulator.tags.impl.RifidiTag.class);
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
	 * Helper method to create a square room TODO: this is just a temporary
	 * solution
	 * 
	 * @return node containing the room
	 */
	private void createRoom(SceneData sceneData) {
		Map<Direction, Node> walls;
		walls = new HashMap<Direction, Node>();
		Node room = new Node("rifidi_room_components");

		// create and texture the floor
		StaticPhysicsNode phys = sceneData.getPhysicsSpace().createStaticNode();
		Box box = new Box("rifidi_floor_wall", new Vector3f(0, -1.5f, 0),
				new Vector3f(sceneData.getWidth(), 0, sceneData.getWidth()));
		phys.setName(box.getName());
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture texture = TextureManager.loadTexture(Activator.class
				.getClassLoader().getResource("aluminium_0001_c.jpg"),
				Texture.MM_LINEAR, Texture.FM_LINEAR);
		texture.setWrap(Texture.WM_WRAP_S_WRAP_T);
		texture.setScale(new Vector3f(3, 3, 3));
		ts.setTexture(texture);
		box.setRenderState(ts);
		box.setRandomColors();
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		phys.attachChild(box);
		phys.setModelBound(new BoundingBox());
		phys.updateModelBound();
		phys.generatePhysicsGeometry();
		room.attachChild(phys);
		walls.put(Direction.DOWN, phys);

		// create and apply materialstate to the walls
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(.1f, .7f, .1f, .2f));

		// create west wall
		phys = sceneData.getPhysicsSpace().createStaticNode();
		box = new Box("rifidi_west_wall", new Vector3f(-.1f, sceneData
				.getHeight(), sceneData.getWidth() / 2), .1f, sceneData
				.getHeight(), sceneData.getWidth() / 2);
		phys.setName(box.getName());
		box.setRenderState(ms);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		phys.attachChild(box);
		phys.setModelBound(new BoundingBox());
		phys.updateModelBound();
		phys.generatePhysicsGeometry();
		room.attachChild(phys);
		walls.put(Direction.WEST, phys);

		// create east wall
		phys = sceneData.getPhysicsSpace().createStaticNode();
		box = new Box("rifidi_east_wall", new Vector3f(
				sceneData.getWidth() + .1f, sceneData.getHeight(), sceneData
						.getWidth() / 2), .1f, sceneData.getHeight(), sceneData
				.getWidth() / 2);
		phys.setName(box.getName());
		box.setRenderState(ms);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		phys.attachChild(box);
		phys.setModelBound(new BoundingBox());
		phys.updateModelBound();
		phys.generatePhysicsGeometry();
		room.attachChild(phys);
		walls.put(Direction.EAST, phys);

		// create south wall
		phys = sceneData.getPhysicsSpace().createStaticNode();
		box = new Box("rifidi_south_wall", new Vector3f(
				sceneData.getWidth() / 2, sceneData.getHeight(), sceneData
						.getWidth() + .1f), sceneData.getWidth() / 2, sceneData
				.getHeight(), .1f);
		phys.setName(box.getName());
		box.setRenderState(ms);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		phys.attachChild(box);
		phys.setModelBound(new BoundingBox());
		phys.updateModelBound();
		phys.generatePhysicsGeometry();
		room.attachChild(phys);
		walls.put(Direction.SOUTH, phys);

		// create north wall
		phys = sceneData.getPhysicsSpace().createStaticNode();
		box = new Box("rifidi_north_wall", new Vector3f(
				sceneData.getWidth() / 2, sceneData.getHeight(), -.1f),
				sceneData.getWidth() / 2, sceneData.getHeight(), .1f);
		phys.setName(box.getName());
		box.setRenderState(ms);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		phys.attachChild(box);
		phys.setModelBound(new BoundingBox());
		phys.updateModelBound();
		phys.generatePhysicsGeometry();
		room.attachChild(phys);
		walls.put(Direction.NORTH, phys);

		sceneData.setRoomNode(room);
		sceneData.setWalls(walls);
	}

	/**
	 * @param cablingService
	 *            the cablingService to set
	 */
	public void setCablingService(CablingService cablingService) {
		this.cablingService = cablingService;
	}

	/**
	 * @param cablingService
	 *            the cablingService to unset
	 */
	public void unsetCablingService(CablingService cablingService) {
		this.cablingService = null;
	}

	/**
	 * @param selectionService
	 *            the selectionService to set
	 */
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}

	/**
	 * @param selectionService
	 *            the selectionService to unset
	 */
	public void unsetSelectionService(SelectionService selectionService) {
		this.selectionService = null;
	}

	/**
	 * @param eventsService
	 *            the eventsService to set
	 */
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	/**
	 * @param eventsService
	 *            the eventsService to unset
	 */
	public void unsetEventsService(EventsService eventsService) {
		this.eventsService = null;
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

}
