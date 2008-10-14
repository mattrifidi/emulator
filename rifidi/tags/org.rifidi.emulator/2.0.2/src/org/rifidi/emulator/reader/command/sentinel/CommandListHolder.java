package org.rifidi.emulator.reader.command.sentinel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Represents a map of Commands mapped to CommandLists.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class CommandListHolder {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(CommandListHolder.class);

	/**
	 * Map to return to the sentinel once the parsing is complete.  
	 */
	private Map<String, ArrayList<byte[]>> holder;
	
	/**
	 * ArrayList of bytes to hold output commands.  
	 */
	private ArrayList<byte[]> arrayOfBytes;

	/**
	 * Create a new ArrayList to hold the commands.
	 */
	public CommandListHolder() {
		arrayOfBytes = new ArrayList<byte[]>();
		holder = new HashMap<String, ArrayList<byte[]>>();
	}
	
	/**
	 * Add an output command to the holder.  
	 * 
	 * @param arg
	 */
	public void addCommandListArg(String arg) {
		logger.debug("adding a byte command: " + arg);
		arrayOfBytes.add(arg.getBytes());
	}

	/**
	 * Add an input command to the holder.
	 * 
	 * @param arg
	 */
	public void addCommand(String arg) {
		logger.debug("adding a command: " + arg);
		arg=arg.toLowerCase();
		
		ArrayList<byte[]> newArrayList = new ArrayList<byte[]>(arrayOfBytes);
	
		holder.put(arg, newArrayList);
		arrayOfBytes.clear();
	}

	/**
	 * Returns the map of commands that this holder now posesses.  
	 * 
	 * @return	The map of commands.
	 */
	public Map<String, ArrayList<byte[]>> getCommandListHolder() {
		return holder;
	}
}
