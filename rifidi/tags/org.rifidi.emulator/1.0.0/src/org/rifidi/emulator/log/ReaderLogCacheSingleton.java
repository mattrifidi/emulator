/**
 * 
 */
package org.rifidi.emulator.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderLogCacheSingleton {

	private static ReaderLogCacheSingleton instance;

	/**
	 * Map containing all readers and log messages
	 */
	private Map<String, ArrayList<String>> consoleCache = new HashMap<String, ArrayList<String>>();
	/**
	 * History size of log
	 */
	private int maxCacheLines = 2000;

	private ReaderLogCacheSingleton() {

	}

	public static ReaderLogCacheSingleton getInstance() {
		if (instance == null) {
			instance = new ReaderLogCacheSingleton();
		}
		return instance;
	}

	public void addMessage(String readerName, String message) {
		if (!consoleCache.containsKey(readerName)) {
			// Create new log for readerName

			consoleCache.put(readerName, new ArrayList<String>());
		}
		List<String> log = consoleCache.get(readerName);
		synchronized (this) {
			if (log.size() > maxCacheLines) {
				// Remove item at index 0 if it is longer than maxCacheLines
				log.remove(0);
			}
			log.add(message);
		}

	}

	/**
	 * @return get the maxCacheLines
	 */
	public int getMaxCacheLines() {
		return maxCacheLines;
	}

	/**
	 * @param maxCacheLines
	 *            to maxCacheLines set
	 */
	public void setMaxCacheLines(int maxCacheLines) {
		this.maxCacheLines = maxCacheLines;
	}

	public List<String> getCache(String readerName) {
		// Return logged stuff and remove it from cache

		List<String> log = consoleCache.get(readerName);
		ArrayList<String> ret = new ArrayList<String>(log);
		synchronized (this) {
			log.removeAll(ret);
		}
		return ret;
	}
}
