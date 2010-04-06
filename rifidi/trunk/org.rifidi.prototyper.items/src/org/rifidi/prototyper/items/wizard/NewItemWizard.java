/**
 * 
 */
package org.rifidi.prototyper.items.wizard;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.model.ItemType;
import org.rifidi.prototyper.items.service.DuplicateItemException;
import org.rifidi.prototyper.items.view.ItemModelProviderSingleton;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.factory.TagCreationPattern;
import org.rifidi.tags.factory.TagFactory;
import org.rifidi.tags.id.TagType;
import org.rifidi.tags.impl.RifidiTag;

/**
 * A wizard that creates a new item.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NewItemWizard extends Wizard implements INewWizard {

	/** The name of the item */
	protected String itemName;
	/** The number of items to created */
	protected Integer number;
	/** The type of item to created */
	protected ItemType itemType;
	/** The Tag Type */
	protected TagType tagType;
	/** GEN 1 or Gen 2 */
	protected TagGen tagGen;
	/** A prefix if ncessary */
	protected String tagPrefix;
	/** The logger for this class */
	private final static Log logger = LogFactory.getLog(NewItemWizard.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		for (int i = 0; i < number; i++) {
			TagCreationPattern pattern = new TagCreationPattern();
			pattern.setNumberOfTags(1);
			pattern.setTagGeneration(this.tagGen);
			pattern.setTagType(this.tagType);
			if (TagType.CustomEPC96.equals(this.tagType)) {
				pattern.setPrefix(this.tagPrefix);
			}

			ArrayList<RifidiTag> tags = TagFactory.generateTags(pattern);

			ItemModel itemModel = new ItemModel();
			String itemName = this.itemName;
			if (i > 0) {
				itemName = itemName + "_" + i;
			}
			itemModel.setName(itemName);
			itemModel.setTag(tags.get(0));
			itemModel.setType(this.itemType);
			try {
				ItemModelProviderSingleton.getModelProvider().createItem(
						itemModel);
			} catch (DuplicateItemException ex) {
				logger.error("DuplicateItemException: ", ex);
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(new NewItemWizardTypeChooserPage("Type Chooser"));
		addPage(new NewItemWizardPage("New Item"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return (itemName != null) && (number != null) && (itemType != null)
				&& (tagType != null) && (tagGen != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

}
