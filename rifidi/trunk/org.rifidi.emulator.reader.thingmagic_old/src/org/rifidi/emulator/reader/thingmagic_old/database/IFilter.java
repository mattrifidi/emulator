package org.rifidi.emulator.reader.thingmagic_old.database;

import java.util.*;

/**
 * 
 * @author jmaine
 *
 * @param <T>
 */
public interface IFilter<T> {
	
	public Collection<T> filter(Collection<T> rows);

}
