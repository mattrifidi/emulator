package org.rifidi.emulator.reader.thingmagic.database;

import java.util.*;

/**
 * 
 * @author jmaine
 *
 * @param <K>
 * @param <V>
 */
public interface DataBaseTable <K, V> {
	public List<List<Object>> searchTable(List<K> keyList, IFilter where, int timeout);
}
