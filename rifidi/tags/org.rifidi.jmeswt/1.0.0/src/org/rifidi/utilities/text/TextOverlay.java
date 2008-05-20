/**
 * 
 */
package org.rifidi.utilities.text;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.opengl.GLCanvas;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;

/**
 * @author dan
 */
public class TextOverlay {
//	private Map<String,TextOverlayElement> overlays;
	private Map<String,TextOverlayGroup> overlayGroups;
//	private List<String> ids;

	private GLCanvas canvas;

	private int lastWidth = -1;
	private int lastHeight = -1;

	/**
	 * Default overlays are displayed in the bottom left and list upwards
	 */
	public TextOverlay( GLCanvas canvas ) {
		this.canvas = canvas;
		clear();
	}

	/**
	 * Resets the listing of overlay groups in this overlay
	 */
	public void clear() {
		overlayGroups = new HashMap<String,TextOverlayGroup>();
	}

	/**
	 * Creates a new group with the specified name in the overlay
	 * @throws GroupAlreadyExistsException 
	 */
	public TextOverlayGroup createGroup( String id ) throws GroupAlreadyExistsException {
		if ( overlayGroups.containsKey(id) ) {
			throw( new GroupAlreadyExistsException(id) );
		} else {
			TextOverlayGroup group = new TextOverlayGroup();
			overlayGroups.put(id, group);
			return group;
		}
	}

	/**
	 * @param string the message to display
	 * @throws GroupDoesntExistException 
	 */
	public TextOverlayElement postMessage( String group, String message, float ttl ) throws GroupDoesntExistException {
		if ( !overlayGroups.containsKey(group) ) {
			throw( new GroupDoesntExistException(group) );
		} else {
			return overlayGroups.get(group).postMessage( message, ttl );
		}
	}

	/**
	 * Draws this overlay on the given renderer
	 * @param renderer the renderer to render to
	 */
	public void render( Renderer renderer ) {
//		boolean first = false;
//		if ( lastWidth != renderer.getWidth() || lastHeight != renderer.getHeight() ) {
//			first = true;
//			update(0);
//		}
//
//		if ( dirty || first )
//			reposition( renderer );
//
//		for ( String id : ids ) {
//			overlays.get(id).draw(renderer);
//		}

		for ( TextOverlayGroup group : overlayGroups.values() ) {
			group.render(renderer);
		}
	}

	/**
	 * @param tpf the time that's passed since the last update
	 */
	public void update( float tpf ) {
		for ( TextOverlayGroup group : overlayGroups.values() ) {
			group.update( tpf );
		}
	}
}