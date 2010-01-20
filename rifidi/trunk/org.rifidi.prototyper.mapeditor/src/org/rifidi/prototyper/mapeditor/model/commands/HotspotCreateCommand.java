/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotCreateCommand extends Command {

	private ElementSet<HotspotElement> hotspotElements;
	private CreateRequest request;
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
