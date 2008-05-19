package org.rifidi.ui.common.reader.callback;

import org.rifidi.services.tags.IGen1Tag;

/**
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
public interface TagIDChangedCallbackInterface {
	
	/**
	 * this is the callback method beeing invoked when the tag id changed
	 * @param oldID
	 * @param newID
	 */
	public void tagIDChanged(byte[] oldID, IGen1Tag tag);

}
