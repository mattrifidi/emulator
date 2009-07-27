/**
 * 
 */
package org.rifidi.emulator.reader.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is an abstract class that each reader plugin should implement.
 * ReaderModuleFactory creates ReaderModules.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public abstract class ReaderModuleFactory {

	/**
	 * Create a reader module
	 * 
	 * @param properties
	 * @return
	 */
	public abstract ReaderModule createReaderModule(
			GeneralReaderPropertyHolder properties);

	/**
	 * Get a general reader property holder that contains all the defautl values
	 * for the reader.
	 * 
	 * @return
	 */
	public abstract GeneralReaderPropertyHolder getDefaultProperties();

	/**
	 * 
	 * @param readerName
	 * @return
	 */
	public abstract String getReaderXMLDescription();

	/**
	 * Get the type of reader. Should be unique and will be displayed in the UI
	 * 
	 * @return
	 */
	public abstract String getReaderType();

	/**
	 * Get the cannonical class name of the ReaderModule class that this class
	 * creates
	 * 
	 * @return
	 */
	public abstract String getReaderModuleClassName();

	/**
	 * helper method because the getReaderXMLDescription will probably need to
	 * read in the XML from a file on the classpath
	 * 
	 * @param stream
	 * @return
	 */
	protected String getStreamAsString(InputStream stream) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
