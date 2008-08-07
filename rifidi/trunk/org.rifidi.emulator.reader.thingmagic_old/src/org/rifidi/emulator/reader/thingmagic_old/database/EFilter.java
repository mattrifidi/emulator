/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic_old.database;

/**
 * @author jmaine
 * This defines all the types of simple filters we can do.
 */
public enum EFilter {
	Equales, NotEquales, LessThan, 
	GreaterThan, LessThanOrEquales, GreaterThanOrEquals;
	
	/**
	 * Here we grab the appropriate enum for the simple filter type
	 * @param arg The string to grab the filter type from
	 * @return The filter type.
	 */
	public static EFilter getFilterType(String arg){
		EFilter retVal = null;
		
		if (arg.contains("<>")){
			retVal = NotEquales;
		} else if (arg.contains("<=")) {
			retVal = LessThanOrEquales;
		} else if (arg.contains(">=")){
			retVal = GreaterThanOrEquals;
		} else if (arg.contains("=")){
			retVal = Equales;
		} else if (arg.contains("<")) {
			retVal = LessThan;
		} else if (arg.contains(">")) {
			retVal = GreaterThan;
		} else {
			throw new IllegalArgumentException();
		}
		
		
		return retVal;
	}
	
	/**
	 * Return a toString representation of this object.
	 * @return A string version of this Enum.
	 */
	public String toString(){
		String retVal = "";
		switch(this){
			case Equales:
				retVal = "=";
			break;
			case NotEquales:
				retVal = "<>";
			break;
			case LessThan:
				retVal = "<";
			break;
			case GreaterThan:
				retVal = ">";
			break;
			case LessThanOrEquales:
				retVal = "<=";
			break;
			case GreaterThanOrEquals:
				retVal = ">=";
		}
		
		return retVal;
	}
}
