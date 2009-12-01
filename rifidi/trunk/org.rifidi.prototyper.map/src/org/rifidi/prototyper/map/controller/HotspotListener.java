/**
 * 
 */
package org.rifidi.prototyper.map.controller;

import org.rifidi.prototyper.model.HotspotViewModel;

/**
 * @author kyle
 *
 */
public interface HotspotListener {
	
	void hotspotAdded(HotspotViewModel model);

	void hotspotDeleted(HotspotViewModel model);

}
