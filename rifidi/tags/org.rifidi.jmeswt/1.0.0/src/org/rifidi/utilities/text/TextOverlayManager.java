/**
 * 
 */
package org.rifidi.utilities.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.opengl.GLCanvas;

import com.jme.renderer.Renderer;

/**
 * @author dan
 */
public class TextOverlayManager {
	private static TextOverlayManager instance;

	private Map<GLCanvas,TextOverlay> overlays;
//	private List<String> ids;
//	private int spacing = 1;
//	private boolean rightalign = false;
//	private boolean listdown = false;
//	private boolean dirty = false;
//	private Vector3f loc = new Vector3f(0,0,0);
//	private int lastWidth = -1;
//	private int lastHeight = -1;
//	private ColorRGBA textColor = ColorRGBA.darkGray;

	public static void create() {
		instance = new TextOverlayManager();
	}

	/**
	 * Default overlays are displayed in the bottom left and list upwards
	 */
	private TextOverlayManager() {
		overlays = new HashMap<GLCanvas,TextOverlay>();
	}

//	public static void createGroup( String groupname, Vector3f loc, boolean listdown ) {
//		instance = new TextOverlayManager(loc,listdown);
//	}

	public static TextOverlayManager getInstance() {
		return instance;
	}

	public void render( Renderer renderer ) {
//		boolean first = false;
//		if ( lastWidth != renderer.getWidth() || lastHeight != renderer.getHeight() ) {
//			first = true;
//			update(0);
//		}
//
//		if ( dirty || first )
//			reposition( renderer );

		for ( TextOverlay to : overlays.values() ) {
			to.render(renderer);
		}
	}

	/**
	 * @param tpf the time that's passed since the last update
	 */
	public void update(float tpf) {
		List<TextOverlay> values = new ArrayList<TextOverlay>(overlays.values());
		for ( TextOverlay msg : values )
			msg.update(tpf);
	}

//	public void removeOverlay( int id ) {
//		removeOverlay(ids.get(id));
//	}
//
//	public void removeOverlay( String id ) {
//		overlays.remove(id);
//		ids.remove(id);
//		dirty = true;
//	}

	public TextOverlay createOverlay( GLCanvas canvas ) throws OverlayAlreadyExistsException {
		if ( overlays.containsKey(canvas) ) {
			throw( new OverlayAlreadyExistsException() );
		} else {
			TextOverlay to = new TextOverlay(canvas);
			overlays.put( canvas, to );
			return to;
		}
	}

	public TextOverlay getOverlay( GLCanvas canvas ) throws NoOverlayForCanvasException {
		if ( !overlays.containsKey(canvas) )
			throw( new NoOverlayForCanvasException() );
		else return overlays.get(canvas);
	}

//	public TextOverlay getOverlay(String id) {
//		return overlays.get(id);
//	}
//
//	/**
//	 * @return the listdown
//	 */
//	public boolean isListdown() {
//		return listdown;
//	}
//
//	/**
//	 * @param listdown the listdown to set
//	 */
//	public void setListdown(boolean listdown) {
//		if ( this.listdown != listdown )
//			dirty = true;
//		this.listdown = listdown;
//	}
//
//	/**
//	 * @return the rightalign
//	 */
//	public boolean isRightalign() {
//		return rightalign;
//	}
//
//	/**
//	 * @param rightalign the rightalign to set
//	 */
//	public void setRightalign(boolean rightalign) {
//		if ( this.rightalign != rightalign )
//			dirty = true;
//		this.rightalign = rightalign;
//	}
//
//	/**
//	 * @param colorRGBA
//	 */
//	public void setTextColor(ColorRGBA colorRGBA) {
//		textColor = colorRGBA;
//		for ( TextOverlay to : overlays.values() )
//			to.getText().setTextColor(textColor.clone());
//	}
}