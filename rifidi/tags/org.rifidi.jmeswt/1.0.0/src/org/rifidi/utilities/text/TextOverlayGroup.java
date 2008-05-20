package org.rifidi.utilities.text;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;

public class TextOverlayGroup {
	private int spacing = 1;
	private boolean listdown = false;
	private boolean rightalign = false;
	private Vector3f loc = new Vector3f(0,0,0);
	private ColorRGBA textColor = ColorRGBA.darkGray;

	private List<TextOverlayElement> overlayElements;

	private int lastWidth, lastHeight;
	private boolean needsUpdate = false;

	public TextOverlayGroup() {
		overlayElements = new ArrayList<TextOverlayElement>();
	}

	public TextOverlayElement postMessage( String message, float ttl ) {
		TextOverlayElement element = new TextOverlayElement(message,ttl);
		element.setTextColor(textColor);
		overlayElements.add( element );
		needsUpdate = true;
		return element;
	}

	/**
	 * Updates all elements in this overlay group
	 * @param dt the amount of time elapsed since last update
	 */
	public void update( float dt ) {
		List<TextOverlayElement> unmodifiedList;
		unmodifiedList = new ArrayList<TextOverlayElement>(overlayElements);
		for ( TextOverlayElement element : unmodifiedList ) {
			element.update(dt);
			if ( element.getTTL() == 0f ) {
				overlayElements.remove(element);
				needsUpdate = true;
			}
		}
	}

	/**
	 * Renders the overlay group to the renderer
	 * @param renderer the renderer to render to
	 */
	public void render( Renderer renderer ) {
		if ( needsUpdate )
			reposition(renderer);

		for ( TextOverlayElement element : overlayElements ) {
			element.render(renderer);
		}
	}

	/**
	 * Repositions all the overlay elements after one is added or removed
	 * @param renderer
	 */
	public void reposition( Renderer renderer ) {
		int voffset = 0;
		int hoffset = 0;
		for ( TextOverlayElement element : overlayElements ) {
			lastWidth = renderer.getWidth();
			lastHeight = renderer.getHeight();
			if ( listdown && voffset == 0)
				voffset -= element.getTextGeom().getHeight() + spacing;
			if ( rightalign )
				hoffset = -(int)(element.getTextGeom().getWidth()) - 5;
			element.getTextGeom().setLocalTranslation(loc.x*lastWidth+hoffset,loc.y*lastHeight+voffset,loc.z);
			if ( listdown )	voffset -= element.getTextGeom().getHeight() + spacing;
			else			voffset += element.getTextGeom().getHeight() + spacing;
		}

		needsUpdate = false;
	}

	/**
	 * @return the spacing
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * @param spacing the spacing to set
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * @return the listdown
	 */
	public boolean isListdown() {
		return listdown;
	}

	/**
	 * @param listdown the listdown to set
	 */
	public void setListdown(boolean listdown) {
		this.listdown = listdown;
	}

	/**
	 * @return the rightalign
	 */
	public boolean isRightalign() {
		return rightalign;
	}

	/**
	 * @param rightalign the rightalign to set
	 */
	public void setRightalign(boolean rightalign) {
		this.rightalign = rightalign;
	}

	/**
	 * @return the textColor
	 */
	public ColorRGBA getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(ColorRGBA textColor) {
		this.textColor = textColor;
	}

	public void clear() {
		for ( TextOverlayElement e : overlayElements )
			e.setTTL(0);
	}
}