/*
 *  SceneData.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.grouping.GroupContainer;
import org.rifidi.designer.entities.interfaces.IEntityObservable;
import org.rifidi.services.tags.impl.RifidiTag;

import com.jme.input.InputHandler;
import com.jme.scene.Node;
import com.jmex.physics.PhysicsSpace;

/**
 * This is basically a wrapper object. Contains the root of the entity graph
 * and, on save, a byte array containing the serialized version of the
 * scenegraph
 * 
 * @author Jochen Mader Oct 8, 2007
 * 
 */

@XmlRootElement
public class SceneData implements IAdaptable, IWorkbenchAdapter,
		IEntityObservable {

	/**
	 * Positions for the walls.
	 * 
	 * @author dan
	 */
	public enum Direction {
		NORTH, SOUTH, EAST, WEST, DOWN
	}

	/** All the entities in the scene. */
	private List<Entity> entities;
	/** Byte form of the scenegraph. */
	private byte[] nodeBytes;
	/** Root of the scene graph. */
	private Node rootNode;
	/** The room (stored separate for renderqueue purposes). */
	private Node roomNode;
	/** Counter for entity ids */
	private Integer idCounter = 0;
	/** List of entity groups (this one is wrapped by entityGroups). */
	private List<EntityGroup> rawEntityGroups;
	/** List of entity groups. */
	private List<EntityGroup> entityGroups;
	/** A descriptive name for this scene. */
	private String name;
	/** Default group that contains all newly added entities. */
	private EntityGroup defaultGroup;
	/** Default group that contains all produced added entities. */
	private EntityGroup producedEntities;
	/** Group for cables. */
	private EntityGroup cableGroup;
	/** All currently taken names of entites */
	private List<String> entityNames;
	/** The currently used physics space. */
	private PhysicsSpace physicsSpace;
	/** The currently uised collision handler. */
	private InputHandler collisionHandler;
	/** This map conatins the 4 walls that surround the scene. */
	private Map<Direction, Node> walls;
	/** Container for entity groups. */
	private GroupContainer groupedComponentsContainer;
	/** Synchronized list of entites. */
	private List<Entity> syncedEntities;
	/** Current SWT display. */
	private Display display;
	/** Id of the floorplan to use. */
	private String floorId;
	/** Only used to store tags from the tagregistry to an xml file. */
	private List<RifidiTag> tags = new ArrayList<RifidiTag>();

	/**
	 * @return the nodeBytes
	 */
	public byte[] getNodeBytes() {
		return nodeBytes;
	}

	/**
	 * @param nodeBytes
	 *            the nodeBytes to set
	 */
	public void setNodeBytes(final byte[] nodeBytes) {
		this.nodeBytes = nodeBytes;
	}

	/**
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public SceneData() {
		entities = new ArrayList<Entity>();
		syncedEntities = Collections.synchronizedList(entities);
		entityNames = new ArrayList<String>();
		rawEntityGroups = new ArrayList<EntityGroup>();
		entityGroups = new WritableList(rawEntityGroups, EntityGroup.class);
		defaultGroup = new EntityGroup();
		defaultGroup.setName("Ungrouped Components");
		defaultGroup.setSceneData(this);
		defaultGroup.setLocked(true);
		producedEntities = new EntityGroup();
		producedEntities.setName("Generated Components");
		producedEntities.setSceneData(this);
		producedEntities.setLocked(true);
		cableGroup = new EntityGroup();
		cableGroup.setName("Cables");
		cableGroup.setSceneData(this);
		cableGroup.setLocked(true);
		rootNode = new Node("root");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object o) {
		groupedComponentsContainer = new GroupContainer("Grouped Components",
				this);
		Object[] ret = new Object[] { defaultGroup, groupedComponentsContainer,
				producedEntities };
		return ret;
	}

	public GroupContainer getGroupedComponentsContainer() {
		return groupedComponentsContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object
	 * )
	 */
	public ImageDescriptor getImageDescriptor(final Object object) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
	 */
	public String getLabel(final Object o) {
		return "Entities Root";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	public Object getParent(final Object o) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(final Class adapter) {
		if (adapter.equals(IWorkbenchAdapter.class)) {
			return this;
		}
		return null;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	private List<Entity> searchableEntities;
	private int oldHash = 0;

	public List<Entity> getSearchableEntities() {
		if (searchableEntities == null || oldHash != entities.hashCode()) {
			searchableEntities = Collections
					.unmodifiableList(new ArrayList<Entity>(entities));
			oldHash = entities.hashCode();
		}
		return searchableEntities;
	}

	public List<Entity> getSyncedEntities() {
		return syncedEntities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<Entity> entities) {
		this.entities = entities;
		syncedEntities = Collections.synchronizedList(entities);
	}

	/**
	 * @return the rootNode
	 */
	@XmlTransient
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * @param rootNode
	 *            the rootNode to set
	 */
	public void setRootNode(final Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * The next available ID for an entity.
	 * 
	 * @return
	 */
	public synchronized Integer getNextID() {
		return ++idCounter;
	}

	/**
	 * @return the idCounter
	 */
	public Integer getIdCounter() {
		return idCounter;
	}

	/**
	 * @param idCounter
	 *            the idCounter to set
	 */
	public void setIdCounter(final Integer idCounter) {
		this.idCounter = idCounter;
	}

	/**
	 * @return the entityGroups
	 */
	public List<EntityGroup> getEntityGroups() {
		return entityGroups;
	}

	/**
	 * @param entityGroups
	 *            the entityGroups to set
	 */
	public void setEntityGroups(List<EntityGroup> entityGroups) {
		this.entityGroups = entityGroups;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the defaultGroup
	 */
	public EntityGroup getDefaultGroup() {
		return defaultGroup;
	}

	/**
	 * @param defaultGroup
	 *            the defaultGroup to set
	 */
	public void setDefaultGroup(final EntityGroup defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	/**
	 * @return a list of all the entity names
	 */
	public List<String> getEntityNames() {
		return entityNames;
	}

	/**
	 * @return the producedEntities
	 */
	public EntityGroup getProducedEntities() {
		return producedEntities;
	}

	@XmlTransient
	public Node getRoomNode() {
		return roomNode;
	}

	public void setRoomNode(final Node roomNode) {
		this.roomNode = roomNode;
	}

	/**
	 * @return the physicsSpace
	 */
	@XmlTransient
	public PhysicsSpace getPhysicsSpace() {
		return physicsSpace;
	}

	/**
	 * @param physicsSpace
	 *            the physicsSpace to set
	 */
	public void setPhysicsSpace(final PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	/**
	 * @return the collisionHandler
	 */
	@XmlTransient
	public InputHandler getCollisionHandler() {
		return collisionHandler;
	}

	/**
	 * @param collisionHandler
	 *            the collisionHandler to set
	 */
	public void setCollisionHandler(final InputHandler collisionHandler) {
		this.collisionHandler = collisionHandler;
	}

	/**
	 * Get the walls of the room.
	 * 
	 * @return the walls
	 */
	@XmlTransient
	public Map<Direction, Node> getWalls() {
		return walls;
	}

	/**
	 * Set the walls of the room.
	 * 
	 * @param walls
	 *            the walls to set
	 */
	public void setWalls(final Map<Direction, Node> walls) {
		this.walls = walls;
	}

	/**
	 * @return the cableGroup
	 */
	public EntityGroup getCableGroup() {
		return this.cableGroup;
	}

	/**
	 * @param cableGroup
	 *            the cableGroup to set
	 */
	public void setCableGroup(EntityGroup cableGroup) {
		this.cableGroup = cableGroup;
	}

	/**
	 * @return the display
	 */
	public Display getDisplay() {
		return this.display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	@XmlTransient
	public void setDisplay(Display display) {
		this.display = display;
	}

	/**
	 * @return the floorId
	 */
	public String getFloorId() {
		return this.floorId;
	}

	/**
	 * @param floorId
	 *            the floorId to set
	 */
	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	/**
	 * @return the tags
	 */
	public List<RifidiTag> getTags() {
		return this.tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<RifidiTag> tags) {
		this.tags = tags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.entities.interfaces.IEntityObservable#
	 * addListChangeListener
	 * (org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	@Override
	public void addListChangeListener(IListChangeListener changeListener) {
		((WritableList) entityGroups).addListChangeListener(changeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.entities.interfaces.IEntityObservable#
	 * removeListChangeListener
	 * (org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	@Override
	public void removeListChangeListener(IListChangeListener changeListener) {
		((WritableList) entityGroups).removeListChangeListener(changeListener);
	}
}