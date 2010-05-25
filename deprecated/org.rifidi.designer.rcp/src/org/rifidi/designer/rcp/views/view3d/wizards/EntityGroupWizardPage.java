/*
 *  EntityGroupWizardPage.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * A wizard page that allows a user to create a new entitygroup.
 * 
 * @author Jochen Mader Nov 14, 2007
 * 
 */
public class EntityGroupWizardPage extends WizardPage {
	/**
	 * Container for the page.
	 */
	private Composite pageComposite;
	/**
	 * Widget for the group name.
	 */
	private Text nameText;
	/**
	 * The new group.
	 */
	private EntityGroup entityGroup;
	/**
	 * Reference to the entities service.
	 */
	private EntitiesService entitiesService;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 *            title of the page
	 * @param entityGroup
	 *            the new group
	 */
	public EntityGroupWizardPage(String pageName, EntityGroup entityGroup) {
		super(pageName);
		this.entityGroup = entityGroup;
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		parent.setLayout(new FillLayout());
		pageComposite = new Composite(parent, SWT.NONE);
		GridLayout pageLayout = new GridLayout();
		pageLayout.numColumns = 2;
		pageComposite.setLayout(pageLayout);
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.widthHint = 100;

		GridData grabTwo = new GridData();
		grabTwo.horizontalSpan = 2;
		grabTwo.grabExcessHorizontalSpace = true;
		grabTwo.widthHint = 200;

		Label label = new Label(pageComposite, SWT.None);
		label.setText("Name");
		nameText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(layoutData);
		nameText.setText(entityGroup.getName());
		nameText.addModifyListener(new ModifyListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				nameText.getText();
				entityGroup.setName(nameText.getText());
				if (nameText.getText().length() > 0
						&& !entitiesService.getEntityGroups().contains(
								entityGroup)) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}
			}

		});
		setControl(pageComposite);

	}

	/**
	 * @param entitiesService the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

}
