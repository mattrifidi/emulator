/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.rifidi.prototyper.items.service.ItemService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * This class listens to DND events and handles them. 
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class TextTransferDropTargetListener extends
		AbstractTransferDropTargetListener {

	private ItemService itemService;
	private ReaderRegistryService readerService;
	private DNDElementFactory factory;
	private final static Log logger = LogFactory
			.getLog(TextTransferDropTargetListener.class);

	/**
	 * @param viewer
	 */
	public TextTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer);
		ServiceRegistry.getInstance().service(this);
	}

	@Override
	protected Request createTargetRequest() {
		CreateRequest request = new CreateRequest();
		request.setFactory(factory);
		return request;
	}

	@Override
	protected void updateTargetRequest() {
		((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
	 */
	@Override
	protected void handleDrop() {
		String s = ((String) getCurrentEvent().data);
		factory.setText(s);
		super.handleDrop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#getTransfer()
	 */
	@Override
	public Transfer getTransfer() {
		return TextTransfer.getInstance();
	}

	@Inject
	public void setItemSerivce(ItemService service) {
		logger.debug("ItemService set");
		this.itemService = service;
		if (readerService != null) {
			this.factory = new DNDElementFactory(service, readerService);
		}
	}

	@Inject
	public void setReaderService(ReaderRegistryService service) {
		logger.debug("ReaderService set");
		this.readerService = service;
		if (itemService != null) {
			this.factory = new DNDElementFactory(itemService, service);
		}
	}

}
