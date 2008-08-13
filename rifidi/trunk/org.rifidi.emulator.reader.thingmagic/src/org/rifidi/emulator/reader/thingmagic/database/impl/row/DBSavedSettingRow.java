package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBReadException;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBWriteException;


public class DBSavedSettingRow implements IDBRow {
	
	
	private static Map<String, String> savedSettings = new HashMap<String, String>(); 
	
	private static boolean initialized = false;
	
	public DBSavedSettingRow () {
		initialize();
	}
	
	private static void initialize(){
		if (!initialized){
			//TODO find something better for ip related values than "localhost"
			savedSettings.put("hostname", "mercury4");
			savedSettings.put("iface", "dhcp");
			savedSettings.put("dhcpcd", "-t 15");
			savedSettings.put("ip_address", "localhost");
			savedSettings.put("netmask", "localhost");
			savedSettings.put("gateway", "localhost");
			savedSettings.put("ntp_servers", "localhost");
			savedSettings.put("uhf_power_centidbm", "3250");
			savedSettings.put("primary_dns", "localhost");
			savedSettings.put("secondary_dns", "localhost");
			savedSettings.put("domain_name", "thingmagic.com");
			
			initialized = true;
		}
	}

	@Override
	public boolean containsColumn(String column) {
		return savedSettings.containsKey(column);
	}

	@Override
	public boolean isReadable(String column) {
		/*
		 * All columns are readable 
		 */
		return savedSettings.containsKey(column);
	}

	@Override
	public boolean isWritable(String column) {
		/*
		 * All columns are writable
		 */
		return savedSettings.containsKey(column);
	}
	
	@Override
	public String get(String key) throws DBReadException {
		// TODO Auto-generated method stub
		String retVal = savedSettings.get(key);
		if (retVal == null) {
			/* !Should never get here!
			 * If we actually do... there is something seriously
			 * wrong with the code that calls this class, or this method itself.
			 * 
			 * Better throwing a custom RuntimeException than
			 * trying to guess what caused the null pointers... 
			 */
			throw new DBReadException("Could not read from field " + key); 
		}
		return retVal;
	}

	@Override
	public String put(String key, String value) {
		if (savedSettings.containsKey(key)){
			return savedSettings.put(key, value);
		}
		/* !Should never get here!
		 * If we actually do... there is something seriously
		 * wrong with the code that calls this class, or this method itself.
		 * 
		 * Better throwing a custom RuntimeException than
		 * trying to guess what caused the null pointers... 
		 */
		throw new DBWriteException("Could not write to field " + key);
	}

}
