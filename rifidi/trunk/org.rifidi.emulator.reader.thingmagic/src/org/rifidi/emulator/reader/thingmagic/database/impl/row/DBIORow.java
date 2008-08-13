package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBReadException;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBWriteException;

public class DBIORow implements IDBRow {

	private GPIOController gpioController;

	public DBIORow(GPIOController gpioController) {
		this.gpioController = gpioController;
	}

	@Override
	public boolean containsColumn(String column) {
		
		return column.equals("data");
	}
	

	@Override
	public boolean isReadable(String column) {
		return column.equals("data");
	}

	@Override
	public boolean isWritable(String column) {
		return column.equals("data");
	}

	@Override
	public String get(String column) throws DBReadException {
		
		if (column.equals("data")){
			short io = 0;
			if (gpioController.getGPIState(0)) {
				io |= 0x02;
			}
			
			if (gpioController.getGPIState(1)) {
				io |= 0x20;
			}
			/*
			 * format the hex string to what the thing magic would actually return.
			 */
			return "0x" + Long.toHexString(0x100000000L | io).substring(1);
		}
		
		/* !Should never get here!
		 * If we actually do... there is something seriously
		 * wrong with the code that calls this class, or this method itself.
		 * 
		 * Better throwing a custom RuntimeException than
		 * trying to guess what caused the null pointers... 
		 */
		throw new DBReadException("Could not read from field " + column);
	}

	@Override
	public String put(String column, String value) {
		if (column.equals("data")){
			int io = Integer.parseInt(value.substring(2), 16 );
			
			if ((io & 0x04) == 0x04){
				gpioController.setGPOHight(0);
			} else {
				gpioController.setGPOLow(0);
			}
			
			if ((io & 0x08) == 0x08){
				gpioController.setGPOHight(1);
			} else {
				gpioController.setGPOLow(1);
			}
			
			if ((io & 0x10) == 0x10){
				gpioController.setGPOHight(2);
			} else {
				gpioController.setGPOLow(2);
			}
			return value;
		}
		
		/* !Should never get here!
		 * If we actually do... there is something seriously
		 * wrong with the code that calls this class, or this method itself.
		 * 
		 * Better throwing a custom RuntimeException than
		 * trying to guess what caused the null pointers... 
		 */
		throw new DBWriteException("Could not write to field " + column);
	}

}
