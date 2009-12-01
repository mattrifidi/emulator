/**
 * 
 */
package org.rifidi.prototyper.map.view.mousehandler;

import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.MouseEvent;
import org.rifidi.prototyper.map.collision.CollisionManager;
import org.rifidi.prototyper.map.controller.EditModeListener;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;
import org.rifidi.prototyper.map.view.layers.HotspotLayer;
import org.rifidi.prototyper.map.view.layers.ItemLayer;

/**
 * @author kyle
 * 
 */
public class MapViewMouseHandler extends AbstractMapViewMouseHandlerState
		implements EditModeListener {

	private AbstractMapViewMouseHandlerState currentHandler;
	private EditModeMapViewMouseHandler editModeHandler;
	private NormalModeMapViewMouseHandler normalModeHandler;

	public MapViewMouseHandler(CollisionManager collistionManager,
			ItemLayer itemLayer, HotspotLayer hsLayer) {
		this.editModeHandler = new EditModeMapViewMouseHandler(hsLayer);
		this.normalModeHandler = new NormalModeMapViewMouseHandler(
				collistionManager, itemLayer);
		this.currentHandler = normalModeHandler;
		ViewModelSingleton.getInstance().addListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mousePressed(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		this.currentHandler.mousePressed(me);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent
	 * )
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		this.currentHandler.mouseReleased(me);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d
	 * .MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		this.currentHandler.mouseDragged(me);

	}
	

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseDoubleClicked(MouseEvent me) {
		this.currentHandler.mouseDoubleClicked(me);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#mouseEntered(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent me) {
		this.currentHandler.mouseEntered(me);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#mouseExited(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent me) {
		this.currentHandler.mouseExited(me);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#mouseMoved(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent me) {
		this.currentHandler.mouseMoved(me);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#keyPressed(org.eclipse.draw2d.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		this.currentHandler.keyPressed(ke);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#keyReleased(org.eclipse.draw2d.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent ke) {
		this.currentHandler.keyReleased(ke);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState#mouseHover(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseHover(MouseEvent me) {
		this.currentHandler.mouseHover(me);
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
			this.currentHandler = editModeHandler;
		} else {
			this.currentHandler = normalModeHandler;
		}

	}

}
