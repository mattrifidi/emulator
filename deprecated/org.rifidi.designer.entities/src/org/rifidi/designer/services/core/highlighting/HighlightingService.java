/*
 *  HighlightingService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.highlighting;

import java.util.Set;

import org.rifidi.designer.entities.VisualEntity;

import com.jme.renderer.ColorRGBA;

/**
 * Service for applying renderstates for highlighting.
 * 
 * @author Jochen Mader - jochen@pramari.com - Jun 30, 2008
 * 
 */
public interface HighlightingService {

	/**
	 * Change the highlight state of entities.
	 * 
	 * @param color
	 * @param highlight
	 */
	void changeHighlighting(ColorRGBA color, Set<VisualEntity> highlight);

	/**
	 * Change the highlight of already highlighted entities.
	 * 
	 * @param color
	 * @param newcolor
	 * @param entity
	 */
	void changeHighlightColor(ColorRGBA color, ColorRGBA newcolor,
			Set<VisualEntity> hilight);

	/**
	 * Remove highlighting from the given set of entities.
	 * 
	 * @param hilight
	 */
	void clearHighlights(Set<VisualEntity> hilight);

	/**
	 * Remove all highlights.
	 */
	void clearAllHighlights();
}
