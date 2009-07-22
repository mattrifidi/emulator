package org.rifidi.emulator.rmi.server;

import java.util.List;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

public interface RifidiManagerInterface {

	/**
	 * 
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public abstract Boolean createReader(GeneralReaderPropertyHolder properties) throws Exception;
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public abstract Boolean removeReader(String name) throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract List<String> getReaderList() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract List<String> getSupportedReaderTypes() throws Exception;
	
	/**
	 * 
	 * @param readerModule
	 * @return
	 * @throws Exception
	 */
	public abstract String getReaderXMLDescription(String readerModule) throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Boolean cleanup() throws Exception;
	
	/**
	 * Get the cached log statements of a specific Reader
	 * @return List of cached log statements 
	 * @throws Exception
	 */
	public abstract List<String> getCachedLogs(String readerName) throws Exception;
}