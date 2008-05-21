/*
 *  _C1G2Write.java
 *
 *  Created:	Oct 10, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import javax.naming.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpec;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.C1G2Operations;
import org.rifidi.emulator.rmi.client.ClientCallbackInterface;
import org.rifidi.services.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.services.tags.impl.C1G2Tag;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * 
 * This class performs the Write access operation on C1G2 Tags
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class _C1G2Write implements _OpSpec {

	int OpSpecID;

	int memoryBank;

	short wordPtr;

	byte[] writeData;

	byte[] accessPassword;
	
	private ClientCallbackInterface callback;
	
	private Antenna antenna;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_C1G2Write.class);

	public _C1G2Write(int OpSpecID, int memoryBank, short wordPtr,
			short[] writeData, int accessPassword, ClientCallbackInterface callback, Antenna ant) {
		this.OpSpecID = OpSpecID;
		this.memoryBank = memoryBank;
		this.wordPtr = wordPtr;
		this.writeData = new byte[writeData.length * 2];
		int pointer = 0;
		for (int i = 0; i < writeData.length; i++) {
			this.writeData[pointer++] = (byte) (writeData[i]>>8);
			//logger.debug("byte: " + ByteAndHexConvertingUtility.toHexString(this.writeData[pointer - 1]));
			this.writeData[pointer++] = (byte)(writeData[i]);
			//logger.debug("byte: " + ByteAndHexConvertingUtility.toHexString(this.writeData[pointer - 1]));
		}
		
		this.callback = callback;
		this.antenna = ant;


		this.accessPassword = ByteAndHexConvertingUtility.intToByteArray(
				accessPassword, 4);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpec#performOperation(org.rifidi.tags.Gen1Tag)
	 */
	public _OpSpecResult performOperation(RifidiTag tag) {

		C1G2Tag c1g2tag = (C1G2Tag) tag.getTag();

		int result = 0;

		try {
			logger.debug("Attempting to write tag");
			C1G2Operations.C1G2WriteTagMem(c1g2tag, tag.getTagEntitiyID(), this.memoryBank, this.wordPtr,
					writeData, accessPassword, callback, antenna);
		} catch (AuthenticationException e) {
			logger.debug("C1G2Write error: " + e.getMessage());
			result = 1;
		} catch (InvalidMemoryAccessException e) {
			logger.debug("C1G2Write error: " + e.getError());
			result = 1;
		}

		short wordsWritten;
		if (result == 0) {
			wordsWritten = (short) (writeData.length / 2);
		} else {
			wordsWritten = 0;
		}

		return new _C1G2WriteOpSpecResult(this.OpSpecID, wordsWritten, result);
	}
}
