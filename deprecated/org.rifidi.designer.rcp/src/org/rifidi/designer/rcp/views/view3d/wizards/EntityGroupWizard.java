/*
 *  EntityGroupWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.wizards;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Wizard for creating a new entitygroup.
 * 
 * @see EntityGroup
 * 
 * @author Jochen Mader Nov 14, 2007
 * 
 */
public class EntityGroupWizard extends Wizard {
	/**
	 * Wizard page for this wizard.
	 */
	private EntityGroupWizardPage entityGroupWizardPage;
	/**
	 * The newly created entity group.
	 */
	private EntityGroup entityGroup;
	/**
	 * List of entities to be added to the new group.
	 */
	private List<Entity> entities;
	/**
	 * Reference to the entities service.
	 */
	private EntitiesService entitiesService;
	/**
	 * Reference to the finder service.
	 */
	private FinderService finderService;

	/**
	 * Create a group that should be filled with some entities.
	 * 
	 * @param entities
	 */
	public EntityGroupWizard(List<Entity> entities) {
		super();
		this.entityGroup = new EntityGroup();
		entityGroup.setName("new_group");
		ServiceRegistry.getInstance().service(this);
		int count = 1;
		while (finderService.entityGroupExists(entityGroup)) {
			entityGroup.setName("new_group" + count);
			count++;
		}
		this.entities = entities;
	}

	/**
	 * Create an empty group.
	 */
	public EntityGroupWizard() {
		super();
		ServiceRegistry.getInstance().service(this);
		this.entityGroup = new EntityGroup();
		entityGroup.setName("new_group");
		int count = 1;
		while (finderService.entityGroupExists(entityGroup)) {
			entityGroup.setName("new_group" + count);
			count++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		entityGroupWizardPage = new EntityGroupWizardPage(
				"Create a new Entity Group", entityGroup);
		addPage(entityGroupWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (entities != null) {
			entityGroup.setEntities(entities);
		}
		entitiesService.addEntityGroup(entityGroup);
		return true;
	}

	/**
	 * @param entitiesService the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

	/**
	 * @param finderService the finderService to set
	 */
	@Inject
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}

}
