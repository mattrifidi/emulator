package org.rifidi.ui.common.reader.callback;

import org.rifidi.emulator.tags.Gen1Tag;

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
	public void tagIDChanged(byte[] oldID, Gen1Tag tag);

}
