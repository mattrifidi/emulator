/**
 * 
 */
package org.rifidi.emulator.log;

import java.util.List;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public interface ReaderLogService {
	
	void addMessage(String readerName, String message);
	
	int getMaxCacheLines();
	
	void setMaxCacheLines(int size);
	
	List<String> getCache(String readerName);
}
