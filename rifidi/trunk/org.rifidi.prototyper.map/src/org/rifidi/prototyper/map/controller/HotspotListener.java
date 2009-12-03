/**
 * 
 */
package org.rifidi.prototyper.map.controller;

import org.rifidi.prototyper.model.HotspotViewModel;

/**
 * An interface implemented by classes who want to be notified when hotspot
 * models are added or removed.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface HotspotListener {

	/**
	 * Called when a hotspot is added
	 * 
	 * @param model
	 *            the hotspot model that was added
	 */
	void hotspotAdded(HotspotViewModel model);

	/**
	 * Called when a hotspot model is removed.
	 * 
	 * @param model
	 *            the model that was removed.
	 */
	void hotspotDeleted(HotspotViewModel model);

}
