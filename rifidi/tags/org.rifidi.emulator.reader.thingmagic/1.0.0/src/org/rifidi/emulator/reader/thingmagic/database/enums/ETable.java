package org.rifidi.emulator.reader.thingmagic.database.enums;

/**
 * @author jmaine
 * This enum lists all tables possible in the thing magic reader.
 */
public enum ETable {
	TAG_ID, TAG_DATA, SETTINGS, IO, SAVED_SETTINGS;
	
	/**
	 * Get the table rows associated with this table.
	 * @return An array of table rows.
	 */
	public GenericTableRow<?>[] getTableRows(){
		GenericTableRow<?>[] retVal = null;
		switch(this){
			case TAG_ID:
				retVal = Etag_id.values();
			break;
			default:
				/* better than letting null pointers dangle around */
				throw new UnsupportedOperationException(
						"This enum ," + this + ", is not suported yet."
				);
		}
		
		return retVal;
	}
	
	/**
	 * Grab the table row that matches the given string value for this table.
	 * @param arg The string value to find the enum that matches.
	 * @return the enum matching arg, NULL if not.
	 */
	public GenericTableRow<?> getTableRowsValueOf(String arg){
		GenericTableRow<?> retVal = null;
		switch(this){
			case TAG_ID:
				retVal = Etag_id.valueOf(arg);
			break;
			default:
				/* better than letting null pointers dangle around */
				throw new UnsupportedOperationException(
						"This enum ," + this + ", is not suported yet."
				);
		}
		
		return retVal;
	}
}
