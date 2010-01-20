/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import org.eclipse.gef.requests.CreationFactory;
import org.rifidi.emulator.readerview.support.ReaderDNDSupport;
import org.rifidi.prototyper.items.model.ItemDNDSupport;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.service.ItemService;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;
import org.rifidi.prototyper.mapeditor.model.ItemElement;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * Creates new Item and Hotspot Elements from a drag and drop operation.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class DNDElementFactory implements CreationFactory {

	public final static String ITEM = "item", READER = "reader";
	private String text;
	private ItemService itemService;
	private ReaderRegistryService readerService;

	public DNDElementFactory(ItemService itemService,
			ReaderRegistryService readerService) {
		this.itemService = itemService;
		this.readerService = readerService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	@Override
	public Object getNewObject() {
		if (text == null) {
			return null;
		}
		if (ItemDNDSupport.isItem(text)) {
			int id = ItemDNDSupport.getItemID(text);
			ItemModel model = itemService.getItem(id);
			ItemElement item = new ItemElement(model);
			return item;
		} else if (ReaderDNDSupport.isReaderDND(text)) {
			String readerID = ReaderDNDSupport.getReaderID(text);
			Integer antennaID = ReaderDNDSupport.getAntennaID(text);
			UIReader reader = readerService.getReaderByName(readerID);
			HotspotElement hotspot = new HotspotElement(reader
					.getGeneralReaderPropertyHolder(), antennaID);
			return hotspot;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	@Override
	public Object getObjectType() {
		if (text == null) {
			return null;
		}
		if (ItemDNDSupport.isItem(text)) {
			return ITEM;
		} else if (ReaderDNDSupport.isReaderDND(text)) {
			return READER;
		}
		return null;
	}

	/**
	 * @param s
	 */
	public void setText(String s) {
		this.text = s;

	}

}
