package org.rifidi.emulator.reader.thingmagic.database.impl.tagbuffer;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBWriteException;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.impl.RifidiTag;


/*
 * We use AbstractMap here since we are defining a key-value pair of tag data and to keep from
 * re-inventing the wheel with Map collections.
 */
//TODO implement this better.
public class TagRowData implements IDBRow {
	private static Log logger = LogFactory.getLog(ThingMagicTagTableMemory.class);
	
	static private String ID = "id";
	static private String ANTENNA_ID = "antenna_id";
	static private String READ_COUNT = "read_count";
	static private String PROTOCOL_ID = "protocol_id";
	static private String KILLED = "killed";
	static private String PASSWORD = "password";
	static private String LOCKED = "locked";
	static private String FREQUENCY = "frequency";
	static private String DSPMICROS = "dspmicros";
	static private String TIMESTAMP = "timestamp";
	
	static private Set<String> columns = new HashSet<String>();
	static private Set<String> readable = new HashSet<String>();
	static private Set<String> writable = new HashSet<String>();

	private static boolean initialized = false;
	
	
	private RifidiTag tag;
	
	/**
	 * This creates a map of tag data keys (based on the way the Mercury 4, ThingMagic reader
	 * defines them) and their respective values.
	 * @param tag The RFID tag to get the tag data from
	 */
	public TagRowData(RifidiTag tag){
		logger.debug("Creating tag row data");
		this.tag = tag;
	
		initialize();
	}
	
	/* we only need to do this once... and be done with it.*/
	private static void initialize(){
		if (!initialized){
			columns.add(ID);
			columns.add(ANTENNA_ID);
			columns.add(READ_COUNT);
			columns.add(PROTOCOL_ID);
			columns.add(KILLED);
			columns.add(PASSWORD);
			columns.add(LOCKED);
			columns.add(FREQUENCY);
			columns.add(DSPMICROS);
			columns.add(TIMESTAMP);
			
			
			readable.add(ID);
			readable.add(ANTENNA_ID);
			readable.add(READ_COUNT);
			readable.add(PROTOCOL_ID);
			// TODO: Finish adding the reabables
			readable.add(TIMESTAMP);
			
			// TODO: Add the writables.
			
			initialized = true;
		}
	}
	
	public RifidiTag getTag(){
		return this.tag;
	}
	
	@Override
	public boolean containsColumn(String key) {
		return columns.contains(key);
	}

	@Override
	public String get(String key){
		// TODO Auto-generated method stub
		if (key.equals(ID)) {
			return "0x" + tag.toString().replace(" ", "");
		}
		
		if (key.equals(ANTENNA_ID)){
			Integer antenna = tag.getAntennaLastSeen();
			antenna++;
			return antenna.toString();
		}
		
		if (key.equals(READ_COUNT)){
			/*
			 * For the Mercury 4, Thing Magic reader, the read count is how many times
			 * from the start of a command to the end of it that it has read it in the
			 * antenna field of view. Which for this emulator is once and only once.
			 */
			return "1";
		}
		
		if (key.equals(PROTOCOL_ID)) {
			String protocolID = "0";
			if (tag.getTagGen() == TagGen.GEN1) {
				protocolID = "1";
			}
			if (tag.getTagGen() == TagGen.GEN2) {
				protocolID = "12";
			}
			return protocolID;
		}
		
		
		
		if (key.equals(TIMESTAMP)){
			/*
			 * timestamp
			 * Time stamp is 'seconds from epoc'.'microseconds'
			 * 1) Get the current time in milliseconds and convert it to a String.
			 * 2) Insert the period to make it seconds from epoc--not milliseconds from epoc.
			 * 3) Append three '0's to fake microseconds.
			 * And now we have a passable thingmagic timestamp.
			 */		
			StringBuilder timeStamp = new StringBuilder(Long.toString(System.currentTimeMillis()));
			timeStamp.insert(timeStamp.length()-3, '.');
			timeStamp.append("000");
			
			return timeStamp.toString();
		}
		
		/* Best effort to complete the command...
		 * If it is not readable or not there, we return an empty String.
		 */
		return "";
	}

	@Override
	public String put(String key, String value) throws DBWriteException {
		// TODO Auto-generated method stub
		
		
		/* !Should never get here!
		 * If we actually do... there is something seriously
		 * wrong with the code that calls this class, or this method itself.
		 * 
		 * Better throwing a custom RuntimeException than
		 * trying to guess what caused the null pointers... 
		 */
		throw new DBWriteException("Could not write to field " + key);
	}

	@Override
	public boolean isReadable(String column) {
		/* 
		 * this method must be called if get(column) can be called
		 */
		return readable.contains(column);
	}

	@Override
	public boolean isWritable(String column) {
		/* 
		 * this method must be called if put(column, value) can be called
		 */
		return writable.contains(column);
	}
	
	/*
	 * On the next two methods we simple call the equivalent method of the
	 * internal tag object.
	 */
	
	@Override
	public boolean equals (Object o){
		if (o instanceof TagRowData){
			return tag.equals(((TagRowData) o).getTag());
		} 
		
		return false;
	}
	
	@Override 
	public int hashCode(){
		return tag.hashCode();
	}
}
