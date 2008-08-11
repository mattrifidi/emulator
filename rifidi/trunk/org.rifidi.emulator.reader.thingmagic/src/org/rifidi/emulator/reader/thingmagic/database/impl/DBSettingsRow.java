package org.rifidi.emulator.reader.thingmagic.database.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;

public class DBSettingsRow extends AbstractMap<String, String> implements IDBRow{

	DateFormat dateFormatter;
	
	public DBSettingsRow () {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'000'");
		
		/*
		 * set the timezone of the dateFormatter to GMT (UTC) time.
		 */
		dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
	}
	@Override
	public Set<Map.Entry<String, String>> entrySet() {
		Set<Map.Entry<String, String>> settings = new HashSet<Map.Entry<String, String>>();
		
		settings.add(new AbstractMap.SimpleEntry<String, String>("current_time", dateFormatter.format( new Date(System.currentTimeMillis())) ));
		settings.add(new AbstractMap.SimpleEntry<String, String>("version", "2.3.14 (2005-11-23T14:22:53-05:00 Tesla )"));
		settings.add(new AbstractMap.SimpleEntry<String, String>("supported_protocols", "EPC1 GEN2"));
		return settings;
	}

}
