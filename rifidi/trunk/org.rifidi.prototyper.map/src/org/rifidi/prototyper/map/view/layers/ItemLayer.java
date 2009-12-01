/**
 * 
 */
package org.rifidi.prototyper.map.view.layers;

import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.rifidi.prototyper.map.controller.EditModeListener;
import org.rifidi.prototyper.map.controller.ItemListener;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;
import org.rifidi.prototyper.map.view.figures.ItemFigure;
import org.rifidi.prototyper.model.ItemViewModel;

/**
 * @author kyle
 * 
 */
public class ItemLayer extends Layer implements ItemListener, EditModeListener {

	private HashMap<String, ItemFigure> items;
	private XYLayout layout;
	private Log logger = LogFactory.getLog(ItemLayer.class);
	//private int layerID=0;

	public ItemLayer(Collection<ItemViewModel> models) {
		this.items = new HashMap<String, ItemFigure>();
		layout = new XYLayout();
		setLayoutManager(layout);
		ViewModelSingleton.getInstance().addListener(this);
		for (ItemViewModel item : models) {
			itemAdded(item);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.ItemListener#ItemAdded(org.rifidi
	 * .prototyper.map.view.Item)
	 */
	@Override
	public void itemAdded(ItemViewModel itemModel) {
		ItemFigure fig = new ItemFigure(itemModel);
		items.put(itemModel.getItemID(), fig);
		add(fig, fig.getConstraint());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.ItemListener#ItemDeleted(org.rifidi
	 * .prototyper.map.view.Item)
	 */
	@Override
	public void itemDeleted(ItemViewModel item) {
		ItemFigure fig = items.remove(item.getItemID());
		//fig.dispose();
		remove(fig);
		

	}

	public void dispose() {
		ViewModelSingleton.getInstance().removeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure,
	 * java.lang.Object, int)
	 */
	@Override
	public void add(IFigure figure, Object constraint, int index) {
		if (figure instanceof ItemFigure) {
			super.add(figure, constraint, index);
		} else {
			logger.warn("Only objects of type ItemFigure "
					+ "can be added to the item layer");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.EditModeListener#setEditMode(boolean
	 * )
	 */
	@Override
	public void setEditMode(boolean toggle) {
		if (toggle) {
			setVisible(false);
		} else {
			setVisible(true);
		}

	}

}
