/*
 *  SceneDataServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.scenedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.rifidi.designer.entities.CableEntity;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.interfaces.GPO;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.ParentEntity;
import org.rifidi.designer.entities.interfaces.RifidiEntity;
import org.rifidi.designer.entities.placement.BitMap;
import org.rifidi.designer.library.EntityLibraryRegistry;
import org.rifidi.designer.rcp.Activator;
import org.rifidi.designer.rcp.views.view3d.View3D.Direction;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.selection.SelectionServiceImpl;
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
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

/**
 * This implementation loads the ScenDate from an IFile.
 * 
 * @author Jochen Mader Jan 20, 2008
 * 
 */
public class SceneDataServiceImpl implements SceneDataService {
	/**
	 * Logger for thius class.
	 */
	private static Log logger = LogFactory.getLog(SceneDataServiceImpl.class);
	/**
	 * the currently loaded scene.
	 */
	private SceneData currentSceneData;
	/**
	 * the file the scene was loaded from.
	 */
	private IFile fileOfCurrentScene;
	/**
	 * objects listening for scenedata events.
	 */
	private List<SceneDataChangedListener> listeners;
	/**
	 * Reference to the cabling service.
	 */
	private CablingService cablingService;
	/**
	 * Reference to the selection service.
	 */
	private SelectionService selectionService;

	/**
	 * Constructor.
	 */
	public SceneDataServiceImpl() {
		listeners = new ArrayList<SceneDataChangedListener>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#loadScene(org.eclipse.core.resources.IFile)
	 */
	@SuppressWarnings("unchecked")
	public void loadScene(IFile file) {
		SceneData sceneData;
		TextureManager.clearCache();
		// invalidate the current sceneData
		for (SceneDataChangedListener listener : listeners) {
			listener.destroySceneData(this.currentSceneData);
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
						initEntit(entity, sceneData);
					}
					for (Entity entity : sceneData.getProducedEntities()
							.getEntities()) {
						ServiceRegistry.getInstance().service(entity);
						sceneData.getEntityNames().add(entity.getName());
						initEntit(entity, sceneData);
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
		currentSceneData = sceneData;
		fileOfCurrentScene = file;
		for (SceneDataChangedListener listener : listeners) {
			listener.sceneDataChanged(sceneData);
		}
	}

	/**
	 * Initialze the entity TODO: REDUNDANT!!!
	 * 
	 * @param entity
	 * @param sceneData
	 */
	private void initEntit(Entity entity, SceneData sceneData) {
		// reassociate the entities with their nodes
		if (entity instanceof VisualEntity) {
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
		if (entity instanceof VisualEntity) {
			((VisualEntity) entity).loaded();
		}
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
			file.setContents(new ByteArrayInputStream(
					toByteArray(currentSceneData)), IFile.FORCE, null);
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
					toByteArray(currentSceneData)), IFile.FORCE, null);
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
		return currentSceneData.getRootNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getRootNode()
	 */
	@Override
	public Node getRootNode() {
		return currentSceneData.getRoomNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getWidth()
	 */
	@Override
	public Integer getWidth() {
		return currentSceneData.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getWalls()
	 */
	@Override
	public Map<Direction, Node> getWalls() {
		return currentSceneData.getWalls();
	}

	/**
	 * Get the currently loaded SceneData.
	 * 
	 * @return currently loaded SceneData or null
	 */
	public SceneData getCurrentSceneData() {
		return currentSceneData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#getName()
	 */
	@Override
	public String getName() {
		return currentSceneData.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataService#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		currentSceneData.setName(name);
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

}
