/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic_old.database.enums;

import java.util.Comparator;

/**
 * @author jmaine
 * Sole purpose is to define a combine interfaces.
 */
public interface GenericTableRow<T> extends Comparator<T> {
	public boolean isReadable();
	public boolean isWriteable();
}
