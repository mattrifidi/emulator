/**
 * 
 */
package org.rifidi.prototyper.map.view.layers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.rifidi.prototyper.map.controller.HotspotListener;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;
import org.rifidi.prototyper.map.view.figures.HotspotFigure;
import org.rifidi.prototyper.model.HotspotViewModel;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;
import org.rifidi.ui.common.registry.RegistryChangeListener;

/**
 * @author kyle
 * 
 */
public class HotspotLayer extends Layer implements HotspotListener,
		RegistryChangeListener {

	private Map<HotspotViewModel, HotspotFigure> hotspots;
	private XYLayout layout;
	private ReaderRegistryService readerRegistry;
	private static final Log logger = LogFactory.getLog(HotspotLayer.class);

	public HotspotLayer(Collection<HotspotViewModel> hotspotModels) {
		this.hotspots = new HashMap<HotspotViewModel, HotspotFigure>();
		layout = new XYLayout();
		setLayoutManager(layout);
		ViewModelSingleton.getInstance().addListener(this);
		for (HotspotViewModel hsModel : hotspotModels) {
			addHotspot(hsModel);
		}
		ServiceRegistry.getInstance().service(this);
	}

	private void addHotspot(HotspotViewModel hsModel) {
		UIReader uiReader = readerRegistry.getReaderByName(hsModel.getName());
		if (uiReader != null) {
			HotspotFigure fig = new HotspotFigure(hsModel, uiReader);
			this.hotspots.put(hsModel, fig);
			add(fig, fig.getConstraint());
		} else {
			// TODO: inconsistant state if we get here, since the ViewModel will
			// still have an entry in its hashmap for the hotspot
			logger.warn("Cannot find reader with name : " + hsModel.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.HotspotListener#hotspotAdded(org
	 * .rifidi.prototyper.model.HotspotViewModel)
	 */
	@Override
	public void hotspotAdded(HotspotViewModel model) {
		addHotspot(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.HotspotListener#hotspotDeleted(org
	 * .rifidi.prototyper.model.HotspotViewModel)
	 */
	@Override
	public void hotspotDeleted(HotspotViewModel model) {
		HotspotFigure fig = this.hotspots.remove(model);
		if (fig != null) {
			remove(fig);
		}

	}

	/**
	 * @param readerRegistry
	 *            the readerRegistry to set
	 */
	@Inject
	public void setReaderRegistry(final ReaderRegistryService readerRegistry) {
		this.readerRegistry = readerRegistry;
		readerRegistry.addListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.RegistryChangeListener#add(java.lang.Object
	 * )
	 */
	@Override
	public void add(Object event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.RegistryChangeListener#remove(java.lang
	 * .Object)
	 */
	@Override
	public void remove(Object event) {
		if (event instanceof UIReader) {
			UIReader reader = (UIReader) event;
			Set<HotspotViewModel> models = new HashSet<HotspotViewModel>(
					hotspots.keySet());
			for (HotspotViewModel model : models) {
				if (model.getName().equals(reader.getReaderName())) {
					ViewModelSingleton.getInstance().removeHotspot(
							model.hashString());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.RegistryChangeListener#update(java.lang
	 * .Object)
	 */
	@Override
	public void update(Object event) {

	}

}
