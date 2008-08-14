package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import java.util.HashMap;
import java.util.Map;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;


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
	public String get(String key) {
		return savedSettings.get(key);
	}

	@Override
	public String put(String key, String value) {
		if (savedSettings.containsKey(key)){
			return savedSettings.put(key, value);
		}
		return null;
	}

}
