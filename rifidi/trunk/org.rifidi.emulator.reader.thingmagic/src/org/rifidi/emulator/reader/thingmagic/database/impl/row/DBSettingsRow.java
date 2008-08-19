package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;

public class DBSettingsRow implements IDBRow {

	private static String CURRENT_TIME = "current_time";
	private static String VERSION = "version";
	private static String SUPPORTED_PROTOCOLS = "supported_protocols";

	private static Set<String> columns = new HashSet<String>();
	private static DateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'kk:mm:ss.SSS'000'");

	private static boolean initialized = false;

	public DBSettingsRow() {
		initialize();
	}

	private static void initialize() {
		/*
		 * we only really need to do this once.
		 */
		if (!initialized) {
			/*
			 * set the timezone of the dateFormatter to GMT (UTC) time.
			 */
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));

			/* add columns to the set */

			columns.add(CURRENT_TIME);
			columns.add(VERSION);
			columns.add(SUPPORTED_PROTOCOLS);

			initialized = true;
		}
	}

	@Override
	public boolean isReadable(String column) {
		/*
		 * All columns/fields are readable
		 */
		return columns.contains(column);
	}

	@Override
	public boolean isWritable(String key) {
		/* This table has no writable fields/columns */
		return false;
	}

	@Override
	public boolean containsColumn(String column) {
		return columns.contains(column);
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub

		if (key.equals(CURRENT_TIME)) {
			return dateFormatter.format(new Date(System.currentTimeMillis()));
		}

		if (key.equals(VERSION)) {
			return "2.3.14 (2005-11-23T14:22:53-05:00 Tesla )";
		}

		if (key.equals(SUPPORTED_PROTOCOLS)) {
			return "EPC1 GEN2";
		}

		return null;
	}

	@Override
	public String put(String key, String value) {
		return null;
	}

}
