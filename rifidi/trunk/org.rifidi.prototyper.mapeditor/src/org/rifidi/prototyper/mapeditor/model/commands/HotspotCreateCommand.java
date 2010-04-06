/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;

/**
 * This command creates hotspots
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotCreateCommand extends Command {

	/** The list of current hotspots */
	private ElementSet<HotspotElement> hotspotElements;
	/** The request that creates the hotspot */
	private CreateRequest request;
	/** The hotspot to create */
	private HotspotElement hotspot;

	/**
	 * @param itemElements
	 */
	public HotspotCreateCommand(ElementSet<HotspotElement> itemElements,
			CreateRequest request) {
		super();
		this.hotspotElements = itemElements;
		this.request = request;
		hotspot = (HotspotElement) request.getNewObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return !hotspotElements.contains(hotspot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		hotspot.setLocation(request.getLocation());
		hotspotElements.addElement(hotspot);
		super.execute();
	}

}
