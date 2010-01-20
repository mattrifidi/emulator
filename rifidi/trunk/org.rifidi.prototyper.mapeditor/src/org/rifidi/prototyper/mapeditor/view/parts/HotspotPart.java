/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;
import org.rifidi.prototyper.mapeditor.model.MapModel;
import org.rifidi.prototyper.mapeditor.view.figures.HotspotFigure;
import org.rifidi.prototyper.mapeditor.view.parts.policies.ItemComponentEditPolicy;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;
import org.rifidi.ui.console.service.ConsoleService;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotPart extends AbstractMapPart<HotspotElement> {

	final Set<ItemPart> containedItems = new HashSet<ItemPart>();
	private ReaderRegistryService readerService;
	/** A service to log output to the console */
	private ConsoleService service;
	/** The name of the console to display collisions at */
	private final String CONSOLE_NAME = "Collisions";

	/**
	 * 
	 */
	public HotspotPart() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#createEditPolicies
	 * ()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ItemComponentEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractEditPart#setModel(java.lang.Object)
	 */
	@Override
	public void setModel(Object model) {
		super.setModel(model);
		if (!getModelElement().hasBeenInitialized()) {
			getModelElement().initialize(readerService);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		MapModel map = ((MapPart) getParent().getParent()).getModelElement();
		return new HotspotFigure(getModelElement(), map.getFloorplan()
				.getScale());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		HotspotElement model = getModelElement();
		HotspotFigure fig = (HotspotFigure) getFigure();
		fig.refreshImage();
		Rectangle bounds = new Rectangle(model.getLocation(), model.getSize());

		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
	}

	public void manageCollision(ItemPart item) {
		Rectangle bounds = new Rectangle(item.getModelElement().getLocation(),
				item.getModelElement().getDimension());
		if (getFigure().intersects(bounds)) {
			if (!containedItems.contains(item)) {
				handleItemSeen(item);
			}
		} else {
			if (containedItems.contains(item)) {
				handleItemUnseen(item);
			}
		}
	}

	private void handleItemSeen(ItemPart item) {
		containedItems.add(item);
		getModelElement().handleTagSeen(item.getModelElement().getTag());
		service.write(CONSOLE_NAME, "Item: " + item.getHoverText()
				+ " arrived at " + this.getHoverText() + "\n");
	}

	private void handleItemUnseen(ItemPart item) {
		containedItems.remove(item);
		getModelElement().handleTagUnseen(item.getModelElement().getTag());
		service.write(CONSOLE_NAME, "Item: " + item.getHoverText()
				+ " departed from " + this.getHoverText() + "\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#propertyChange
	 * (java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		refreshVisuals();
	}

	/**
	 * Used to inject the console service
	 */
	@Inject
	public void setConsoleService(ConsoleService service) {
		this.service = service;
		this.service.createConsole(CONSOLE_NAME);
	}

	/**
	 * Used to inject the console service
	 */
	@Inject
	public void setReaderServiceService(ReaderRegistryService service) {
		this.readerService = service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	@Override
	public String toString() {
		return "Hotspot Part " + getModelElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#getHoverText()
	 */
	@Override
	public String getHoverText() {
		return "Hotspot: " + getModelElement().getReaderName() + " ant: "
				+ getModelElement().getAntennaID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		// TODO: Does not work. Needs to make sure that items are seen on
		// antennas when a hotspot is dropped.
		List<ItemPart> itemParts = ((MapPart) getParent().getParent())
				.getItemLayerPart().getItemParts();
		for (ItemPart itemPart : itemParts) {
			manageCollision(itemPart);
		}
	}
}
