/*
 *  CablingServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.cabling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rifidi.designer.entities.CableEntity;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.GPO;
import org.rifidi.designer.services.core.cabling.CableChangeListener;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.scenedata.SceneDataChangedListener;
import org.rifidi.designer.services.core.scenedata.SceneDataService;
import org.rifidi.services.annotations.Inject;

/**
 * Base implementation.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
public class CablingServiceImpl implements CablingService,
		SceneDataChangedListener {
	/**
	 * Reference to the currently loaded scenedata.
	 */
	private SceneData sceneData;
	/**
	 * List of registered cables.
	 */
	private List<CableEntity> cableList;

	/**
	 * Reference to the entities service.
	 */
	private EntitiesService entitiesService;

	/**
	 * List of cable change listeners.
	 */
	private List<CableChangeListener> cableChangeListeners;
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;

	/**
	 * Constructor.
	 */
	public CablingServiceImpl() {
		cableList = Collections.synchronizedList(new ArrayList<CableEntity>());
		cableChangeListeners = new ArrayList<CableChangeListener>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#createCable(org.rifidi.designer.entities.CableEntity)
	 */
	@Override
	public void createCable(CableEntity cableEntity) {
		entitiesService.addEntity(cableEntity, false);
		cableList.add(cableEntity);
		triggerCableListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#recreateCable(org.rifidi.designer.entities.CableEntity)
	 */
	@Override
	public void recreateCable(CableEntity cableEntity) {
		cableList.add(cableEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#destroyCable(org.rifidi.designer.entities.CableEntity)
	 */
	@Override
	public void destroyCable(CableEntity cableEntity) {
		List<Entity> del = new ArrayList<Entity>();
		del.add(cableEntity);
		entitiesService.deleteEntities(del);
		cableList.remove(cableEntity);
		triggerCableListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#cableExists(org.rifidi.designer.entities.CableEntity)
	 */
	@Override
	public boolean cableExists(CableEntity cableEntity) {
		return cableList.contains(cableEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#setHigh(org.rifidi.designer.entities.interfaces.GPO,
	 *      int)
	 */
	@Override
	public void setHigh(GPO source, int port) {
		List<CableEntity> activeCables = new ArrayList<CableEntity>();
		synchronized (cableList) {
			for (CableEntity cable : cableList) {
				if (cable.getGpo().equals(source)) {
					activeCables.add(cable);
				}
			}
		}
		for (CableEntity act : activeCables) {
			((GPI) act.getGpi()).setHigh(port);
		}
	}

	@Override
	public void setLow(GPO source, int port) {
		List<CableEntity> activeCables = new ArrayList<CableEntity>();
		synchronized (cableList) {
			for (CableEntity cable : cableList) {
				if (cable.getGpo().equals(source)
						&& cable.getSourcePort() == port) {
					activeCables.add(cable);
				}
			}
		}
		for (CableEntity act : activeCables) {
			((GPI) act.getGpi()).setLow(act.getTargetPort());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#getSources(org.rifidi.designer.entities.interfaces.GPI)
	 */
	@Override
	public List<CableEntity> getSources(GPI gpi) {
		List<CableEntity> sources = new ArrayList<CableEntity>();
		synchronized (cableList) {
			for (CableEntity cableEntity : cableList) {
				if (cableEntity.getGpi().equals(gpi)) {
					sources.add(cableEntity);
				}
			}
		}
		return sources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#getTargets(org.rifidi.designer.entities.interfaces.GPO)
	 */
	@Override
	public List<CableEntity> getTargets(GPO gpo) {
		List<CableEntity> targets = new ArrayList<CableEntity>();
		synchronized (cableList) {
			for (CableEntity cableEntity : cableList) {
				if (cableEntity.getGpo().equals(gpo)) {
					targets.add(cableEntity);
				}
			}
		}
		return targets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		cableList.clear();
		cableChangeListeners.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
		this.sceneData = sceneData;
		for (Entity entity : sceneData.getCableGroup().getEntities()) {
			CableEntity cable = (CableEntity) entity;
			if (!cableList.contains(cable)) {
				cableList.add(cable);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#addCableChangeListener(org.rifidi.services.registry.core.cabling.CableChangeListener)
	 */
	@Override
	public void addCableChangeListener(CableChangeListener cableChangeListener) {
		cableChangeListeners.add(cableChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.cabling.CablingService#removeCableChangeListener(org.rifidi.services.registry.core.cabling.CableChangeListener)
	 */
	@Override
	public void removeCableChangeListener(
			CableChangeListener cableChangeListener) {
		cableChangeListeners.remove(cableChangeListener);
	}

	/**
	 * Inform registered listeners about a change.
	 */
	private void triggerCableListeners() {
		for (CableChangeListener cableChangeListener : cableChangeListeners) {
			cableChangeListener.cableChanged();
		}
	}

	/**
	 * @param entitiesService
	 *            the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to unset
	 */
	public void unsetSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = null;
	}
	
}
